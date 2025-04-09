# Expense Tracker Web Application

## 📃 Overview
This project is a **full-stack expense tracker** web application that allows users to log, track, and manage their income and expenses. It provides a simple yet powerful interface for budget management and spending analysis.

---

## 🔧 Tech Stack
- **Frontend**: ReactJS
- **Backend**: Java with Spring Boot
- **Database**: H2 (in-memory) or configurable relational DB
- **Build Tools**: Maven
- **API Testing**: Postman (for development)

---

## 🎓 Functional Requirements / Features

### 1. 💸 Add New Transaction
- Users can add a new transaction entry.
- Input fields: **Description**, **Amount**, and **Type** (Income / Expense)

### 2. 📊 View Transaction History
- Display list of transactions with amount, description, and type.
- Optional: Delete any transaction.

### 3. 📅 Monthly Summary
- Displays:
  - **Total Income**
  - **Total Expenses**
  - **Balance** (Income - Expenses)

### 4. ✅ Input Validation
- Ensures description is not empty
- Amount must be a valid positive number

### 5. ⚖️ Data Persistence
- Transactions stored using backend database (in-memory DB during development)
- REST APIs exposed to create, read, and delete transactions

### 6. 🔹 React UI Features
- Components:
  - Add Transaction Form
  - Transaction List
  - Summary Section
- Simple, clean layout with basic styling

---

## 🚀 Backend Logic (Spring Boot)

### `TransactionController.java`
- **`@PostMapping("/transactions")`**
  - Accepts a JSON payload to add a transaction
  - Calls service layer for logic

- **`@GetMapping("/transactions")`**
  - Returns all transactions stored in the database

- **`@DeleteMapping("/transactions/{id}")`**
  - Deletes a transaction by ID

### `TransactionService.java`
- **`addTransaction(Transaction txn)`**
  - Validates and persists transaction object

- **`getAllTransactions()`**
  - Retrieves all transaction records from DB

- **`deleteTransaction(Long id)`**
  - Removes transaction by ID

### `Transaction.java`
- Entity class with fields:
  - `id` (auto-generated)
  - `description`
  - `amount`
  - `type` (INCOME / EXPENSE)

- Annotated with `@Entity`, `@Id`, and validation annotations

---

## 🔮 Frontend Logic (ReactJS)

### Key State Variables
- `transactions`: Array of all transaction records
- `description`, `amount`, `type`: Form input states

### Major Components

#### `App.js`
- Root component that initializes and loads transactions

#### `AddTransactionForm.js`
- Input form to enter new transaction
- On submit, sends POST request to backend

#### `TransactionList.js`
- Displays the list of all transactions
- Each entry shows type (INCOME/EXPENSE), amount, and description
- Delete button to remove transaction

#### `Summary.js`
- Calculates total income, total expense, and net balance
- Rendered from `transactions` prop

---

## 📆 Challenges Faced
- Validating numeric input while allowing decimal values
- Handling CORS issues in development mode
- Maintaining consistent state between backend and frontend on CRUD operations
- Managing layout to show summary and transactions responsively

---

## 🚗 How to Run

### Backend
```bash
cd expenseTracker/backend
mvn spring-boot:run
```

### Frontend
```bash
cd expenseTracker/frontend
npm install
npm start
```

---

## 📊 Future Enhancements
- Add filtering by date range or category
- Export transactions to CSV
- Monthly spending charts
- Login and multi-user support

---

## 👨‍💼 Author
**Bhoomika** - Independent Software Developer with a passion for building useful tools.

---

## 📅 Repository Link
[Expense Tracker GitHub](https://github.com/bhoomika1996/portfolioProjects/tree/main/expenseTracker)

