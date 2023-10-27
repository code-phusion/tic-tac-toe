package com.upskill.tictactoe.service;

import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

@Service
public class MiniMaxAI {
  public Move calculateMove(TicTacToeBoardModel board, char aiSymbol) {
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, aiSymbol);
          int score = minimax(board, 0, false, aiSymbol, aiSymbol == 'X' ? 'O' : 'X');
          board.undoMove(row, col);

          if (score > bestScore) {
            bestScore = score;
            bestMove = new Move(row, col);
          }
        }
      }
    }

    return bestMove;
  }

  private int minimax(TicTacToeBoardModel board, int depth, boolean isMaximizing, char maximizingSymbol, char minimizingSymbol) {
    if (board.isGameOver()) {
      char winner = board.getWinner();
      if (winner == maximizingSymbol) {
        return 1;
      } else if (winner == minimizingSymbol) {
        return -1;
      } else {
        return 0; // It's a draw
      }
    }

    if (isMaximizing) {
      int bestScore = Integer.MIN_VALUE;
      for (int row = 0; row < board.getSize(); row++) {
        for (int col = 0; col < board.getSize(); col++) {
          if (board.isEmpty(row, col)) {
            board.makeMove(row, col, maximizingSymbol);
            int score = minimax(board, depth + 1, false, maximizingSymbol, minimizingSymbol);
            board.undoMove(row, col);
            bestScore = Math.max(score, bestScore);
          }
        }
      }
      return bestScore;
    } else {
      int bestScore = Integer.MAX_VALUE;
      for (int row = 0; row < board.getSize(); row++) {
        for (int col = 0; col < board.getSize(); col++) {
          if (board.isEmpty(row, col)) {
            board.makeMove(row, col, minimizingSymbol);
            int score = minimax(board, depth + 1, true, maximizingSymbol, minimizingSymbol);
            board.undoMove(row, col);
            bestScore = Math.min(score, bestScore);
          }
        }
      }
      return bestScore;
    }
  }
}