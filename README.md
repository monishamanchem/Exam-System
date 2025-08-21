# Online Exam System – Java, Swing, MySQL

A full-featured Java-based Online Examination System developed using Java Swing for the GUI and MySQL as the backend.  
The platform supports secure user login/registration, real-time examination with score tracking, and an administrative dashboard to manage questions and view user results.

---

## Features

### 1. User Side
- Secure login and registration with MySQL authentication  
- Dynamic question loading from the database  
- Real-time countdown timer for exams (configurable)  
- Answer selection and navigation between questions (Next/Previous)  
- Automatic score calculation upon exam submission  
- Past results viewing by users  

### 2. Admin Dashboard
- Admin login to access management features  
- Add, edit, and delete questions with real-time database updates  
- View all results from users with username, score, and timestamp  
- Refresh option to instantly sync question updates from the database  

---

## Tech Stack

| Layer       | Technology      |
|-------------|-----------------|
| GUI         | Java Swing      |
| Backend     | Java (OOP)      |
| Database    | MySQL           |
| DB Access   | JDBC            |

---

## Project Structure


Online_exam/
├── LoginPage.java
├── ExamPage.java
├── AdminDashboard.java
├── ViewResultsPage.java
├── UserService.java
├── QuestionBank.java
├── DBConnection.java
├── model/
│ ├── Question.java
│ └── User.java


---

## How to Run

1. Install Java and MySQL on your system.
2. Create the required MySQL database and tables (schema below).
3. Update your DB credentials in `DBConnection.java`.
4. Import the project into IntelliJ/Eclipse.
5. Run `LoginPage.java` to start the application.

---

