import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuth from '../../contexts/useAuth';
import { Container, Paper, TextField, Button, Typography, Box } from '@mui/material';
import api from '../../services/api';

// Login form component
const Login = () => {
  // State for username and password input fields
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  // State for displaying error messages
  const [error, setError] = useState('');
  // Get login method from Auth context — to update auth state on success
  const { login } = useAuth();
  // React Router hook for navigation
  const navigate = useNavigate();

  // Handles form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Try logging in with the API
      const res = await api.post('/auth/login', { username, password });
      // On success, store token and user info; redirect to expenses
      login(res.data.user, res.data.token);
      navigate('/expenses');
    } catch (err) {
      // On fail, show error
      setError('Invalid credentials');
    }
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Login
        </Typography>
        {error && <Typography color="error">{error}</Typography>}
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
          <TextField
            fullWidth
            label="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            margin="normal"
            required
          />
          <TextField
            fullWidth
            label="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            margin="normal"
            required
          />
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 3 }}
          >
            Login
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default Login;
