import React, { useState, useEffect } from 'react';
import { Container, Paper, Typography, TextField, Button, Box, MenuItem } from '@mui/material';
import api from '../../services/api';
import useAuth from '../../contexts/useAuth';
import { useParams } from 'react-router-dom';

const AddGroupExpense = () => {
    const { groupId } = useParams();
    const { user } = useAuth();
    const [group, setGroup] = useState(null);
    const [amount, setAmount] = useState('');
    const [description, setDescription] = useState('');
    const [categoryName, setCategoryName] = useState('');
    const [date, setDate] = useState('');
    const [splitAmong, setSplitAmong] = useState([]);
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    // Fetch group info for member selection
    useEffect(() => {
        api.get(`/api/groups/${groupId}`)
            .then(res => setGroup(res.data))
            .catch(() => setError('Failed to load group info'));
    }, [groupId]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        // By default, split among all current members
        const splitAmongIds = splitAmong.length ? splitAmong : (group?.members.map(m => m.id) || []);
        try {
            await api.post(`/api/groups/${groupId}/expenses`, {
                description,
                amount,
                date,
                categoryName,
                paidById: user.id,
                splitAmongIds: splitAmongIds
            });
            setSuccess('Expense added to group!');
            setError('');
            setAmount('');
            setDescription('');
            setCategoryName('');
            setDate('');
            setSplitAmong([]);
        } catch (err) {
            setError('Failed to add group expense.');
            setSuccess('');
        }
    };

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
                <Typography variant="h6">
                    Add Expense to Group: {group?.name}
                </Typography>
                {error && <Typography color="error">{error}</Typography>}
                {success && <Typography color="success.main">{success}</Typography>}
                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                    <TextField
                        required
                        label="Amount"
                        type="number"
                        fullWidth
                        margin="normal"
                        value={amount}
                        onChange={e => setAmount(e.target.value)}
                    />
                    <TextField
                        required
                        label="Description"
                        fullWidth
                        margin="normal"
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                    />
                    <TextField
                        required
                        select
                        label="Category"
                        fullWidth
                        margin="normal"
                        value={categoryName}
                        onChange={e => setCategoryName(e.target.value)}
                    >
                        {['food', 'travel', 'entertainment', 'rent', 'other'].map(cat => (
                            <MenuItem key={cat} value={cat}>{cat}</MenuItem>
                        ))}
                    </TextField>
                    <TextField
                        required
                        label="Date"
                        type="date"
                        fullWidth
                        margin="normal"
                        InputLabelProps={{ shrink: true }}
                        value={date}
                        onChange={e => setDate(e.target.value)}
                    />
                    <Button type="submit" variant="contained" color="primary" sx={{ mt: 3 }}>
                        Add Group Expense
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default AddGroupExpense;
