// src/components/Layout/Navbar.js
import React from 'react';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import useAuth from '../../contexts/useAuth';

const Navbar = () => {
    const { token, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" sx={{ flexGrow: 1 }}>
                    Expense Tracker
                </Typography>
                {token ? (
                    <>
                        <Button color="inherit" component={Link} to="/expenses">Expenses</Button>
                        <Button color="inherit" component={Link} to="/add-expense">Add Expense</Button>
                        <Button color="inherit" component={Link} to="/create-group">Create Group</Button>
                        <Button color="inherit" component={Link} to="/my-groups">My Groups</Button>
                        <Button color="inherit" onClick={handleLogout}>Logout</Button>
                    </>
                ) : (
                    <>
                        <Button color="inherit" component={Link} to="/login">Login</Button>
                        <Button color="inherit" component={Link} to="/register">Register</Button>
                    </>
                )}
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
