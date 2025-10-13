import React, { useEffect, useState } from 'react';
import { Container, Paper, Typography, List, ListItem, ListItemText, Button } from '@mui/material';
import api from '../../services/api';
import useAuth from '../../contexts/useAuth';
import { Link } from 'react-router-dom';

// Lists all groups the current user belongs to
const MyGroups = () => {
    const { user } = useAuth();
    const [groups, setGroups] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        if (user?.id) {
            api.get(`/api/groups/user/${user.id}`)
                .then(res => setGroups(res.data))
                .catch(() => setError('Failed to load your groups'));
        }
    }, [user]);

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
                <Typography variant="h5" gutterBottom>
                    My Groups
                </Typography>
                {error && <Typography color="error">{error}</Typography>}
                {groups.length === 0 ? (
                    <Typography>You are not part of any groups yet.</Typography>
                ) : (
                    <List>
                        {groups.map(group => (
                            <ListItem key={group.id}
                                secondaryAction={
                                    <Button variant="outlined"
                                            component={Link}
                                            to={`/groups/${group.id}/add-expense`}>
                                        Add Expense
                                    </Button>
                                }
                            >
                                <ListItemText
                                    primary={group.name}
                                    secondary={`Members: ${group.members.map(m => m.username).join(', ')}`}
                                />
                            </ListItem>
                        ))}
                    </List>
                )}
            </Paper>
        </Container>
    );
};

export default MyGroups;
