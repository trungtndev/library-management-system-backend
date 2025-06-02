# Database

## Main Entities

- **User**
  - id (UUID, PK)
  - username, password, fullName, dateOfBirth, email, createdAt
  - roles: Many-to-Many with Role

- **Role**
  - name (PK)
  - description
  - permissions: Many-to-Many with Permission

- **Permission**
  - name (PK)
  - description

- **Book**
  - id (UUID, PK)
  - title, author, description, quantity, createdAt, imageUrl
  - fileStorage: One-to-One with FileStorage
  - genres: Many-to-Many with Genre

- **Genre**
  - name (PK)
  - description

- **Loan**
  - id (UUID, PK)
  - user: Many-to-One with User
  - book: Many-to-One with Book
  - loanDate, dueDate, returnDate, createdAt, status (enum)

- **Review**
  - id (UUID, PK)
  - book: Many-to-One with Book
  - user: Many-to-One with User
  - rating, comment, createdAt

- **Payment**
  - id (UUID, PK)
  - user: Many-to-One with User
  - loan: Many-to-One with Loan
  - amount, status (enum), paymentDate, note

- **FileStorage**
  - id (UUID, PK)
  - originalFileName, storedFileName, contentType, filePath, url, size

- **InvalidatedToken**
  - id (token id, PK)
  - expiryTime

# API for This App

## Authentication

- `POST /auth/login`
  - Request (JSON):
    ```json
    {
      "username": "string",
      "password": "string"
    }
    ```
  - Response: JWT access & refresh tokens
  - No authentication required

- `POST /auth/introspect`
  - Request (JSON):
    ```json
    {
      "token": "string"
    }
    ```
  - Response: token validity info
  - No authentication required

- `POST /auth/logout`
  - Request (JSON):
    ```json
    {
      "refreshToken": "string"
    }
    ```
  - Response: success/failure
  - Requires JWT

- `POST /auth/refresh`
  - Request (JSON):
    ```json
    {
      "refreshToken": "string"
    }
    ```
  - Response: new access token
  - No authentication required

## User

- `POST /users`
  - Request (JSON):
    ```json
    {
      "username": "string",
      "password": "string",
      "fullName": "string",
      "dateOfBirth": "yyyy-MM-dd",
      "email": "string"
    }
    ```
  - Response: user detail
  - No authentication required

- `GET /users`
  - List users (pagination, sorting)
  - Requires JWT

## Book

- `POST /books`
  - Request: multipart/form-data
    - request: JSON string (BookCreationRequest)
      ```json
      {
        "title": "string",
        "author": "string",
        "description": "string",
        "quantity": number,
        "genreNames": ["string", ...]
      }
      ```
    - image: file
  - Response: book detail
  - Requires JWT

- `GET /books`
  - List books (pagination, search, sorting)
  - Public

## Loan

- `POST /books/{bookId}/loans`
  - Request (JSON):
    ```json
    {
      "dueDate": "yyyy-MM-dd"
    }
    ```
  - Response: loan detail
  - Requires JWT

- `GET /loans`
  - List all loans (pagination, sorting)
  - Requires JWT

## Review

- `POST /books/{bookId}/reviews`
  - Request (JSON):
    ```json
    {
      "rating": number,
      "comment": "string"
    }
    ```
  - Response: review detail
  - Requires JWT

- `GET /books/{bookId}/reviews`
  - List reviews for a book (pagination, sorting)
  - Public

## Genre

- `POST /genre`
  - Request (JSON):
    ```json
    {
      "name": "string",
      "description": "string"
    }
    ```
  - Response: genre detail
  - Requires JWT

- `GET /genre`
  - List all genres
  - Public

- `GET /genre/{genreId}`
  - Get genre by id
  - Public

## Notes

- All endpoints returning sensitive or user-specific data require JWT authentication.
- JWT is passed via the Authorization header as a Bearer token.
- Standard response format: wrapped in `ApiResponse` with code, success, and result fields.

