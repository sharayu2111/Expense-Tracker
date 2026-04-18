# Project Documentation

## 1. System Architecture
The application uses a Hybrid Client-Server Architecture:
- **Presentation Layer**: Swing UI (Desktop), JSP (Web Views)
- **Controller/Service Layer**: Swing Event Listeners, Servlets (`DashboardServlet`)
- **Data Access Layer**: JDBC using SQLite (`TransactionDAOImpl`)
- **Database**: SQLite relational database storing normalized transactions.

## 2. Use Case Diagram
- **User Actions**: Provide Login Credentials, View Dashboard Summary, Add Income Transaction, Add Expense Transaction, Export to CSV, Auto-sync data to Web.
- **System Actions**: Calculate balance, Generate pie charts, Handle database SQL connection, Render JSP metrics.

## 3. Class Diagram Analysis
- **`com.expensetracker.model`**:
  - `Transaction` (abstract) ← `Income`, `Expense`
- **`com.expensetracker.dao`**:
  - `ITransactionDAO` (interface) ← `TransactionDAOImpl` (class)
  - `DBConnection` (Singleton class)
  - `SetupDB` (Utility class)
- **`com.expensetracker.gui`**:
  - `MainFrame` (JFrame)
  - `DashboardPanel` (JPanel, uses `SwingWorker`)
  - `LoginPanel` (JPanel)
- **`com.expensetracker.web`**:
  - `WebServer` (Spawns Embedded Tomcat)
  - `DashboardServlet` (Extends `HttpServlet`)

## 4. ER (Entity-Relationship) Diagram
**Entity: TRANSACTIONS**
- `id` (Primary Key, INT, Auto-increment)
- `type` (VARCHAR: 'INCOME' or 'EXPENSE')
- `amount` (DOUBLE/REAL)
- `category` (VARCHAR: 'Food', 'Salary', etc.)
- `date` (DATE / VARCHAR internally in SQLite)
- `description` (VARCHAR)

*Relationships*: Standalone entity representing full transaction audit logs.

## 5. Security and Features Highlight
- Avoids blocking Swing EDT by mapping `SwingWorker` tasks.
- SQL Injection mitigation using `PreparedStatement` queries.
- Modular Maven architecture ensures dependencies are portable and executable in any desktop environment seamlessly.
