import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Container, TextField, Typography } from '@mui/material';
import gameApi from '../api/api';

function StartScreen() {
  const [size, setSize] = useState('10');
  const navigate = useNavigate();

  const startGame = async (againstAI) => {
    try {
      // Create a new game with the selected size and againstAI flag
      const response = await gameApi.newGame(size, againstAI);
      const gameId = response?.gameId;


      // Navigate to the game with the generated gameId
      navigate(`game/${gameId}`);
    } catch (error) {
      console.error('An error occurred while starting the game:', error);
    }
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
      <Button
        variant="contained"
        color="primary"
        onClick={() => startGame(true)} // Start Against AI
      >
        Start Against AI
      </Button>
      <Button
        variant="contained"
        color="secondary"
        onClick={() => startGame(false)} // Start Against Human
      >
        Start Against Human
      </Button>
    </Container>
  );
}

export default StartScreen;
