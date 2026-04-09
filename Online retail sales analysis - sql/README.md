Online Retail Sales Analysis Database


Project Overview


This project simulates an online retail store database to analyze sales performance. Retail businesses generate large amounts of sales data but often lack structured insights. The database and SQL queries created in this project help identify top-selling products, high-value customers, and other key business metrics to support data-driven decision-making.


Database Schema


1. Customers
Column Name	Data Type	Description
customer_id	INT (PK)	Unique ID for each customer
name	VARCHAR 100	Customer's name
city	VARCHAR 50	Customer's city


2. Products
Column Name	Data Type	Description
product_id	INT (PK)	Unique ID for each product
name	VARCHAR 100	Product name
category	VARCHAR 50	Product category
price	DECIMAL(10,2)	Product price


3. Orders
Column Name	Data Type	Description
order_id	INT (PK)	Unique order ID
customer_id	INT (FK)	References Customers(customer_id)
order_date	DATE	Date of the order


4. Order_Items
Column Name	Data Type	Description
order_id	INT (FK)	References Orders(order_id)
product_id	INT (FK)	References Products(product_id)
quantity	INT	Quantity of the product ordered



Sample Data


The database includes sample customers, products, orders, and order items to demonstrate query outputs.
Data is structured to reflect realistic retail transactions and supports analysis of multiple business questions.



Key Analytical Queries

Top-Selling Products
Lists products ranked by total quantity sold.
Most Valuable Customers
Identifies customers with the highest total spending.
Monthly Revenue Calculation
Computes total revenue grouped by month and year.
Category-Wise Sales Analysis
Provides total revenue per product category.
Inactive Customers
Detects customers with no orders in the last two months.



How to Use

Execute the provided .sql script in a MySQL environment.



The script will:

Create the database and tables.
Insert sample data.
Include queries for all key tasks.
Run the queries to generate analytical insights.
Business Insights
Helps identify products that drive revenue and those underperforming.
Shows which customers contribute most to revenue.
Allows detection of inactive customers for targeted marketing.
Supports category-wise performance analysis for inventory and sales planning.



Tools Used

MySQL 8.x (or compatible versions)
SQL for database creation, data insertion, and analysis