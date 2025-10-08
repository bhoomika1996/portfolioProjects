import React from 'react';
import { Navigate } from 'react-router-dom';
import useAuth from '../../contexts/useAuth';

// This component wraps routes that should only be visible to authenticated users
const ProtectedRoute = ({ children }) => {
  const { token, loading } = useAuth();

  // Show a loading indicator while checking auth state
  if (loading) return <div>Loading...</div>;

  // If not logged in, redirect to login page
  if (!token) return <Navigate to="/login" />;

  // If logged in, render the protected content
  return children;
};

export default ProtectedRoute;
