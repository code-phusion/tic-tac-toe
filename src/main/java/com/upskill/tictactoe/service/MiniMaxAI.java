package com.upskill.tictactoe.service;

import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

/*
* Pretty Stupid AI Algo ;)
* */
@Service
public class MiniMaxAI {
  public Move calculateMove(TicTacToeBoardModel board, char aiSymbol) {
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, aiSymbol); // Make the move
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
    if (board.isGameOver()) {
      char winner = board.getWinner();
      if (winner == aiSymbol) {
        return 1;
      } else if (winner == ' ') {
        return 0; // It's a draw
      } else {
        return -1;
      }
    }

    int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          char playerSymbol = isMaximizing ? 'O' : 'X';
          board.makeMove(row, col, playerSymbol);
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