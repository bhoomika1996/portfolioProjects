import React, { createContext, useState, useEffect } from 'react';
import api from '../services/api';

// This context will make the authentication state and methods available throughout your app
export const AuthContext = createContext();

// The provider component wraps your app and provides the authentication state (user, token) and functions (login, logout)
export const AuthProvider = ({ children }) => {
  // Holds the current user (null if not logged in)
  const [user, setUser] = useState(null);
  // Tracks the JWT token (initialize from localStorage to persist sessions)
  const [token, setToken] = useState(localStorage.getItem('token'));
  // Tracks loading state while checking for logged-in user
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // On launch or token change, fetch user info if token exists
    if (token) {
      // You'd implement a /auth/me endpoint in backend to fetch user profile from JWT
      api.get('/auth/me')
        .then(res => {
          setUser(res.data);
        })
        .catch(() => {
          // If token invalid, remove it and clear state
          localStorage.removeItem('token');
          setToken(null);
        })
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, [token]);

  // When user successfully logs in, save token and user info
  const login = (userData, authToken) => {
    localStorage.setItem('token', authToken); // Persist JWT in browser
    setToken(authToken);
    setUser(userData);
  };

  // Log out: clear everything
  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  return (
    // AuthContext.Provider will make these values and functions available to any child component in your app
    <AuthContext.Provider value={{ user, token, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
