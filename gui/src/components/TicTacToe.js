import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {Button, Typography} from '@mui/material';
import gameApi from "../api/api";
import {useQuery, useMutation} from "@tanstack/react-query";

const TicTacToe = () => {

  const {gameId} = useParams();
  const [stateId, setStateId] = useState(0);

  const {data: state, refetch} = useQuery(["gameState", gameId], () => gameApi.getState(gameId, stateId), {
    onSuccess: () => refetch(),
    onError: () => setTimeout(() => refetch(), 1000)
  });
  const board = state?.game?.board?.board || [];
  const gameOver = state?.game?.gameOver || false;
  const draw = state?.game?.draw || false;
  const nextState = state?.stateId || stateId;

  useEffect(() => {
    setStateId(nextState);
  }, [nextState]);

  const {mutate: move} = useMutation(v => gameApi.move(gameId, v.row, v.col))
  const handleCellClick = async (row, col) => {
    if (!gameOver) {
      move({row, col});
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
