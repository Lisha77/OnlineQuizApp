# Online Quiz Application (3-Tier Architecture)

A dynamic, 3-tier web application built with Java Servlets, Jakarta EE, and MySQL. This project demonstrates core server-side web development concepts, including session management, role-based access control (RBAC), password hashing, and database persistence, all wrapped in a custom "Dark-Tech/Mainframe" UI theme.

## 🚀 Features

* **Secure Authentication:** User registration and login with SHA-256 password hashing.
* **Role-Based Access Control (RBAC):** `HttpSession` tracking to route 'students' to the quiz and 'admins' to a secure dashboard.
* **Dynamic Evaluation:** A 10-question Java programming quiz that grades submissions instantly using server-side logic.
* **Database Persistence:** All user accounts and quiz attempts (scores, percentages, timestamps) are permanently stored via JDBC.
* **Admin Dashboard:** A protected endpoint where administrators can view all student scores fetched directly from the database.
* **Responsive UI:** Custom CSS mainframe/terminal aesthetic with hover effects and conditional styling based on performance.

## 🛠️ Technology Stack

* **Frontend:** HTML5, CSS3
* **Backend:** Java Servlets (Jakarta EE / Java EE)
* **Database:** MySQL Server 8.x, JDBC (`mysql-connector-j`)
* **Server:** Apache Tomcat 9.0 / 11.x
* **IDE:** Eclipse IDE for Enterprise Java Developers

## 📋 Prerequisites

Before running this project, ensure you have the following installed:
* Java Development Kit (JDK 8 or higher)
* Apache Tomcat v9.0+ or v11.0+
* MySQL Server & MySQL Workbench
* [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (.jar file)

## ⚙️ Setup and Installation

### 1. Database Configuration
1. Open MySQL Workbench.
2. Execute the provided `database_setup.sql` script to create the `quiz_db` database, `users` table, and `quiz_results` table.
3. The script automatically inserts a default admin account.

### 2. Project Import (Eclipse)
1. Clone this repository: `git clone https://github.com/YOUR_USERNAME/OnlineQuizApp.git`
2. Open Eclipse and navigate to **File > Import > Existing Dynamic Web Project**.
3. Select the cloned repository folder.
4. Add the `mysql-connector-j-x.x.x.jar` file into the `src/main/webapp/WEB-INF/lib/` directory.

### 3. Database Credentials Update
Open the following Java files located in `src/main/java/com/quizapp/`:
* `RegisterServlet.java`
* `LoginServlet.java`
* `QuizServlet.java`
* `AdminDashboardServlet.java`

Locate the `DriverManager.getConnection(...)` line in each file and update `"password"` to match your local MySQL root password.

### 4. Deployment
1. Right-click the project in Eclipse > **Run As > Run on Server**.
2. Select your configured Apache Tomcat server.
3. Navigate to `http://localhost:8080/OnlineQuizApp/login.html` in your web browser.

## 🔐 Default Credentials

* **Admin Portal:**
  * **Username:** `admin`
  * **Password:** `admin123`
* **Student Access:** Click "Register New Access" on the login page to create a custom student account, or log in with an account you have created.

## 📝 License

This project was created for academic purposes as part of the Advanced Internet Programming (AIP) coursework.
