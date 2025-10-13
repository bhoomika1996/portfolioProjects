import React, { useEffect, useState } from 'react';
import { Container, Paper, Typography, List, ListItem, ListItemText } from '@mui/material';
import api from '../../services/api';
import { useParams } from 'react-router-dom';

const GroupBalances = () => {
    const { groupId } = useParams();
    const [balances, setBalances] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get(`/api/groups/${groupId}/balances`)
            .then(res => setBalances(res.data))
            .catch(() => setBalances({}))
            .finally(() => setLoading(false));
    }, [groupId]);

    if (loading) return <Typography>Loading balances...</Typography>;

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
                <Typography variant="h5" gutterBottom>Who Owes Whom</Typography>
                {Object.keys(balances).length === 0 ? (
                    <Typography>No balances to show.</Typography>
                ) : (
                    <List>
                        {Object.entries(balances).map(([user, amount]) => (
                            <ListItem key={user}>
                                <ListItemText
                                    primary={amount >= 0
                                        ? `${user} is owed ₹${amount.toFixed(2)}`
                                        : `${user} owes ₹${Math.abs(amount).toFixed(2)}`
                                    }
                                />
                            </ListItem>
                        ))}
                    </List>
                )}
            </Paper>
        </Container>
    );
};

export default GroupBalances;
