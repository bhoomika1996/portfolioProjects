import React, { useState } from 'react';
import { Container, Paper, Typography, Button, Box } from '@mui/material';
import api from '../../services/api';
import { useParams } from 'react-router-dom';

const SettleUp = () => {
    const { groupId } = useParams();
    const [owedById, setOwedById] = useState('');
    const [owedToId, setOwedToId] = useState('');
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async () => {
        try {
            await api.post(`/api/groups/${groupId}/settle`, { owedById, owedToId });
            setSuccess('Settlement recorded!');
            setError('');
        } catch (err) {
            setError('Failed to settle up.');
            setSuccess('');
        }
    };

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
                <Typography variant="h5" gutterBottom>Settle Up</Typography>
                {error && <Typography color="error">{error}</Typography>}
                {success && <Typography color="success.main">{success}</Typography>}
                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                    <Typography>Who paid whom?</Typography>
                    <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
                        <input
                            type="number"
                            placeholder="Owed By User ID"
                            value={owedById}
                            onChange={e => setOwedById(e.target.value)}
                            style={{ flex: 1 }}
                        />
                        <input
                            type="number"
                            placeholder="Owed To User ID"
                            value={owedToId}
                            onChange={e => setOwedToId(e.target.value)}
                            style={{ flex: 1 }}
                        />
                    </Box>
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                        sx={{ mt: 3 }}
                    >
                        Mark as Settled
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default SettleUp;
