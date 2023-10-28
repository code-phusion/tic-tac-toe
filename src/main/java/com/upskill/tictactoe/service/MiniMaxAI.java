package com.upskill.tictactoe.service;

import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

/*
* Minimax algorithm for Tic Tac Toe
* For now only supports 3x3 boars. Will fix it in the future. Probably with other algo.
* */
@Service
public class MiniMaxAI {
  public Move calculateMove(TicTacToeBoardModel board, char aiSymbol) {
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, aiSymbol);
          if (board.isWinning(aiSymbol)) {
            board.undoMove(row, col);
            return new Move(row, col);
          }
          board.undoMove(row, col);
        }
      }
    }

    char playerSymbol = (aiSymbol == 'X') ? 'O' : 'X';
    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, playerSymbol);
          if (board.isWinning(playerSymbol)) {
            board.undoMove(row, col);
            return new Move(row, col);
          }
          board.undoMove(row, col);
        }
      }
    }

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, aiSymbol);
          int score = minimax(board, 0, false, aiSymbol);
          board.undoMove(row, col); // Undo the move

          if (score > bestScore) {
            bestScore = score;
            bestMove = new Move(row, col);
          }
        }
      }
    }

    return bestMove;
  }

  private int minimax(TicTacToeBoardModel board, int depth, boolean isMaximizing, char aiSymbol) {
    char playerSymbol = (aiSymbol == 'X') ? 'O' : 'X'; // Opponent's symbol

    if (board.isGameOver()) {
      char winner = board.getWinner();
      if (winner == aiSymbol) {
        return 1; // AI wins
      } else if (winner == playerSymbol) {
        return -1; // Opponent wins
      } else {
        return 0; // It's a draw
      }
    }

    int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, isMaximizing ? aiSymbol : playerSymbol);
          int score = minimax(board, depth + 1, !isMaximizing, aiSymbol);
          board.undoMove(row, col);

          if (isMaximizing) {
            bestScore = Math.max(bestScore, score);
          } else {
            bestScore = Math.min(bestScore, score);
          }
        }
      }
    }

    return bestScore;
  }
}