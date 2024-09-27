# server-ecommerce-microservice-deeplearning
cài docker để run và chạy câu lệnh bên dưới

1. chạy:
docker pull nguyentanthanh0709/mysql:productdb
docker pull nguyentanthanh0709/mysql:purchasedb
docker pull nguyentanthanh0709/mysql:userdb
docker pull nguyentanthanh0709/mysql:cartdb
2. chạy tiếp:
docker-compose -f docker-compose.yml up -d --build
=> sau bước trên thì đã chạy cơ sở dữ liệu thành công
3. vào thư mục kafka mở cmd và chạy:
cd Kafka
docker-compose -f docker-compose.yml up -d --build

mở intel or eclipse chạy từng service riêng lẻ
chạy ứng dụng: NodeJS Notification-Service: npm start
chạy ứng dụng: Flask PythonAPI: python app.py


sau khi chạy xong vào http://localhost:8761
để kiểm tra discovery-registry
xem các service trong hệ thống microservice đã chạy hết chưa
gồm:
API-GATEWAY
USER-SERVICE
PRODUCT-SERIVCE
CART-SERVICE
PURCHASE-SERVICE
NOTIFICATION-SERVICE

# Deep learning
 chưa implement

tiến hành xây dựng hệ thống khuyến nghị sản phẩm
và tìm kiếm sản phẩm dựa trên hình ảnh CNN