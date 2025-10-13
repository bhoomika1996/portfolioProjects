import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import Navbar from './components/Layout/Navbar';
import ExpenseList from './components/Expenses/ExpenseList';
import AddExpense from './components/Expenses/AddExpense';
import ProtectedRoute from './components/Shared/ProtectedRoute';
import CreateGroup from './components/Groups/CreateGroup';
import MyGroups from './components/Groups/MyGroups';
import AddGroupExpense from './components/Groups/AddGroupExpense';
import GroupExpenseHistory from './components/Groups/GroupExpenseHistory';
import GroupBalances from './components/Groups/GroupBalances';
import SettleUp from './components/Groups/SettleUp';

// Main Application Component: sets up routing
function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        {/* Routing for authentication */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        {/* Protect expense pages so only logged in users see them */}
        <Route
          path="/expenses"
          element={
            <ProtectedRoute>
              <ExpenseList />
            </ProtectedRoute>
          }
        />
        <Route
          path="/add-expense"
          element={
            <ProtectedRoute>
              <AddExpense />
            </ProtectedRoute>
          }
        />
        <Route
            path="/create-group"
            element={
                <ProtectedRoute>
                    <CreateGroup />
                </ProtectedRoute>
            }
        />
        <Route
            path="/my-groups"
            element={
                <ProtectedRoute>
                    <MyGroups />
                </ProtectedRoute>
            }
        />
        <Route
            path="/groups/:groupId/add-expense"
            element={
                <ProtectedRoute>
                    <AddGroupExpense />
                </ProtectedRoute>
            }
        />
        <Route
            path="/groups/:groupId/history"
            element={
                <ProtectedRoute>
                    <GroupExpenseHistory />
                </ProtectedRoute>
            }
        />
        <Route
            path="/groups/:groupId/balances"
            element={
                <ProtectedRoute>
                    <GroupBalances />
                </ProtectedRoute>
            }
        />
        <Route
          path="/groups/:groupId/settle"
          element={
              <ProtectedRoute>
                  <SettleUp />
              </ProtectedRoute>
          }
      />
      </Routes>
    </Router>
  );
}

export default App;
