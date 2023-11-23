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