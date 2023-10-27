package com.upskill.tictactoe.service;

import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

/*
* Pretty Stupid AI Algo ;)
* */
@Service
public class MiniMaxAI {
  private char aiSymbol;
  private char opponentSymbol;

  public Move calculateMove(TicTacToeBoardModel board, char aiSymbol, int depth) {
    this.aiSymbol = aiSymbol;
    this.opponentSymbol = (aiSymbol == 'X') ? 'O' : 'X';

    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          board.makeMove(row, col, aiSymbol);
          int score = minimax(board, depth - 1, false, aiSymbol, Integer.MIN_VALUE, Integer.MAX_VALUE);
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

  private int minimax(TicTacToeBoardModel board, int depth, boolean isMaximizing, char currentPlayer, int alpha, int beta) {
    if (board.isGameOver() || depth == 0) {
      return evaluate(board, aiSymbol);
    }

    int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          char playerSymbol = (currentPlayer == aiSymbol) ? aiSymbol : opponentSymbol;
          board.makeMove(row, col, playerSymbol);
          int score = minimax(board, depth - 1, !isMaximizing, playerSymbol, alpha, beta);
          board.undoMove(row, col);

          if (isMaximizing) {
            bestScore = Math.max(bestScore, score);
            alpha = Math.max(alpha, score);
          } else {
            bestScore = Math.min(bestScore, score);
            beta = Math.min(beta, score);
          }

          if (beta <= alpha) {
            break;
          }
        }
      }
    }

    return bestScore;
  }

  private int evaluate(TicTacToeBoardModel board, char playerSymbol) {
    if (board.isWinning(playerSymbol)) {
      return 10;
    } else if (board.isWinning(opponentSymbol)) {
      return -10;
    } else if (board.isCenterOccupiedBy(playerSymbol)) {
      return 5;
    } else if (board.isCenterOccupiedBy(opponentSymbol)) {
      return -5;
    }
    return 0;
  }
}