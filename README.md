# Hướng dẫn chạy project

## 1. Yêu cầu hệ thống
- Java 21
- Maven 3.8+
- Docker (để chạy MySQL)

## 2. Khởi tạo database MySQL bằng Docker

```bash
docker pull mysql:8.0.41-debian
```

```bash
docker run  --name mysql_db \
            -p 3306:3306 \
            -e MYSQL_ROOT_PASSWORD=root \
            -d mysql:8.0.41-debian
```

## 3. Cấu hình kết nối database
- Thông tin kết nối đã được cấu hình sẵn trong `src/main/resources/application.yaml`:
  - url: jdbc:mysql://localhost:3306/library_test
  - username: root
  - password: root

## 4. Build và chạy ứng dụng

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Ứng dụng sẽ chạy tại: http://localhost:8080/library

## 5. Tài liệu API
- Xem chi tiết trong file `system.md`

