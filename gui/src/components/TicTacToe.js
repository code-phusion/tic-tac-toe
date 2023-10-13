import React from 'react';
import {useNavigate} from 'react-router-dom';
import {Button, Typography} from '@mui/material';
import gameApi from "../api/api";
import {useQuery} from "@tanstack/react-query";

const TicTacToe = () => {

  const {data: state, refetch: fetchState} = useQuery(["gameState"], gameApi.getState, {
    refetchInterval: 1000
  });
  const board = state?.board?.board || [];
  const gameOver = state?.gameOver || false;
  const draw = state?.draw || false;

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
