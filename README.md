Write a Java program that connects to the database and performs the following tasks:
Inserts data into one table
Updates data in another table
Deletes data from one table
Uses transactions to ensure that all three tasks are completed successfully or rolled back if an error occurs
Uses try-catch blocks to handle SQLExceptions and print error messages
Table 1: Courses
Columns:ID (Primary Key)CourseName CourseDescription
Table 2: Themes
Columns:ID (Primary Key)ThemeName CourseID (Foreign Key referencing the ID column in the Courses table)
This schema represents a one-to-many relationship between Courses and Themes,

Faruk task
//Exercise 1:
//Create a Java program using JDBC that performs multiple money transfers between multiple bank accounts within a single transaction.
//Implement a rollback mechanism to ensure data consistency in case of an error during any part of the transaction.
//
//To set up the database with tables and initial data, you can use the following SQL statements as an example:
//
//-- Create a bank_accounts table
//CREATE TABLE bank_accounts (
//    account_id INT AUTO_INCREMENT PRIMARY KEY,
//    balance DECIMAL(10, 2)
//);
//
//-- Insert initial account data
//INSERT INTO bank_accounts (account_id, balance) VALUES
//    (0, 1000.00),
//    (0, 1500.00),
//    (0, 800.00),
//    (0, 2000.00);