import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Container, TextField, Typography } from '@mui/material';

function StartScreen() {
  const [config, setConfig] = useState('');
  const navigate = useNavigate();

  const startGame = () => {
    // You can perform any necessary validation or processing of the config here.

    // Create a JSON object with the config data
    const configData = { config };

    // Send a POST request to the server
    fetch(`http://localhost:9090/api/game/new?size=${config}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(configData),
    })
      .then((response) => response.json())
      .then((data) => {
        // Handle the response from the server if needed
        console.log('Server response:', data);

        // Navigate to the game screen
        navigate('game');
      })
      .catch((error) => {
        // Handle any errors here
        console.error('Error:', error);
      });
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
        value={config}
        onChange={(e) => setConfig(e.target.value)}
        style={{ marginBottom: '20px' }}
      />
      <Button variant="contained" color="primary" onClick={startGame}>
        Start
      </Button>
    </Container>
  );
}

export default StartScreen;
