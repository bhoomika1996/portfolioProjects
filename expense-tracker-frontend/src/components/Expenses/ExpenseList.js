import React, { useEffect, useState } from 'react';
import { Paper, Typography, List, ListItem, ListItemText, CircularProgress } from '@mui/material';
import api from '../../services/api';

// Displays a user's expenses in a list
const ExpenseList = () => {
    // State to hold fetched expenses
    const [expenses, setExpenses] = useState([]);
    // State for loading animation
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Fetch expenses from backend when component loads
        api.get('/expenses')
            .then(res => {
                setExpenses(res.data);
            })
            .catch(err => {
                // You could set an error message here if needed
                setExpenses([]);
            })
            .finally(() => setLoading(false));
    }, []); // Run only once after render

    if (loading) return <CircularProgress />;

    return (
        <Paper sx={{ p: 4, mt: 4 }}>
            <Typography variant="h5" gutterBottom>
                Your Expenses
            </Typography>
            {expenses.length === 0 ? (
                <Typography>No expenses found!</Typography>
            ) : (
                <List>
                    {expenses.map((expense) => (
                        <ListItem key={expense.id}>
                            <ListItemText
                                primary={`₹${expense.amount} - ${expense.description}`}
                                secondary={`Category: ${expense.category?.name || 'N/A'} | Date: ${expense.date}`}
                            />
                        </ListItem>
                    ))}
                </List>
            )}
        </Paper>
    );
};

export default ExpenseList;
