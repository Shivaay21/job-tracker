# 🚀 Job Application Tracker API

A secure and scalable backend application built using **Spring Boot** that allows users to track job applications efficiently with JWT-based authentication and role-based access control.

---

## 📌 Project Overview

The **Job Application Tracker API** is designed to help users manage and monitor their job applications with features like:

- Secure Authentication (JWT)
- Role-Based Authorization (USER / ADMIN)
- Company Management
- Job Application Tracking
- Status-based Filtering
- Date Range Filtering
- Application Statistics
- Pagination & Sorting
- Search Functionality
- Swagger API Documentation

This project follows a clean **layered architecture**:

Controller → Service → Repository → Entity

---

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA (Hibernate)
- MySQL
- ModelMapper
- Lombok
- Swagger (OpenAPI 3)
- Maven

---

## 🔐 Authentication & Security

This application uses **JWT-based Stateless Authentication**.

### Flow:

1. User registers
2. User logs in
3. Server generates JWT token
4. Client sends token in Authorization header:


Authorization: Bearer <your_token>


5. JWT Filter validates token
6. User is authenticated via SecurityContext

### Role-Based Access:

- USER → Access only their own job applications
- ADMIN → Access all job applications

---

## 📂 Core Features

### 👤 User Module
- Register User
- Login User (JWT Token Generation)

### 🏢 Company Module
- Create Company
- Update Company
- Delete Company
- Get All Companies (Pagination + Sorting)
- Search Companies by Name

### 💼 Job Application Module
- Create Job Application
- Update Job Application
- Delete Job Application
- Filter by Status
- Filter by Date Range
- Count by Status
- Get Application Statistics

---

## 📊 Application Statistics

The API provides analytics including:

- Total Applications
- Total Rejected
- Total Interviews
- Total Selected
- Success Rate %

---

## 📖 API Documentation (Swagger)

Swagger UI available at:


http://localhost:8080/swagger-ui/index.html


Use the **Authorize** button to enter your JWT token.

---

## ⚙️ How to Run the Project

### 1️⃣ Clone Repository


git clone https://github.com/Shivaay21/job-tracker.git


### 2️⃣ Configure Database

Update your `application.yml` or `application.properties`:


spring.datasource.url=jdbc:mysql://localhost:3306/jobtracker
spring.datasource.username=root
spring.datasource.password=your_password


### 3️⃣ Run Application


mvn spring-boot:run


Or run from IDE.

---

## 📌 Database Schema Highlights

- Proper entity relationships (User ↔ JobApplication ↔ Company)
- Enum-based Application Status

---

## 🏗 Architecture Design

- DTO Layer (Request / Response separation)
- Global Exception Handling
- Service Layer Business Logic
- Repository Abstraction
- Clean Code Practices

---

## 🔎 Pagination & Sorting Example


GET /api/companies?page=0&size=5&sortBy=name&sortDir=asc


---

## 🧪 Sample API Flow

1. Register User
2. Login → Get JWT Token
3. Authorize in Swagger
4. Create Company
5. Create Job Application
6. View Statistics

---

## 🚀 Future Improvements (Planned)

- Refresh Token Implementation
- Docker Containerization
- Deployment on Cloud (AWS / Render / Railway)

---

## 👨‍💻 Author

Shivam Kumar  
Backend Developer | Java & Spring Boot Enthusiast  

GitHub: https://github.com/Shivaay21  
LinkedIn: https://www.linkedin.com/in/shivam-kumar-74ab76227/

---

## ⭐ Why This Project?

This project demonstrates:

- Secure backend development
- Clean architecture principles
- Production-level API structure
- Real-world problem-solving implementation

---
