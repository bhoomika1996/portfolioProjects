import React, { useEffect, useState } from 'react';
import { Container, Paper, Typography, List, ListItem, ListItemText } from '@mui/material';
import api from '../../services/api';
import { useParams } from 'react-router-dom';

const GroupExpenseHistory = () => {
    const { groupId } = useParams();
    const [expenses, setExpenses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get(`/api/groups/${groupId}/expenses`)
            .then(res => setExpenses(res.data))
            .catch(() => setExpenses([]))
            .finally(() => setLoading(false));
    }, [groupId]);

    if (loading) return <Typography>Loading...</Typography>;

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
                <Typography variant="h5" gutterBottom>Expense History</Typography>
                {expenses.length === 0 ? (
                    <Typography>No expenses in this group.</Typography>
                ) : (
                    <List>
                        {expenses.map(exp => (
                            <ListItem key={exp.id}>
                                <ListItemText
                                    primary={`₹${exp.amount} - ${exp.description}`}
                                    secondary={`Paid by: ${exp.paidBy.username} | ${exp.date}`}
                                />
                            </ListItem>
                        ))}
                    </List>
                )}
            </Paper>
        </Container>
    );
};

export default GroupExpenseHistory;
