import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Typography } from '@mui/material';

const TicTacToe = () => {
  const [board, setBoard] = useState([]);
  const [gameOver, setGameOver] = useState(false);
  const [draw, setDraw] = useState(false);

  const fetchState = () => {
    // Fetch the initial game state from the backend when the component mounts
    fetch('http://localhost:9090/api/game/state')
      .then((response) => response.json())
      .then((data) => {
        setBoard(data.board.board);
        setGameOver(data.gameOver)
        setDraw(data.draw)
      })
      .catch((error) => {
        console.error('Error fetching game state:', error);
      });
  }

  useEffect(() => {
    fetchState();
  }, []);

  const handleCellClick = (row, col) => {
    if (gameOver) {
      return;
    }

    // Send the move to the backend API
    fetch(`http://localhost:9090/api/game/move?row=${row}&col=${col}`, {
      method: 'POST',
    })
      .then((response) => response.json())
      .then((data) => {
        fetchState()
      })
      .catch((error) => {
        console.error('Error making a move:', error);
      });
  };

  const renderCell = (rowIndex, colIndex, value) => {
    let buttonClass;
    if (value === 'X') {
      buttonClass = "x-button cell";
    } else if (value === '0') {
      buttonClass = "o-button cell";
    } else {
      buttonClass = "cell";
    }
    return (
      <Button
        variant="outlined"
        className={buttonClass}
        onClick={() => handleCellClick(rowIndex, colIndex)}
        key={colIndex}
      >
        {value}
      </Button>
    )
  }

  const navigate = useNavigate();

  const handleRestart = () => {
    // Navigate back to the first screen (assuming it has the path "/")
    navigate('/');
  };

  return (
    <div className="game">
      <Typography variant="h4" gutterBottom>
        Tic Tac Toe
      </Typography>
      <Typography variant="h6" gutterBottom>
        Game Over : {gameOver ? 'true' : 'false'}
      </Typography>
      <Typography variant="h6" gutterBottom>
        Draw : {draw ? 'true' : 'false'}
      </Typography>

      <div className="board">
        {board.map((row, rowIndex) => (
          <div className="board-row" key={rowIndex}>
            {row.split('').map((cell, colIndex) => (
              <div className="cell-container" key={colIndex}>
                {renderCell(rowIndex, colIndex, cell)}
              </div>
            ))}
          </div>
        ))}
      </div>
      <Button variant="contained" color="primary" onClick={handleRestart}>
        Restart
      </Button>
    </div>
  );
};

export default TicTacToe;

