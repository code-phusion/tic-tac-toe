import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Container, TextField, Typography } from '@mui/material';
import gameApi from "../api/api";

function StartScreen() {
  const [size, setSize] = useState('');
  const navigate = useNavigate();

  const startGame = async () => {
    await gameApi.newGame(size);
    navigate('game');
  };

  return (
    <Container maxWidth="sm" style={{ textAlign: 'center', paddingTop: '100px' }}>
      <Typography variant="h4" gutterBottom>
        Start Game
      </Typography>
      <TextField
        type="number"
        min={1}
        max={100}
        variant="outlined"
        label="Enter Field Size"
        fullWidth
        value={size}
        onChange={(e) => setSize(e.target.value)}
        style={{ marginBottom: '20px' }}
      />
      <Button variant="contained" color="primary" onClick={startGame}>
        Start
      </Button>
    </Container>
  );
}

export default StartScreen;
