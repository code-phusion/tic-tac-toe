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

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [resultImage, setResultImage] = useState(null);
  const [resultText, setResultText] = useState(null);

  const [cellNotEmptyError, setCellNotEmptyError] = useState(''); // New state variable

  const { data: gameState, refetch } = useQuery(
    ['gameState', gameId],
    async () => gameApi.getState(gameId, gameState?.stateId || 0),
    {
      onSuccess: () => setTimeout(() => refetch(), 0),
      onError: () => setTimeout(() => refetch(), 1000),
    }
  );

  const board = gameState?.game?.board?.board || [];
  const lastMove = gameState?.game?.board?.lastMove || [];
  const gameOver = gameState?.game?.gameOver || false;
  const draw = gameState?.game?.draw || false;
  const againstAI = gameState?.game?.againstAI || false;

  const { mutate: makeMove } = useMutation(async (params) =>
    await gameApi.move(gameId, params.row, params.col)
  );

  const { mutate: makeAIMove } = useMutation(async () =>
    await gameApi.makeAIMove(gameId)
  );

  const { mutate: clearBoard } = useMutation(async () =>
    await gameApi.clearBoard(gameId)
  );

  const handleCellClick = async (row, col) => {
    if (!gameOver) {
      const currentPlayerSymbol = gameState?.game?.currentPlayerModel?.symbol;

      if (!againstAI || currentPlayerSymbol === 'X') {
        if (board[row][col] === ' ') {
          setCellNotEmptyError('');
          await makeMove({ row, col });
        } else {
          setCellNotEmptyError('Cell is not empty! Please move correctly.');
          throw new Error('Cell is not empty.');
        }

        if (againstAI && currentPlayerSymbol === 'X' && !gameOver) {
          setTimeout(async () => {
            await makeAIMove();
          }, 1000);
        }
      } else {
        console.log("It's not your turn.");
      }
    }
  };

  const handleRestart = () => {
    navigate('/');
  };

  const handleClearBoard = () => {
    clearBoard()?.then(() => {
      refetch();
    });
  };

  useEffect(() => {
    if (gameOver || draw) {
      setIsDialogOpen(true);

      if (gameOver) {
        const winnerSymbol = gameState?.game?.currentPlayerModel?.symbol;
        if (againstAI) {
          setResultImage(winnerSymbol === 'O'
            ? 'https://media.tenor.com/sPGJ7qsV2ukAAAAj/laughing-bender.gif'
            : 'https://media.tenor.com/dSiQJughhlYAAAAC/fetal-position-futurama.gif');
          setResultText(`${winnerSymbol} wins!`);
        } else {
          setResultText(`${winnerSymbol} wins!`);
        }
      } else {
        if (againstAI) {
          setResultImage('https://media.tenor.com/ihKmG2owX4AAAAAj/what-are-we-gonna-do-bender.gif');
          setResultText("It's a draw!");
        }
        else {
          setResultText("It's a draw!");
        }
      }
    } else {
      setIsDialogOpen(false);
      setCellNotEmptyError(''); // Reset the error message
      setResultImage(null);
      setResultText(null);
    }
  }, [gameOver, draw]);

  const renderCell = (rowIndex, colIndex, value) => {
    const iconClass = value === 'X' ? 'x-button' : value === 'O' ? 'o-button' : 'cell';
    const lastMoveClass = lastMove ?
      rowIndex === lastMove.row && colIndex === lastMove.col ? 'last-move' : ''
      : '';
    const buttonClass = iconClass + ' ' + lastMoveClass;

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

  return (
    <div className="game">
      <Typography variant="h4" gutterBottom>
        Tic Tac Toe
      </Typography>

      {cellNotEmptyError && <div style={{ color: 'red' }}>{cellNotEmptyError}</div>}

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

      <Button variant="contained" color="primary" onClick={handleRestart} style={{ margin: '10px' }}>
        Back to the Main Page
      </Button>
      <Button variant="contained" color="secondary" onClick={handleClearBoard} style={{ margin: '10px' }}>
        Clear Board
      </Button>

      <Dialog open={isDialogOpen} onClose={() => setIsDialogOpen(false)}>
        <DialogTitle>Game Over</DialogTitle>
        <DialogContent>
          {resultImage ? (
            <div>
              <img src={resultImage} alt="Result" />
              <Typography>{resultText}</Typography>
            </div>
          ) : (
            <Typography>{resultText}</Typography>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setIsDialogOpen(false)}>OK</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default TicTacToe;
