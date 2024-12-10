# Library Management System

This is a simple **Library Management System** built with **Spring Boot** and **MySQL**. It allows users to manage books, patrons, and borrowing records, offering a complete CRUD functionality for managing books, patrons, and borrowing operations.

## Features

- **User Authentication**: Allows users to register, login, and access the system via JWT (JSON Web Token).
- **Book Management**: Add, update, delete, and list books.
- **Patron Management**: Add, update, delete, and list patrons.
- **Book Borrowing & Returning**: Patrons can borrow and return books.
- **Database Management**: Built with MySQL, supporting relational data.

## Prerequisites

Make sure you have the following software installed on your machine:

- **Java** (version 21)
- **Maven** (for building the project)
- **MySQL** (or another relational database, but MySQL is recommended)
- **Git** (for cloning the repository)

## Getting Started

### 1. Clone the Repository

Clone the repository to your local machine using Git:

```bash
git clone https://github.com/FadiAlayyas/Library-Management.git
cd library-management-system
```

### 2. Configure Database Connection

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
### 3. Build the Project
```bash
mvn clean install
```
### 4. Run the Application
```bash
mvn spring-boot:run
```
This will start the application on port 8080 by default. You can access the API at:
```bash
http://localhost:8080
```
### 5. API Endpoints Postman Collections Link
```bash
https://documenter.getpostman.com/view/17854195/2sAYHwHPP2
```
### 6. Running Tests
```bash
mvn test
```