# Netak RESTful API

RESTful API for managing posts and users with JWT-based authentication.

📖 In Russian: [перевод на русский](https://github.com/CkutlsGit/netak/blob/main/README.ru.md)

## 📋 Overview

Netak is a Spring Boot application that provides a simple platform for creating and viewing posts. The project implements secure authentication using JWT tokens with role-based access control.

## 🚀 Technology Stack

### Backend
- **Java 21** - Core language
- **Spring Boot 4** - Application framework
- **Spring Data JPA** - Database access and ORM
- **Spring Security** - Authentication and authorization
- **JWT** - Token-based authentication

### Database
- **PostgreSQL** - Production database
- **H2** - Testing database

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

## ✨ Features

- 🔐 **Authentication & Authorization**
  - User registration and login
  - JWT tokens (access/refresh)
  - Role-based access control (User/Admin)

- 👥 **User Management**
  - View users (Admin only)
  - Delete users (Admin only)

- 📝 **Post Management**
  - Create posts (Authenticated users)
  - View all posts (Authenticated users)
  - View user posts (Admin only)
  - Delete own posts (Authenticated users)
  - Delete any post (Admin only)

- ✅ **Data Validation**
  - Request validation
  - Custom error handling

## 🛠️ Quick Start

### Prerequisites
- Docker

### Installation & Running

1. **Clone the repository**
```bash
git clone https://github.com/CkutlsGit/netak
cd netak
```

2. **Build and run with Docker Compose**
```bash
docker compose up --build
```

3. **Access the API**
```
http://localhost:8080
```

## 📚 API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive tokens |
| POST | `/api/auth/refresh` | Refresh access token |

### Users (Admin Only)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/user/{id}` | Get user by ID |
| GET | `/api/user` | Get all users |
| DELETE | `/api/user/{id}` | Delete user by ID |

### Posts

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/post` | Get all posts (Authenticated) |
| GET | `/api/post/{id}` | Get post by ID (Admin only) |
| GET | `/api/post/user/{id}` | Get user's posts by user ID (Admin only) |
| POST | `/api/post/create` | Create a new post (Authenticated) |
| DELETE | `/api/post/delete/{id}` | Delete a post (Authenticated, own posts only) |

### Authentication Flow

1. **Register** a new user
2. Include `accessToken` in the `Authorization` header for protected endpoints:
   ```
   Authorization: Bearer <access_token>
   ```
3. Use `refreshToken` to get a new `accessToken` when it expires

## 📦 Project Structure

```
src/main/java/ru/netak/
├── config/              # Spring configuration
├── controller/          # REST API controllers
│   └── user/           # User and auth controllers
├── dto/                # Data Transfer Objects
│   ├── post/
│   └── user/
├── entity/             # JPA entities
│   └── enums/
├── exception/          # Custom exceptions and handlers
│   ├── controller/
│   └── model/
├── repository/         # JPA repositories
├── security/           # Security configuration
│   ├── dto/
│   ├── exception/
│   ├── jwt/
│   └── service/
└── service/            # Business logic layer
    └── user/
```

## 🔒 Security Features

- **JWT Authentication**: Stateless authentication with access and refresh tokens
- **Role-Based Access**: USER and ADMIN roles with different permissions
- **Password Encoding**: BCrypt for secure password storage
- **Protected Routes**: All endpoints except authentication are protected
- **Post Ownership**: Users can only delete their own posts (except Admins)

## 📝 Environment Variables

Create a `.env` file in the root directory:

```env
DB_USER=postgres
DB_PASSWORD=your_password
DB_NAME=netakapp
JWT_SECRET=your_jwt_secret_key
```

## 🔄 API Usage Examples

### Register a User
```bash
POST /api/auth/register
{
    "email": "user@example.com",
    "username": "johndoe",
    "password": "securePassword123"
}
```

### Login
```bash
POST /api/auth/login
{
    "email": "user@example.com",
    "password": "securePassword123"
}
```

### Create a Post
```bash
POST /api/post/create
Authorization: Bearer <access_token>
{
    "title": "My First Post",
    "description": "This is the content of my first post."
}
```

### Get All Posts
```bash
GET /api/post
Authorization: Bearer <access_token>
```

### Delete a Post
```bash
DELETE /api/post/delete/{id}
Authorization: Bearer <access_token>
```

---

Note: This project is currently at the MVP stage. Future improvements will include pagination, user profiles, comments, likes, post search, and additional features.

## 🚧 Roadmap

| Feature | Status | Description |
|---------|--------|-------------|
| **Pagination** | 🟡 Planned | Page-by-page post output for optimal loading |
| **User Profile** | 🟡 Planned | View and edit personal information, avatar |
| **Comments** | 🟡 Planned | Commenting on posts |
| **Likes** | 🟡 Planned | Rating posts and comments |
| **Search** | 🟡 Planned | Search posts by title and content |

---
