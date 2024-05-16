import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  Button,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
} from "@mui/material";
import gameApi from "../api/api";
import { useQuery, useMutation } from "@tanstack/react-query";
import NavBar from "./NavBar";
import BackIcon from "./SVGs/BackIcon";
import ClearBoardIcon from "./SVGs/ClearBoardIcon";

const TicTacToe = () => {
  const { gameId } = useParams();
  const navigate = useNavigate();

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [resultImage, setResultImage] = useState(null);
  const [resultText, setResultText] = useState(null);

  const [cellNotEmptyError, setCellNotEmptyError] = useState(""); // New state variable

  const { data: gameState, refetch } = useQuery(
    ["gameState", gameId],
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

  const { mutate: makeMove } = useMutation(
    async params => await gameApi.move(gameId, params.row, params.col)
  );

  const { mutate: makeAIMove } = useMutation(
    async () => await gameApi.makeAIMove(gameId)
  );

  const { mutate: clearBoard } = useMutation(
    async () => await gameApi.clearBoard(gameId)
  );

  const handleCellClick = async (row, col) => {
    if (!gameOver) {
      const currentPlayerSymbol = gameState?.game?.currentPlayerModel?.symbol;

      if (!againstAI || currentPlayerSymbol === "X") {
        if (board[row][col] === " ") {
          setCellNotEmptyError("");
          try {
            await makeMove({ row, col });
          } catch (error) {
            console.error("Error making move:", error);
          }
        } else {
          setCellNotEmptyError("Cell is not empty! Please move correctly.");
          throw new Error("Cell is not empty.");
        }

        if (againstAI && currentPlayerSymbol === "X" && !gameOver) {
          const aiMoveTimeout = setTimeout(async () => {
            try {
              await makeAIMove();
            } catch (error) {
              console.error("Error making AI move:", error);
            }
          }, 1000);

          // Clear the timeout if the game ends or the player's turn changes
          gameState?.game?.onGameOver(() => clearTimeout(aiMoveTimeout));
          gameState?.game?.onTurnChange(() => clearTimeout(aiMoveTimeout));
        }
      } else {
        console.log("It's not your turn.");
      }
    }
  };

  const handleRestart = () => {
    navigate("/StartScreen");
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
          setResultImage(
            winnerSymbol === "O"
              ? "https://media.tenor.com/sPGJ7qsV2ukAAAAj/laughing-bender.gif"
              : "https://media.tenor.com/dSiQJughhlYAAAAC/fetal-position-futurama.gif"
          );
          setResultText(`${winnerSymbol} wins!`);
        } else {
          setResultText(`${winnerSymbol} wins!`);
        }
      } else {
        if (againstAI) {
          setResultImage(
            "https://media.tenor.com/ihKmG2owX4AAAAAj/what-are-we-gonna-do-bender.gif"
          );
          setResultText("It's a draw!");
        } else {
          setResultText("It's a draw!");
        }
      }
    } else {
      setIsDialogOpen(false);
      setCellNotEmptyError(""); // Reset the error message
      setResultImage(null);
      setResultText(null);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [gameOver, draw]);

  const renderCell = (rowIndex, colIndex, value) => {
    console.log(value, "value");
    const iconClass =
      value === "X" ? "x-button" : value === "O" ? "o-button" : "cell";
    const lastMoveClass = lastMove
      ? rowIndex === lastMove.row && colIndex === lastMove.col
        ? "last-move"
        : ""
      : "";
    const buttonClass = iconClass + " " + lastMoveClass;

    return (
      <Button
        variant="outlined"
        className={`${
          board?.length === 3 ? "normal-cell" : "flex-cell"
        } cell ${buttonClass}`}
        onClick={() => handleCellClick(rowIndex, colIndex)}
        key={colIndex}
      >
        {/* {value} */}
      </Button>
    );
  };

  return (
    <>
      <NavBar />
      <div className="game">
        <div className="game-container">
          {cellNotEmptyError && (
            <div style={{ color: "red" }}>{cellNotEmptyError}</div>
          )}

          <div className="board">
            {board.map((row, rowIndex) => (
              <div className="board-row" key={rowIndex}>
                {row.split("").map((cell, colIndex) => (
                  <div
                    className={board?.length > 3 && "cell-container"}
                    key={colIndex}
                  >
                    {renderCell(rowIndex, colIndex, cell)}
                  </div>
                ))}
              </div>
            ))}
          </div>

          <div className="btn-container-board">
            <div className="btn" onClick={handleRestart}>
              <BackIcon className="hover-icon" /> Back to Menu
            </div>

            <div className="btn btn-pink" onClick={handleClearBoard}>
              Clear Board <ClearBoardIcon className="hover-icon-pink" />
            </div>
          </div>

          <Dialog open={isDialogOpen} className="customDialog">
            <DialogTitle
              sx={{
                width: "462px",
              }}
              className="dialog-header"
            >
              Game Over
            </DialogTitle>
            <DialogContent>
              {resultImage ? (
                <div className="image-results">
                  <img src={resultImage} alt="Result" />
                  <Typography>{resultText}</Typography>
                </div>
              ) : (
                <div className="image-results">
                  <Typography variant="h5">{resultText}</Typography>
                </div>
              )}
            </DialogContent>

            <div className="btn-container-board modal-footer">
              <div className="btn" onClick={handleRestart}>
                <BackIcon className="hover-icon" /> Back to Menu
              </div>

              <div
                className="btn btn-pink"
                onClick={() => {
                  handleClearBoard();
                  setIsDialogOpen(false);
                }}
              >
                Clear Board <ClearBoardIcon className="hover-icon-pink" />
              </div>
            </div>
          </Dialog>
        </div>
      </div>
    </>
  );
};

export default TicTacToe;
