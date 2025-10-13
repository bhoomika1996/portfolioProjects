import React, { useState, useEffect } from 'react';
import { TextField, Button, Paper, Typography, Box, MenuItem, FormControl, InputLabel, Select } from '@mui/material';
import api from '../../services/api';
import useAuth from '../../contexts/useAuth';

// Enhanced form: supports personal and group expenses
const AddExpense = () => {
    const [amount, setAmount] = useState('');
    const [description, setDescription] = useState('');
    const [categoryName, setCategoryName] = useState('');
    const [date, setDate] = useState('');
    const [groupId, setGroupId] = useState(''); // '' = personal, '123' = group
    const [groups, setGroups] = useState([]); // fetched from backend
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    const { user } = useAuth(); // to get userId

    // Fetch user's groups on mount
    useEffect(() => {
        if (user?.id) {
            api.get('/api/groups/user') // You'll implement this endpoint
                .then(res => setGroups(res.data))
                .catch(err => console.error("Failed to load groups"));
        }
    }, [user]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!user?.id) {
            setError('User ID not available. Try re-logging in.');
            return;
        }
        try {
            console.log('Submitting with user id:', user?.id);

            await api.post('/expenses', {
                amount,
                description,
                date,
                categoryName,
                paidById: user.id,
                groupId: groupId || null // send null if no group
            });
            setSuccess('Expense added!');
            setError('');
            // Reset form
            setAmount('');
            setDescription('');
            setCategoryName('');
            setDate('');
            setGroupId('');
        } catch (err) {
            setError('Failed to add expense. Please try again.');
            setSuccess('');
        }
    };

    return (
        <Paper sx={{ p: 4, mt: 4 }}>
            <Typography variant="h5" gutterBottom>
                Add Expense
            </Typography>
            {error && <Typography color="error">{error}</Typography>}
            {success && <Typography color="success.main">{success}</Typography>}

            <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                {/* Group Selection */}
                <FormControl fullWidth margin="normal">
                    <InputLabel>Group (Optional)</InputLabel>
                    <Select
                        value={groupId}
                        onChange={(e) => setGroupId(e.target.value)}
                        label="Group (Optional)"
                    >
                        <MenuItem value="">Personal Expense</MenuItem>
                        {groups.map((group) => (
                            <MenuItem key={group.id} value={group.id}>
                                {group.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                {/* Amount */}
                <TextField
                    required
                    label="Amount"
                    type="number"
                    fullWidth
                    margin="normal"
                    value={amount}
                    onChange={e => setAmount(e.target.value)}
                />

                {/* Description */}
                <TextField
                    required
                    label="Description"
                    fullWidth
                    margin="normal"
                    value={description}
                    onChange={e => setDescription(e.target.value)}
                />

                {/* Category */}
                <TextField
                    required
                    select
                    label="Category"
                    fullWidth
                    margin="normal"
                    value={categoryName}
                    onChange={e => setCategoryName(e.target.value)}
                >
                    {['food', 'travel', 'entertainment', 'rent', 'other'].map((cat) => (
                        <MenuItem key={cat} value={cat}>
                            {cat}
                        </MenuItem>
                    ))}
                </TextField>

                {/* Date */}
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

                <Button variant="contained" type="submit" sx={{ mt: 3 }}>
                    Add Expense
                </Button>
            </Box>
        </Paper>
    );
};

export default AddExpense;
