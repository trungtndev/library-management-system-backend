
## 1. Kiến trúc Tổng quan

## 2. Kiến trúc Chi tiết

### 2.1. Frontend

### 2.2. Backend với Spring Boot
| HTTP Method | Endpoint                 | Chức năng                          | Quyền yêu cầu          |
| ----------- | ------------------------ | ---------------------------------- | ---------------------- |
| `POST`      | `/api/loans`             | Mượn sách (tạo đơn mượn mới)       | USER, LIBRARIAN        |
| `PUT`       | `/api/loans/{id}/return` | Trả sách                           | USER, LIBRARIAN        |
| `GET`       | `/api/loans/{id}`        | Xem chi tiết đơn mượn              | USER, LIBRARIAN, ADMIN |
| `GET`       | `/api/loans/user/{id}`   | Lịch sử mượn của user cụ thể       | USER (self), LIBRARIAN |
| `GET`       | `/api/loans`             | Lấy danh sách tất cả đơn mượn      | LIBRARIAN, ADMIN       |
| `DELETE`    | `/api/loans/{id}`        | Xóa đơn mượn (nếu bị lỗi hoặc huỷ) | LIBRARIAN, ADMIN       |

### 2.3. Cơ sở Dữ liệu MySQL

#### 2.3.1. Thiết kế Schema (Một số bảng chính)
- **Bảng `book`:**
    - `id` (Primary Key)
    - `title`
    - `author`
    - `description`
    - `quantity`
    - `created_at`

- **Bảng `user`:**
    - `id` (Primary Key)
    - `username`
    - `password` (mã hóa)
    - `full_name`
    - `email`
    - `phone`
    - `created_at`
    - `date_of_birth`

- **Bảng `loans`:**
    - `id` (Primary Key)
    - `user_id`
    - `book_id` 
    - `loan_date`
    - `due_date`
    - `return_date`
    - `status` 

- **Bảng `payment`:**
    - `id` (Primary Key)
    - `user_id`
    - `loan_id` 
    - `amount`
    - `payment_date`
    - `method`
    - `status` 

- **Bảng `review`:**
    - `id` (Primary Key)
    - `user_id`
    - `book_id` 
    - `rating`
    - `comment`
    - `created_at`
