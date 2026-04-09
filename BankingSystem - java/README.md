# 🏦 Banking System (Java Swing + MySQL)

---

## 📌 Project Overview
This is a simple **Banking Management System** developed using **Java Swing (GUI)** and **MySQL database**.

It allows users to:
- Login securely
- Deposit money
- Withdraw money
- View real-time balance
- Store transaction history

---

## 🚀 Features

✔ User Login System  
✔ Deposit Money Functionality  
✔ Withdraw Money Functionality  
✔ Balance Display (Real-Time Update)  
✔ Transaction Logging  
✔ MySQL Database Integration  
✔ Desktop GUI using Java Swing  

---

## 🛠️ Technologies Used

- Java (JDK 17+ / JDK 26)
- Java Swing (GUI)
- MySQL Database
- JDBC (Java Database Connectivity)

---

## 🗄️ Database Setup

### 📍 Database Name

banking_app


---

### 📊 Table: USERS

```sql
CREATE TABLE USERS (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USERNAME VARCHAR(50) UNIQUE,
    PASSWORD VARCHAR(50),
    BALANCE DOUBLE DEFAULT 0
);



📊 Table: TRANSACTIONS
CREATE TABLE TRANSACTIONS (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USERNAME VARCHAR(50),
    DESCRIPTION VARCHAR(255),
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
🔑 Insert Default User
INSERT INTO USERS (USERNAME, PASSWORD, BALANCE)
VALUES ('admin', 'admin123', 0);



🔐 Default Login Credentials
Username: admin
Password: admin123


▶️ How to Run the Project


1. Clone the Repository
git clone <your-repo-link>


2. Import Project
Open in:

IntelliJ IDEA OR
Eclipse OR
VS Code


3. Configure MySQL Connection

Update in Java code:

String URL = "jdbc:mysql://localhost:3306/banking_app";
String USER = "root";
String PASS = "your_mysql_password";


4. Run the Application
Run:

BankingSystem.java


🎮 How to Use
Open the application
Enter username & password
Click Login
Enter amount in textbox
Click:
Deposit → Add money
Withdraw → Remove money
View updated balance
Click Logout to return

⚠️ Important Notes
MySQL server must be running before executing
JDBC driver (MySQL Connector) must be added
Amount field must not be empty


📈 Future Improvements
🔐 Password encryption (BCrypt)
👤 User registration system
📊 Transaction history UI panel
🧾 Mini statement feature
🎨 Modern UI redesign (ATM style)
