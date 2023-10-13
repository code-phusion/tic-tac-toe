import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Typography } from '@mui/material';
import gameApi from "../api/api";

const TicTacToe = () => {
  const [board, setBoard] = useState([]);
  const [gameOver, setGameOver] = useState(false);
  const [draw, setDraw] = useState(false);

  const fetchState = async () => {
    const state = await gameApi.getState();
    setBoard(state.board.board);
    setGameOver(state.gameOver);
    setDraw(state.draw);
  }

  useEffect(() => {
    fetchState();
  }, []);

  const handleCellClick = async (row, col) => {
    if (!gameOver) {
      await gameApi.move(row, col);
      fetchState();
    }
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
