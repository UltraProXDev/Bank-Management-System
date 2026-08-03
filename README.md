# 🏦 Bank Management System

A complete **Bank Management System** developed using **Java**, **MySQL**, and **JDBC**. This project is designed to simulate real-world banking operations with database connectivity, user authentication, account management, transactions, and admin control.

The application uses **JDBC (Java Database Connectivity)** to connect Java files with a MySQL database and perform database operations such as inserting, updating, deleting, and retrieving data.

---

## 🚀 Project Overview

The Bank Management System provides two types of users:

### 👤 Customer
Customers can:
- Register themselves
- Login into their account
- Create a bank account
- Deposit money
- Withdraw money
- Perform transactions
- View transaction history
- Manage their banking information

### 👨‍💼 Admin
Admins can:
- Login to the admin panel
- Manage customers
- Manage accounts
- View transactions
- Manage loan records
- Monitor banking activities

---

## 🔄 Project Flow

Main.java controls the complete application flow.

Register Module:
- User registration details are stored in the `customers` table.

Create Account Module:
- Bank account details are stored in the `accounts` table.

Login Module:
- Customer login connects with `Customer.java`.
- Admin login connects with `Admin.java`.

Transaction Module:
- All banking transactions are stored in the `transactions` table.

---

## 🗄️ Database Used

Database Name:

```
BANK_DB
```

Tables:

- admins
- customers
- accounts
- transactions
- loans

---

## 🛠️ Technologies Used

- Java
- MySQL
- JDBC (Java Database Connectivity)
- SQL
- Object-Oriented Programming (OOP)

---

## ✨ Features

### Customer Features
- Customer Registration
- Secure Login System
- Account Creation
- Deposit Money
- Withdraw Money
- Balance Checking
- Transaction History

### Admin Features
- Admin Login
- Customer Management
- Account Management
- Transaction Monitoring
- Loan Management

---

## 🔌 JDBC Connectivity

The project connects Java with MySQL using JDBC.

Database connection flow:

Java Application → JDBC Driver → MySQL Database → BANK_DB

JDBC is used to execute SQL queries and perform database operations.

---

## 📚 Concepts Implemented

- Object-Oriented Programming
- Classes and Objects
- Encapsulation
- JDBC Connectivity
- SQL Queries
- CRUD Operations
- Exception Handling
- Database Management
- Authentication System

---

## ⚙️ Setup Instructions

### 1. Install Requirements

Install:

- Java JDK
- MySQL Server
- MySQL Connector/J JDBC Driver

---

### 2. Create Database

Create a database in MySQL:

```sql
CREATE DATABASE BANK_DB;
```

Import the required tables:
- admins
- customers
- accounts
- transactions
- loans

---

### 3. Configure Database Connection

Update your JDBC connection details:

```java
String url = "jdbc:mysql://localhost:3306/your_database";
String username = "root";
String password = "your_password";
```

---

### 4. Run Project

Compile:

```bash
 javac -cp ".;lib\mysql-connector-j-9.7.0.jar" src\*.java
```

Run:

```bash
 java -cp ".;lib\mysql-connector-j-9.7.0.jar" src.Main
```

---

## 🎯 Learning Objectives

This project helps in understanding:

- Java backend development
- Database connectivity
- Real-world banking workflows
- MySQL database management
- Secure user authentication
- Building CRUD-based applications

---

## 🔮 Future Improvements

- Java Swing / JavaFX GUI
- Password encryption
- OTP authentication
- Online banking interface
- Email notifications
- Advanced loan management
- Spring Boot migration

---

## 👨‍💻 Author

**Jidnyesh Zambre**

GitHub:
https://github.com/UltraProXDev

---

⭐ If you like this project, consider giving it a star!
