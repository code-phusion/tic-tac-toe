import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Container, TextField, Typography } from '@mui/material';
import gameApi from '../api/api';

function StartScreen() {
  const [size, setSize] = useState('10');
  const navigate = useNavigate();

  const startGame = async (againstAI) => {
    try {
      const fieldSize = parseInt(size, 10);
        if (againstAI && (size < 5 || size > 8)) {
        alert("Board min size 5x5 and max size 8x8");
        return;
      }
      if (fieldSize < 5) {
        alert("Please enter size of 5 or greater.");
        return;
      }

      if (fieldSize > 50) {
        alert("Please enter size of 50 or less.");
        return;
      }

      const response = await gameApi.newGame(fieldSize, againstAI);
      const gameId = response?.gameId;
      navigate(`game/${gameId}`);
    } catch (error) {
      console.error('An error occurred while starting the game:', error);
    }
  };

  const buttonStyle = { margin: '10px' };

  return (
    <Container maxWidth="sm" style={{ textAlign: 'center', paddingTop: '100px' }}>
      <Typography variant="h4" gutterBottom>
        Start Game
      </Typography>
      <TextField
        type="number"
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
        onClick={() => startGame(true)}
        style={buttonStyle}
      >
        Start Against AI
      </Button>
      <Button
        variant="contained"
        color="secondary"
        onClick={() => startGame(false)}
        style={buttonStyle}
      >
        Start Against Human
      </Button>
    </Container>
  );
}

export default StartScreen;