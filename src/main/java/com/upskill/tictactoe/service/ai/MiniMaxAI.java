package com.upskill.tictactoe.service.ai;

import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

/*
* Minimax algorithm for Tic Tac Toe
* For now only supports 3x3 boars. Will fix it in the future. Probably with other algo.
* */
@Service
public class MiniMaxAI implements AIInterface {
  @Override
  public Move calculateMove(TicTacToeBoardModel board, char aiSymbol) {
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;
    char playerSymbol = (aiSymbol == 'X') ? 'O' : 'X';

    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          int score = makeMoveAndMinimaxAndReturnScore(board, row, col, aiSymbol, 0, false, aiSymbol, playerSymbol);
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
          char symbolToUse = isMaximizing ? aiSymbol : playerSymbol;
          int score = makeMoveAndMinimaxAndReturnScore(board, row, col, symbolToUse, depth + 1, !isMaximizing, aiSymbol, playerSymbol);
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

  private int makeMoveAndMinimaxAndReturnScore(TicTacToeBoardModel board, int row, int col, char symbol, int depth, boolean isMaximizing,
                                               char aiSymbol, char playerSymbol) {
    board.makeMove(row, col, symbol);
    int score = minimax(board, depth, isMaximizing, aiSymbol, playerSymbol);
    board.undoMove(row, col);
    return score;
  }

  @Override
  public int getMaxBoardSize() {
    return 3;
  }

  @Override
  public int getWinNumber() {
    return 3;
  }

  @Override
  public String getId() {
    return "minimax";
  }

  @Override
  public String getName() {
    return "MiniMax (3x3 board max, 3 in row to win)";
  }
}