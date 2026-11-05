
# Blog Manager – Backend API

REST API quản lý người dùng và xác thực tài khoản,
được xây dựng bằng Java và Spring Boot.

## 1. Công nghệ sử dụng

- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA / Hibernate
- MySQL
- Maven
- JWT Authentication
- BCrypt Password Encoding
- Bean Validation

## 2. Chức năng

### Authentication
- Đăng ký tài khoản
- Đăng nhập bằng username/password
- Mã hóa mật khẩu BCrypt
- Cấp JWT Access Token
- Xác thực Bearer Token
- Phân quyền USER và ADMIN

### User Management
- ADMIN xem danh sách người dùng
- ADMIN xem người dùng theo ID
- ADMIN cập nhật thông tin User
- ADMIN xóa người dùng

### Exception Handling
- 400 Bad Request
- 401 Unauthorized
- 403 Forbidden
- 404 Not Found
- 409 Conflict

## 3. Cấu hình môi trường

Yêu cầu:
- JDK 21
- Maven hoặc Maven Wrapper
- MySQL đang hoạt động

Tạo database:

```sql
CREATE DATABASE IF NOT EXISTS blog_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Cấu hình biến môi trường:

- DB_PASSWORD: Mật khẩu MySQL
- JWT_SECRET: Khóa Base64 tối thiểu 32 byte

Tạo JWT_SECRET bằng lệnh:

```bash
openssl rand -base64 32
```

## 4. Chạy ứng dụng

Trên macOS/Linux:

```bash
./mvnw spring-boot:run
```

Đảm bảo biến môi trường đã được cấu hình
trước khi chạy.

Backend mặc định chạy tại:

http://localhost:8080

## 5. API Endpoints

| Method | Endpoint | Quyền |
|--------|----------|-------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| GET | /api/auth/me | Authenticated |
| GET | /api/users | ADMIN |
| GET | /api/users/{id} | ADMIN |
| PUT | /api/users/{id} | ADMIN |
| DELETE | /api/users/{id} | ADMIN |

## 6. Ví dụ Register

POST /api/auth/register

```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "ExamplePassword123!",
  "fullName": "Test User"
}
```

## 7. Ví dụ Login

POST /api/auth/login

```json
{
  "username": "testuser",
  "password": "ExamplePassword123!"
}
```

Đăng nhập thành công trả về JWT Access Token.

Gửi token trong các request cần xác thực:

Authorization: Bearer <accessToken>

## 8. Cấu trúc project

```text
src/main/java/com/example/blog/
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
├── security/
└── service/
```

## 9. Giai đoạn phát triển

- Tuần 1: User CRUD, Security, JWT
- Tuần 2: Post CRUD, Search, Pagination, Like, Upload
- Tuần 3: Redis, Kafka, Rate Limiting
- Tuần 4: Git và teamwork

## 10. Lưu ý bảo mật

Project phục vụ mục đích học tập.

Không commit:
- Mật khẩu database
- JWT secret
- Access Token
- Thông tin tài khoản thật

JWT logout/revocation và một số biện pháp
bảo mật production sẽ được phát triển sau.
