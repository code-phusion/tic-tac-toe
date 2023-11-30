package com.upskill.tictactoe.service.ai;

import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

@Service
public class ImprovedMiniMaxAI implements AIInterface {
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

  /**
   * Evaluates the current state of the TicTacToe board for the AI player.
   */
  private int evaluate(TicTacToeBoardModel board, char aiSymbol) {
    // Check if the AI player has won
    if (board.isWinning(aiSymbol)) {
      return Integer.MAX_VALUE - MAX_DEPTH;
    }
    // Check if the opponent has won
    else if (board.isWinning((aiSymbol == 'X') ? 'O' : 'X')) {
      return Integer.MIN_VALUE + MAX_DEPTH;
    }

    else if (board.isDraw()) {
      return 0;
    }

    // Evaluate scores for the AI player and the opponent
    int aiScore = evaluatePlayer(board, aiSymbol);
    int opponentScore = evaluatePlayer(board, (aiSymbol == 'X') ? 'O' : 'X');

    // Calculate the final score by subtracting the opponent's score from the AI player's score
    return aiScore - opponentScore;
  }

  /**
   * Evaluates the score for a specific player based on rows, columns, and diagonals.
   * @return The evaluation score for the player.
   */
  private int evaluatePlayer(TicTacToeBoardModel board, char playerSymbol) {
    int score = 0;

    // Evaluate scores for each row
    for (int row = 0; row < board.getSize(); row++) {
      score += evaluateLine(board, board.getBoard()[row], playerSymbol);
    }

    // Evaluate scores for each column
    for (int col = 0; col < board.getSize(); col++) {
      char[] column = new char[board.getSize()];
      for (int row = 0; row < board.getSize(); row++) {
        column[row] = board.getBoard()[row][col];
      }
      score += evaluateLine(board, column, playerSymbol);
    }

    // Evaluate scores for main and reverse diagonals
    char[] mainDiagonal = new char[board.getSize()];
    char[] reverseDiagonal = new char[board.getSize()];
    for (int i = 0; i < board.getSize(); i++) {
      mainDiagonal[i] = board.getBoard()[i][i];
      reverseDiagonal[i] = board.getBoard()[i][board.getSize() - 1 - i];
    }

    // Added check for additional diagonals in evaluateLine() method.
    score += evaluateLine(board, mainDiagonal, playerSymbol);
    score += evaluateLine(board, reverseDiagonal, playerSymbol);

    return score;
  }

  /**
   * Evaluates the score for a specific line (row, column, or diagonal) on the TicTacToe board.
   * @param line The line (row, column, or diagonal) to be evaluated.
   * @return The evaluation score for the line.
   */
  private int evaluateLine(TicTacToeBoardModel board, char[] line, char playerSymbol) {
    int aiCount = 0;
    int opponentCount = 0;

    // Count occurrences of player's symbol and opponent's symbol in the line
    for (char cell : line) {
      if (cell == playerSymbol) {
        aiCount++;
      } else if (cell != ' ') {
        opponentCount++;
      }
    }

    // Check if the player is about to win
    if (aiCount == board.getSize() - 1 && opponentCount == 0) {
      return Integer.MAX_VALUE / 2;
    }

    // Check if the opponent is about to win
    if (opponentCount == board.getSize() - 1 && aiCount == 0) {
      return Integer.MIN_VALUE / 2;
    }

    // Assign a score based on the number of player's symbols in the line
    int score = (int) Math.pow(10, aiCount);

    // Adjust the score based on the presence of opponent's symbols in the line
    if (opponentCount == 0) {
      return score;
    }

    // If there are only two cells left to win, reduce the score
    if (aiCount + opponentCount == line.length - 1) {
      score /= 2;
    }

    // Additional diagonals
    int mainDiagonalCount = 0;
    int reverseDiagonalCount = 0;

    // Count occurrences of player's symbol in additional diagonals
    for (int i = 0; i < line.length; i++) {
      if (line[i] == playerSymbol) {
        mainDiagonalCount += (i == line.length - 1 - i) ? 2 : 1;
        reverseDiagonalCount += (i == line.length - 1 - i) ? 2 : 1;
      }
    }

    // Check if the player is about to win in additional diagonals
    if (mainDiagonalCount == board.getSize() - 1 && reverseDiagonalCount == 0) {
      return Integer.MAX_VALUE / 2;
    }

    // Check if the opponent is about to win in additional diagonals
    if (reverseDiagonalCount == board.getSize() - 1 && mainDiagonalCount == 0) {
      return Integer.MIN_VALUE / 2;
    }

    // Assign scores based on the number of player's symbols in additional diagonals
    score += (int) Math.pow(10, mainDiagonalCount);
    score += (int) Math.pow(10, reverseDiagonalCount);

    return score;
  }

  @Override
  public int getMaxBoardSize() {
    return 7;
  }

  @Override
  public int getWinNumber() {
    return 5;
  }

  @Override
  public String getId() {
    return "upgradedMiniMax";
  }

  @Override
  public String getName() {
    return "MiniMax with Alpha-Beta pruning and evaluation function for big boards (7x7 board max, 5 in row to win)";
  }
}

