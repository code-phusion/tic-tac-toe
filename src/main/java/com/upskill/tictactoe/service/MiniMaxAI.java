package com.upskill.tictactoe.service;

import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

@Service
public class MiniMaxAI {
  private static final int MAX_DEPTH = 3;

  public Move calculateMove(TicTacToeBoardModel board, char aiSymbol) {
    Move lastPlayerMove = board.getLastMove();
    char playerSymbol = (aiSymbol == 'X') ? 'O' : 'X';

    if (lastPlayerMove != null) {
      int center = board.getSize() / 2;
      if (board.isEmpty(center, center)) {
        return new Move(center, center);
      }
    }

    int bestScore = Integer.MIN_VALUE;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;
    Move bestMove = null;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          int score = makeMoveAndMinimaxAndReturnScore(board, row, col, aiSymbol, 0, false, aiSymbol, playerSymbol, alpha, beta);
          if (score > bestScore) {
            bestScore = score;
            bestMove = new Move(row, col);
          }
        }
      }
    }

    return bestMove;
  }

  private int minimax(TicTacToeBoardModel board, int depth, boolean isMaximizing, char aiSymbol, char playerSymbol, int alpha, int beta) {
    if (board.isWinning(aiSymbol)) {
      return Integer.MAX_VALUE - depth;
    } else if (board.isWinning(playerSymbol)) {
      return Integer.MIN_VALUE + depth;
    } else if (board.isDraw()) {
      return 0;
    } else if (depth == MAX_DEPTH) {
      return evaluate(board, aiSymbol);
    }

    int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          char symbolToUse = isMaximizing ? aiSymbol : playerSymbol;
          int score = makeMoveAndMinimaxAndReturnScore(board, row, col, symbolToUse, depth + 1, !isMaximizing, aiSymbol, playerSymbol, alpha, beta);
          if (isMaximizing) {
            bestScore = Math.max(bestScore, score);
            alpha = Math.max(alpha, bestScore);
          } else {
            bestScore = Math.min(bestScore, score);
            beta = Math.min(beta, bestScore);
          }

          if (beta <= alpha) {
            break;
          }
        }
      }
    }

    return bestScore;
  }

  private int makeMoveAndMinimaxAndReturnScore(TicTacToeBoardModel board, int row, int col, char symbol, int depth, boolean isMaximizing,
                                               char aiSymbol, char playerSymbol, int alpha, int beta) {
    board.makeMove(row, col, symbol);
    int score = minimax(board, depth, isMaximizing, aiSymbol, playerSymbol, alpha, beta);
    board.undoMove(row, col);
    return score;
  }

  private int evaluate(TicTacToeBoardModel board, char aiSymbol) {
    if (board.isWinning(aiSymbol)) {
      return Integer.MAX_VALUE - MiniMaxAI.MAX_DEPTH;
    } else if (board.isWinning((aiSymbol == 'X') ? 'O' : 'X')) {
      return Integer.MIN_VALUE + MiniMaxAI.MAX_DEPTH;
    } else if (board.isDraw()) {
      return 0;
    }

    int aiScore = evaluatePlayer(board, aiSymbol);
    int opponentScore = evaluatePlayer(board, (aiSymbol == 'X') ? 'O' : 'X');

    return aiScore - opponentScore;
  }

  private int evaluatePlayer(TicTacToeBoardModel board, char playerSymbol) {
    int score = 0;

    for (int row = 0; row < board.getSize(); row++) {
      score += evaluateLine(board, board.getBoard()[row], playerSymbol);
    }

    for (int col = 0; col < board.getSize(); col++) {
      char[] column = new char[board.getSize()];
      for (int row = 0; row < board.getSize(); row++) {
        column[row] = board.getBoard()[row][col];
      }
      score += evaluateLine(board, column, playerSymbol);
    }

    char[] mainDiagonal = new char[board.getSize()];
    char[] reverseDiagonal = new char[board.getSize()];
    for (int i = 0; i < board.getSize(); i++) {
      mainDiagonal[i] = board.getBoard()[i][i];
      reverseDiagonal[i] = board.getBoard()[i][board.getSize() - 1 - i];
    }
    score += evaluateLine(board, mainDiagonal, playerSymbol);
    score += evaluateLine(board, reverseDiagonal, playerSymbol);

    return score;
  }

  private int evaluateLine(TicTacToeBoardModel board, char[] line, char playerSymbol) {
    int aiCount = 0;
    int opponentCount = 0;

    for (char cell : line) {
      if (cell == playerSymbol) {
        aiCount++;
      } else if (cell != ' ') {
        opponentCount++;
      }
    }

    if (aiCount == board.getSize() - 1 && opponentCount == 0) {
      return Integer.MAX_VALUE / 2;
    }

    if (opponentCount == board.getSize() - 1 && aiCount == 0) {
      return Integer.MIN_VALUE / 2;
    }

    int score = (int) Math.pow(10, aiCount);

    if (opponentCount == 0) {
      return score;
    }

    if (aiCount + opponentCount == line.length - 1) {
      score /= 2;
    }
    return score;
  }
}