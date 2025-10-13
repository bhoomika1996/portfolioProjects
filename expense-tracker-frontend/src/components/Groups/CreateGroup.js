import React, { useState, useEffect } from 'react';
import { Container, Paper, TextField, Typography, Button, Box, MenuItem, Select, InputLabel, FormControl, Chip, OutlinedInput } from '@mui/material';
import api from '../../services/api';
import useAuth from '../../contexts/useAuth';

// UX Flow: User enters group name, picks members, submits form
const CreateGroup = () => {
    const [groupName, setGroupName] = useState('');
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [users, setUsers] = useState([]);
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    const { user } = useAuth();

    useEffect(() => {
        // Fetch all users for member selection (shown as multi-select chips)
        api.get('/api/users')
            .then(res => setUsers(res.data))
            .catch(() => setError('Could not fetch users'));
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Always include logged-in user as a member
        const memberIds = [...new Set([user.id, ...selectedUsers])];
        try {
            await api.post('/api/groups', {
                name: groupName,
                memberIds: memberIds
            });
            setSuccess(`Group "${groupName}" created!`);
            setError('');
            setGroupName('');
            setSelectedUsers([]);
        } catch (err) {
            setError('Failed to create group. Try again.');
            setSuccess('');
        }
    };

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
                <Typography variant="h5" gutterBottom>Create New Group</Typography>
                {error && <Typography color="error">{error}</Typography>}
                {success && <Typography color="success.main">{success}</Typography>}

                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                    <TextField
                        required
                        label="Group Name"
                        fullWidth
                        margin="normal"
                        value={groupName}
                        onChange={e => setGroupName(e.target.value)}
                    />
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Members (multi-select)</InputLabel>
                        <Select
                            multiple
                            value={selectedUsers}
                            onChange={e => setSelectedUsers(e.target.value)}
                            input={<OutlinedInput label="Members" />}
                            renderValue={(selected) => (
                                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                                    {selected.map((id) => {
                                        const member = users.find(u => u.id === id);
                                        return <Chip key={id} label={member ? member.username : id} />;
                                    })}
                                </Box>
                            )}
                        >
                            {users.filter(u => u.id !== user.id).map((user) => (
                                <MenuItem key={user.id} value={user.id}>
                                    {user.username}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 3 }}>
                        Create Group
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default CreateGroup;
