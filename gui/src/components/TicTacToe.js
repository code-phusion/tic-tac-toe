import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Button,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import gameApi from '../api/api';
import { useQuery, useMutation } from '@tanstack/react-query';

const TicTacToe = () => {
  const { gameId } = useParams();
  const navigate = useNavigate();
  const [winner, setWinner] = useState(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false); // New state to control dialog visibility

  const { data: gameState, refetch } = useQuery(
    ['gameState', gameId],
    async () => gameApi.getState(gameId, gameState?.stateId || 0),
    {
      onSuccess: () => setTimeout(() => refetch(), 0),
      onError: () => setTimeout(() => refetch(), 1000),
    }
  );

  const board = gameState?.game?.board?.board || [];
  const gameOver = gameState?.game?.gameOver || false;
  const draw = gameState?.game?.draw || false;

  const { mutate: makeMove } = useMutation(async (params) =>
    await gameApi.move(gameId, params.row, params.col)
  );

  const { mutate: makeAIMove } = useMutation(async () =>
    await gameApi.makeAIMove(gameId)
  );

  const handleCellClick = async (row, col) => {
    if (!gameOver) {
      const currentPlayerSymbol = gameState?.game?.currentPlayerModel?.symbol;

      if (!gameState?.game?.againstAI || (currentPlayerSymbol === 'X')) {
        await makeMove({ row, col });

        if (gameState?.game?.againstAI && (currentPlayerSymbol === 'X') && !gameOver) {
          // Introduce a delay before the AI makes a move to avoid a race condition
          setTimeout(async () => {
            await makeAIMove();
          }, 1000);
        }
      } else {
        console.log("It's not your turn.");
      }
    }
  };

  const renderCell = (rowIndex, colIndex, value) => {
    const buttonClass = value === 'X' ? 'x-button' : value === 'O' ? 'o-button' : 'cell';

    return (
      <Button
        variant="outlined"
        className={`cell ${buttonClass}`}
        onClick={() => handleCellClick(rowIndex, colIndex)}
        key={colIndex}
      >
        {value}
      </Button>
    );
  };

  const handleRestart = () => {
    navigate('/');
  };

  useEffect(() => {
    if (gameOver || draw) {
      setIsDialogOpen(true); // Open the dialog when the game is over or it's a draw
    } else {
      setIsDialogOpen(false); // Close the dialog if the game is not over
    }
  }, [gameOver, draw]);

  return (
    <div className="game">
      <Typography variant="h4" gutterBottom>
        Tic Tac Toe
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

      <Dialog open={isDialogOpen} onClose={() => setIsDialogOpen(false)}>
        <DialogTitle>Game Over</DialogTitle>
        <DialogContent>
          <Typography>
            {gameOver ? `${gameState?.game?.currentPlayerModel?.symbol} wins!` : "It's a draw!"}
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setIsDialogOpen(false)}>OK</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default TicTacToe;
