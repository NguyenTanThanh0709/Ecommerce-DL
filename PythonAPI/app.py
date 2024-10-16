from flask import Flask, request, jsonify
from flaskext.mysql import MySQL
from py_eureka_client import eureka_client
from flask_cors import CORS
import requests
from PIL import Image
from io import BytesIO
import os
from glob import glob
import numpy as np
import faiss
from sentence_transformers import SentenceTransformer
from collaborative_filtering import CF  # Import the CF class
import pymysql
import pandas as pd
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


# =========================Content Image Base===========================

model = SentenceTransformer('clip-ViT-B-32')
chunk_size = 32

@app.route('/api/v1/aggreations/products', methods=['POST'])
def get_products():

    # Lấy dữ liệu từ request body
    data = request.get_json()
    image_url = data.get('image')  # Renamed the variable

    if not image_url:
        return jsonify({"error": "No image link provided"}), 400
    
    conn = mysql.connect()
    if not conn:
        return jsonify({"error": "Could not connect to the database"}), 500
    cursor = conn.cursor()
    
    query = "SELECT id, image FROM product"
    cursor.execute(query)
    
    data = cursor.fetchall()
    if len(data) == 0:
        return jsonify({"error": "No products found in the database"}), 404

    image_data = []
    for row in data:
        image_data.append({'id': row[0], 'image': row[1]})
    
    cursor.close()
    conn.close()


    # Process images in chunks --------------------------------------------------------------

    embeddings = []


    index = None
    if os.path.exists('index.faiss') and os.path.exists('ids.npy'):
        index = faiss.read_index('index.faiss')
        ids = np.load('ids.npy')
        print("Index loaded successfully.")
    else:
            # Process images in chunks
        for i in range(0, len(image_data), chunk_size):
            chunk = image_data[i:i + chunk_size]
            chunk_embeddings = process_chunk(chunk)
            embeddings.extend(chunk_embeddings)

        dimension = len(embeddings[0])
        index = faiss.IndexFlatIP(dimension)
        index = faiss.IndexIDMap(index)

        vectors = np.array(embeddings).astype('float32')
        ids = np.array(range(0, len(vectors))).astype('int64')
        index.add_with_ids(vectors, np.array(range(len(embeddings))))

        faiss.write_index(index, 'index.faiss')
        #
        # Save IDs to a .npy file
        with open('ids.npy', 'wb') as f:
            np.save(f, ids)

        print("Index and IDs saved successfully.")

    # Call the search_image function with the new variable name
    query, retrieved_images = search_image(image_url, model, index, image_data, top_k=5)  # Pass the renamed variable
    if retrieved_images:
        return jsonify({"retrieved_images": retrieved_images})
    else:
        return jsonify({"error": "No similar images found"}), 404

def process_chunk(chunk):
    images = []
    for data in chunk:
        try:
            # Fetch the image from the URL
            response = requests.get(data['image'])
            image = Image.open(BytesIO(response.content))
            images.append(image)
        except Exception as e:
            print(f"Could not load image ID {data['id']}: {e}")
    
    # Encode the images using the SentenceTransformer model
    if images:
        chunk_embeddings = model.encode(images)
        return chunk_embeddings
    else:
        return []

def search_image(query, model, index, image_data, top_k=5):
    # Check if the query is a URL and load the image if it is
    if query.startswith("http://") or query.startswith("https://"):
        response = requests.get(query)
        
        # Check if the request was successful
        if response.status_code == 200:
            try:
                query = Image.open(BytesIO(response.content)).convert("RGB")  # Ensure it's in RGB mode
            except Exception as e:
                print(f"Error loading image from URL: {e}")
                return None, []
        else:
            print(f"Failed to retrieve image, status code: {response.status_code}")
            return None, []
    else:
        print("Query is not a valid URL.")
        return None, []

    # Encode the query image to get its embedding
    query_embedding = model.encode([query])
    query_embedding = query_embedding.astype('float32').reshape(1, -1)

    # Search the index for the top_k most similar images
    distances, indices = index.search(query_embedding, top_k)

    # Retrieve the corresponding images from image_data
    # retrieved_images = [image_data[i] for i in indices[0] if i < len(image_data)]
    retrieved_images = [image_data[i]["id"] for i in indices[0] if i < len(image_data)]

    return query, retrieved_images

# =========================Content Image Base===========================

# =========================RECOMMENDATION SYSTEM===========================

# Hàm kết nối đến cơ sở dữ liệu
def connect_db(user, password, db, host, port):
    return pymysql.connect(user=user,
                           password=password,
                           database=db,
                           host=host,
                           port=port,  # Thêm tham số port
                           cursorclass=pymysql.cursors.DictCursor)

# SELECT DISTINCT o.customer_id as iduser, oi.product_id as idproduct, 0 as rating
# FROM orders o
# JOIN order_item oi ON o.id = oi.order_id
# WHERE o.customer_id IS NOT NULL;

# SELECT rating as rating FROM `product_review` WHERE id_customer = ? and product_id = ?

def closeConnect(conn_purchase, cursor_purchase, conn_review, cursor_review):
    conn_purchase.close()
    cursor_purchase.close()
    conn_review.close()
    cursor_review.close()

def getDataRecommen():
    conn_purchase = connect_db('root', 'Abc@123456789', 'purchasedb', '127.0.0.1', 3306)
    cursor_purchase = conn_purchase.cursor()
    query_purchase = "SELECT DISTINCT o.customer_id AS iduser, oi.product_id AS idproduct, 0 AS rating FROM orders o JOIN order_item oi ON o.id = oi.order_id WHERE o.customer_id IS NOT NULL"
    cursor_purchase.execute(query_purchase)
    order_data = cursor_purchase.fetchall()
    # Create DataFrame
    df_orders = pd.DataFrame(order_data, columns=['iduser', 'idproduct', 'rating'])
    # Convert DataFrame to a list of lists
    array_result = df_orders.values.tolist()
    # Convert DataFrame to JSON
    
    conn_product = connect_db('root', 'Abc@123456789', 'productdb', '127.0.0.1', 3306)
    cursor_product = conn_product.cursor()
    # Loop through each order to update the rating
    for row in array_result:
        customer_id = row[0]
        product_id = row[1]
        # First query: check if a customer rating exists for the product in product_review table
        query_review = "SELECT rating FROM product_review WHERE id_customer = %s AND product_id = %s"
        cursor_product.execute(query_review, (customer_id, product_id))
        review_result = cursor_product.fetchone()
        if review_result:
            # If a rating exists, update the rating in the array
            row[2] = review_result[0]  # If review_result is a tuple
            # row[2] = review_result['rating']  # If review_result is a dictionary
        else:
            # If no review exists, check the product rating in the product table
            query_product = "SELECT rating FROM product WHERE id = %s"
            cursor_product.execute(query_product, (product_id,))
            product_result = cursor_product.fetchone()
            
            # Safely check if product_result is not None and access the rating based on type (tuple or dictionary)
            if product_result is not None:
                if isinstance(product_result, tuple):  # If product_result is a tuple
                    rating = product_result[0]
                elif isinstance(product_result, dict):  # If product_result is a dictionary
                    rating = product_result.get('rating', 0)  # Default to 0 if 'rating' key not found
                
                # Set the rating if valid, otherwise default to 4
                row[2] = rating if rating > 0 else 4
            else:
                # If no product rating exists, set the default rating to 4
                row[2] = 4
    closeConnect(conn_purchase, cursor_purchase, conn_product, cursor_product)
    return array_result

@app.route('/api/v1/aggreations/<int:user_id>', methods=['GET'])
def recommend_for_user(user_id):

    # Get the data for recommendations and fit the CF model
    recommendation_data = getDataRecommen()
    
    if recommendation_data:
        # Convert the recommendation data to a suitable format (like a matrix)
        data_matrix = np.array([
    [0, 0, 5],
    [0, 1, 4],
    [0, 3, 2],
    [0, 4, 2],
    [1, 0, 5],
    [1, 2, 4],
    [1, 3, 2],
    [1, 4, 0],
    [2, 0, 2],
    [2, 2, 1],
    [2, 3, 3],
    [2, 4, 4],
    [3, 0, 0],
    [3, 1, 0],
    [3, 3, 4],
    [4, 0, 1],
    [4, 3, 4],
    [5, 1, 2],
    [5, 2, 1],
    [6, 2, 1],
    [6, 3, 4],
    [6, 4, 5],
     [7, 0, 2],  # User 7 rates Item 0
    [7, 1, 3],  # User 7 rates Item 1
    [7, 2, 1],  # User 7 rates Item 2
    [8, 0, 4],  # User 8 rates Item 0
    [8, 1, 2],  # User 8 rates Item 1
    [8, 2, 3],  # User 8 rates Item 2
    [9, 0, 5],  # User 9 rates Item 0
    [9, 1, 4],  # User 9 rates Item 1
    [9, 2, 2]   # User 9 rates Item 2
])

        # Initialize the CF model with k neighbors
        cf = CF(data_matrix=data_matrix, k=3, uuCF=1)

        # Fit the CF model
        cf.fit()

        # Get recommendations for the specified user
        recommended_items = cf.recommend(user_id)  # Assuming you have a recommend method

        # Ensure that recommended_items is a list
        if isinstance(recommended_items, list):
            return jsonify(recommended_items), 200  # Return the list directly
        else:
            return jsonify([]), 404  # Return an empty list if no recommendations are available
    else:
        return jsonify([]), 500  # Return an empty list if there's an error
# =========================RECOMMENDATION SYSTEM===========================


if __name__ == '__main__':
    app.run(debug=True, port=rest_port) 