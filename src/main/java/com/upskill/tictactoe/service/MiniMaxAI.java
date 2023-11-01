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
    char playerSymbol = (aiSymbol == 'X') ? 'O' : 'X';

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, aiSymbol);
          int score = minimax(board, 0, false, aiSymbol, playerSymbol);
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

  private int minimax(TicTacToeBoardModel board, int depth, boolean isMaximizing, char aiSymbol, char playerSymbol) {
    if (board.isWinning(aiSymbol)) {
      return 1;
    } else if (board.isWinning(playerSymbol)) {
      return -1;
    } else if (board.isDraw()) {
      return 0;
    }

    int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, isMaximizing ? aiSymbol : playerSymbol);
          int score = minimax(board, depth + 1, !isMaximizing, aiSymbol, playerSymbol);
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