import { useContext } from 'react';
import { AuthContext } from './AuthContext';

// Custom hook to consume authentication context
// Makes it simple to get auth state and methods from anywhere in app
const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export default useAuth;
