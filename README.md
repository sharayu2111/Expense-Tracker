# Personal Expense Tracker System

## Overview
A complete Java-based Personal Expense Tracker System that features both a robust **Swing Desktop GUI** and an integrated **Web Dashboard (JSP & Servlets)** running on an embedded Tomcat server.

Key highlights include Object-Oriented Programming (OOP) concepts, MySQL database integration, File handling, multithreading for responsiveness, exception handling, and visually appealing UI panels.

## Features
- **Smart GUI Dashboard**: Includes Summary Cards, JFreeChart pie chart breakdowns, and interactive JTables.
- **Web Dashboard**: An embedded Tomcat web server provides an HTML/Bootstrap frontend available at `http://localhost:8080/dashboard`.
- **Financial Tools**: Easily track Incomes, Expenses, View Balances, and get Budget Alerts.
- **Data Persistence**: Uses MySQL database for reliable data storage.

## Technologies Used
- **Core Java**: Java 11
- **GUI Engine**: Java Swing + FormDev FlatLaf (Modern Look and Feel)
- **Web App**: Servlets, JSP, Embedded Apache Tomcat
- **Database**: MySQL & JDBC
- **Charting**: JFreeChart
- **Build Tool**: Maven

## Object-Oriented Principles Demonstrated
- **Abstraction**: `Transaction` abstract class, `ITransactionDAO` interface.
- **Inheritance**: `Income` and `Expense` inherit from `Transaction`.
- **Encapsulation**: Fully encapsulated model properties with getters and setters.
- **Polymorphism**: Overridden `getTransactionType()` for different sub-types.
- **Multithreading**: SwingWorker isolates heavy database fetches from the UI. The Embedded Web Server runs concurrently on its own thread.

## Installation & Running

### Requirements
- Java Development Kit (JDK 11 or higher)
- Apache Maven

### Steps
1. Navigate to the project root directory where `pom.xml` is located.
2. Build the project using Maven:
   ```bash
   mvn clean package
   ```
3. Run the application (executes both GUI and Web Server):
   ```bash
   mvn exec:java -Dexec.mainClass="com.expensetracker.Main"
   ```
   Or execute the built Jar:
   ```bash
   java -jar target/PersonalExpenseTracker-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

### Accessing the App
- **Desktop**: A Login Window will appear (Credentials: `admin` / `admin`).
- **Web Dashboard**: Open your browser and go to `http://localhost:8080/dashboard`.


