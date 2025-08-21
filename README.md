Online Exam System – Java, Swing, MySQL

A full-featured Java-based Online Examination System developed using Java Swing for the GUI and MySQL as the backend. The platform supports secure user login/registration, real-time examination with score tracking, and an administrative dashboard to manage questions and view user results.

*Features:

1.User Side

-Secure login and registration with MySQL authentication
-Dynamic question loading from the database
-Real-time countdown timer for exams (configurable)
-Answer selection, navigation between questions (Next/Previous)
-Automatic score calculation upon exam submission
-Past results viewing by users

2.Admin Dashboard

-Admin login to access management features
-Add, edit, and delete questions with real-time DB updates
-View all results from all users with username, score, and timestamp
-Refresh option to instantly sync questions from the database

*Tech Stack:
GUI	- Java Swing
Backend	Java(OOP)
Database - MySQL
DB Access -JDBC


*Project Structure:
Online_exam/
├── LoginPage.java
├── ExamPage.java
├── AdminDashboard.java
├── ViewResultsPage.java
├── UserService.java
├── QuestionBank.java
├── DBConnection.java
└── model/
    ├── Question.java
    └── User.java


*How to Run:
Install Java and MySQL on your system.

Create a MySQL database and tables as per the schema below.

Update your database connection settings in DBConnection.java.

Import the project into your IDE (IntelliJ/Eclipse).