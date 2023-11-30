import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Container, MenuItem, TextField, Typography } from '@mui/material';
import gameApi from '../api/api';

function StartScreen() {
  const [size, setSize] = useState(10);
  const [winNumber, setWinNumber] = useState(3);
  const [aiList, setAiList] = useState([]);
  const [selectedAiModel, setSelectedAiModel] = useState({ id: "" });
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      const response = await gameApi.getAIList();
      setAiList(response?.list);
      if (response.list && response.list.length) {
        setSelectedAiModel(response.list[0]);
      }
    }
    fetchData();
  }, []);

  const startGame = async (againstAI) => {
    try {
      const fieldSize = parseInt(size, 10);
      if (againstAI) {
        if (fieldSize > selectedAiModel.maxBoardSize || winNumber !== selectedAiModel.winNumber) {
          alert("Sorry, selected AI only supports a Field Size <= " + selectedAiModel.maxBoardSize + " and a Win Number = " + selectedAiModel.winNumber);
          return;
        }
      }

      if (fieldSize < 3 || fieldSize > 50) {
        alert("Field Size should be between 3 and 50");
        return;
      }

      if (winNumber < 3 || winNumber > fieldSize) {
        alert("Win Number should be between 3 and the Field Size");
        return;
      }

      const response = await gameApi.newGame({
        fieldSize,
        winNumber,
        againstAI,
        aiId: selectedAiModel.id
      });

      const gameId = response?.gameId;
      navigate(`game/${gameId}`);
    } catch (error) {
      console.error('An error occurred while starting the game: ', error);
    }
  };

  const buttonStyle = { margin: '10px' };

  const aiListOptions = [];
  aiList?.forEach((aiModel) => {
    aiListOptions.push(<MenuItem key={aiModel?.id} value={aiModel?.id}>{aiModel?.name}</MenuItem>);
  });

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
        value={size.toString()}
        onChange={(e) => setSize(Number(e.target.value))}
        style={{ marginBottom: '20px' }}
      />
      <TextField
        type="number"
        variant="outlined"
        label="Enter Win Number (how many cells to fill in row to win)"
        fullWidth
        value={winNumber.toString()}
        onChange={(e) => setWinNumber(Number(e.target.value))}
        style={{ marginBottom: '20px' }}
      />
      <TextField
        select
        variant="outlined"
        label="Select AI algo"
        fullWidth
        style={{ marginBottom: '20px' }}
        value={selectedAiModel.id}
        defaultValue={selectedAiModel.id}
        onChange={(e) => {
          const aiModel = aiList.find((curAiModel) => {
            return curAiModel.id === e.target.value;
          });
          setSelectedAiModel(aiModel);
        }}
      >
        {aiListOptions}
      </TextField>
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