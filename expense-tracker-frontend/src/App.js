import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import Navbar from './components/Layout/Navbar';
// import ExpenseList and AddExpense when built
import ProtectedRoute from './components/Shared/ProtectedRoute';

// Main Application Component: sets up routing
function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        {/* Routing for authentication */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        {/* Protected routes for real expense pages go here */}
      </Routes>
    </Router>
  );
}

export default App;
