
from sklearn.metrics.pairwise import cosine_similarity
from flask import Flask, request, jsonify
from flaskext.mysql import MySQL
from py_eureka_client import eureka_client
from flask_cors import CORS
import requests
from io import BytesIO
from tensorflow.keras.preprocessing import image
from tensorflow.keras.applications.vgg16 import VGG16, preprocess_input
from tensorflow.keras.models import Model
from PIL import Image
import pandas as pd
import numpy as np
# from tensorflow.keras.preprocessing.image import img_to_array
import os
import pymysql
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
    

mysql = MySQL()
app = Flask(__name__)
CORS(app)

app.config['MYSQL_DATABASE_USER'] = 'root'
app.config['MYSQL_DATABASE_PASSWORD'] = 'Abc@123456789'
app.config['MYSQL_DATABASE_DB'] = 'productdb'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
app.config['MYSQL_DATABASE_PORT'] = 3302  # Thêm port ở đây

rest_port = 8086
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="data-aggregation-service",
                   instance_port=rest_port)

mysql.init_app(app)


# Hàm tạo model
def get_extract_model():
    vgg16_model = VGG16(weights="imagenet")
    extract_model = Model(inputs=vgg16_model.inputs, outputs=vgg16_model.get_layer("fc1").output)
    return extract_model

# Hàm tiền xử lý, chuyển đổi hình ảnh thành tensor
def image_preprocess(img):
    img = img.resize((224, 224))
    img = img.convert("RGB")
    x = image.img_to_array(img)
    x = np.expand_dims(x, axis=0)
    x = preprocess_input(x)
    return x

# Hàm trích xuất đặc trưng từ ảnh
def extract_vector(model, image_path):
    try:
        response = requests.get(image_path)
        img = Image.open(BytesIO(response.content))
        img_tensor = image_preprocess(img)

        # Trích đặc trưng
        vector = model.predict(img_tensor)[0]
        # Chuẩn hóa vector bằng cách chia cho L2 norm
        vector = vector / np.linalg.norm(vector)
        return vector
    except Exception as e:
        print(f"Không thể xử lý ảnh {image_path}: {e}")
        return None

# Hàm kết nối đến cơ sở dữ liệu
def connect_db(user, password, db, host, port):
    return pymysql.connect(user=user,
                           password=password,
                           database=db,
                           host=host,
                           port=port,  # Thêm tham số port
                           cursorclass=pymysql.cursors.DictCursor)

@app.route('/api/v1/aggreations/<int:user_id>', methods=['GET'])
def recommend_for_user(user_id):
    # Kết nối đến cơ sở dữ liệu 1 và truy vấn bảng 'product'
    conn1 = connect_db('root', 'Abc@123456789', 'productdb', '127.0.0.1', 3302)
    cursor1 = conn1.cursor()
    
    query1 = "SELECT id FROM product"
    cursor1.execute(query1)
    product_data = cursor1.fetchall()

    query1_1 = "select DISTINCT product.id as product_id, product_review.id_customer, product_review.rating from product join product_review on product.id = product_review.product_id"
    cursor1.execute(query1_1)
    product_data_review = cursor1.fetchall()


    cursor1.close()
    conn1.close()

    # Kết nối đến cơ sở dữ liệu 2 và truy vấn bảng 'order'
    conn2 = connect_db('root', 'Abc@123456789', 'purchasedb', '127.0.0.1', 3304)
    cursor2 = conn2.cursor()
    
    query2 = "SELECT DISTINCT orders.customer_id, order_item.product_id FROM orders JOIN order_item ON orders.id = order_item.order_id"
    cursor2.execute(query2)
    order_data = cursor2.fetchall()
    cursor2.close()
    conn2.close()

    # Kết nối đến cơ sở dữ liệu 3 và truy vấn bảng 'user'
    conn3 = connect_db('root', 'Abc@123456789', 'userdb', '127.0.0.1', 3301)
    cursor3 = conn3.cursor()
    
    query3 = "SELECT id FROM user"
    cursor3.execute(query3)
    user_data = cursor3.fetchall()
    cursor3.close()
    conn3.close()

    df_orders = pd.DataFrame(order_data)
    df_reviews = pd.DataFrame(product_data_review)
    df_products = pd.DataFrame(product_data)
    df_users = pd.DataFrame(user_data)

    # Initialize the matrix with NaN values, with products as index (rows) and users as columns
    user_product_matrix = pd.DataFrame(np.nan, index=df_products['id'], columns=df_users['id'])
    user_product_matrix.index.name = 'user_id'  # Tên hàng
    user_product_matrix.columns.name = 'product_id'    # Tên cột
    # Populate the matrix with ratings
    for _, review in df_reviews.iterrows():
        user_product_matrix.loc[review['product_id'], review['id_customer']] = review['rating']

    # Fill in the matrix based on purchase data
    for _, order in df_orders.iterrows():
        if pd.isna(user_product_matrix.loc[order['product_id'], order['customer_id']]):
            user_product_matrix.loc[order['product_id'], order['customer_id']] = get_ratingproduct(order['product_id'])  # Default rating for purchases

    # Fill in the matrix for non-purchases
    user_product_matrix = user_product_matrix.fillna(0)
    user_product_matrix = pd.DataFrame(user_product_matrix).T  # .T để chuyển đổi từ hàng thành cột (chuyển index và columns)
    # Step 2: Calculate user similarity matrix
    user_similarity = cosine_similarity(user_product_matrix)
    user_similarity_df = pd.DataFrame(user_similarity, index=user_product_matrix.index, columns=user_product_matrix.index)

    # Trả về dữ liệu từ cả ba cơ sở dữ liệu
    recommended_products_for_user_1 = recommend_products(user_id, user_product_matrix, user_similarity_df)
    return jsonify(recommended_products_for_user_1)

def get_ratingproduct(product_id):
    conn1 = connect_db('root', 'Abc@123456789', 'productdb', '127.0.0.1', 3302)
    cursor1 = conn1.cursor()

    try:
        query1 = "SELECT rating FROM product WHERE id = %s"
        cursor1.execute(query1, (product_id,))
        result = cursor1.fetchone()
        
        if result is not None:
            return result['rating']
        else:
            return 0  # Trả về 0 nếu không tìm thấy sản phẩm hoặc không có rating
    finally:
        cursor1.close()
        conn1.close()
# Step 3: Generate recommendations for a specific user
def recommend_products(user_id, user_item_matrix, user_similarity_df, top_n=5):
    if user_id not in user_similarity_df.index:
        return jsonify({"error":"User not found"})
    
    similar_users = user_similarity_df[user_id].sort_values(ascending=False)[1:]
    user_product_ratings = user_item_matrix.loc[user_id]
    
    recommendations = pd.Series(dtype='float64')

    for similar_user, similarity in similar_users.items():
        similar_user_ratings = user_item_matrix.loc[similar_user]
        for product in similar_user_ratings.index:
            if user_product_ratings[product] == 0:  # Consider only products the user hasn't rated
                if product not in recommendations:
                    recommendations[product] = similarity * similar_user_ratings[product]
                else:
                    recommendations[product] += similarity * similar_user_ratings[product]

    recommendations = recommendations.sort_values(ascending=False).head(top_n)
    return recommendations.index.tolist()

@app.route('/api/v1/aggreations/products', methods=['POST'])
def get_products():

    # Lấy dữ liệu từ request body
    data = request.get_json()
    search_image = data.get('image')

    if not search_image:
        return jsonify({"error": "No image link provided"}), 400
    
    conn = mysql.connect()
    cursor = conn.cursor()
    
    query = "SELECT id, image FROM product"
    cursor.execute(query)
    
    data = cursor.fetchall()
    
    image_data = []
    for row in data:
        image_data.append({'id': row[0], 'image': row[1]})
    
    cursor.close()
    conn.close()

    nearest_images = handLe(model, image_data, search_image)


    return jsonify(nearest_images)


def handLe(model,image_data, search_image):

    # Trích xuất đặc trưng từ tất cả ảnh
    vectors = []
    valid_ids = []
    for item  in image_data:
        vector = extract_vector(model, item['image'])
        if vector is not None:
            vectors.append(vector)
            valid_ids.append(item['id'])

    # Chuyển đổi danh sách vectors thành numpy array
    vectors = np.array(vectors)


    # Trích đặc trưng ảnh search
    search_vector = extract_vector(model, search_image)
    # Tính khoảng cách từ search_vector đến tất cả các vector
    distances = np.linalg.norm(vectors - search_vector, axis=1)
    # Sắp xếp và lấy ra K vector có khoảng cách ngắn nhất
    K = 5
    ids_sorted = np.argsort(distances)[:K]

    # # Tạo output
    # nearest_images = [{"id": valid_ids[id], "link_url_picture": image_data[id]['image']} for id in ids_sorted]
    concatenated_ids = "_".join(str(valid_ids[id]) for id in ids_sorted)

    return concatenated_ids



if __name__ == '__main__':
    model = get_extract_model()
    app.run(debug=True, port=rest_port)