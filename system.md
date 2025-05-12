
## 1. Kiến trúc Tổng quan

## 2. Kiến trúc Chi tiết

### 2.1. Frontend

### 2.2. Backend với Spring Boot

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
