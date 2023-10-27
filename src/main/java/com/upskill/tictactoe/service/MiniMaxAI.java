package com.upskill.tictactoe.service;

import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.TicTacToeBoardModel;

@Service
public class MiniMaxAI {
  public Move calculateMove(TicTacToeBoardModel board, char aiSymbol) {
    int[] bestMove = minimax(board, aiSymbol, aiSymbol, Integer.MIN_VALUE, Integer.MAX_VALUE);
    return new Move(bestMove[0], bestMove[1]);
  }

  private int[] minimax(TicTacToeBoardModel board, char playerSymbol, char aiSymbol, int alpha, int beta) {
    // Base case: Check if the game is over or depth limit reached
    if (board.isGameOver() || board.isDraw()) {
      int score = evaluate(board, aiSymbol);
      return new int[] { -1, -1, score };
    }

    int[] bestMove = new int[] { -1, -1 };
    int bestScore = (playerSymbol == aiSymbol) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    // Iterate through empty cells
    for (int row = 0; row < board.getSize(); row++) {
      for (int col = 0; col < board.getSize(); col++) {
        if (board.isEmpty(row, col)) {
          // Simulate the move
          board.makeMove(row, col, playerSymbol);

          // Recursively call minimax for the opponent
          int[] result = minimax(board, (playerSymbol == 'X') ? 'O' : 'X', aiSymbol, alpha, beta);

          // Undo the move
          board.undoMove(row, col);

          int score = result[2];

          // Update best move and score based on player's turn
          if (playerSymbol == aiSymbol) {
            if (score > bestScore) {
              bestScore = score;
              bestMove[0] = row;
              bestMove[1] = col;
              alpha = Math.max(alpha, bestScore);
            }
          } else {
            if (score < bestScore) {
              bestScore = score;
              bestMove[0] = row;
              bestMove[1] = col;
              beta = Math.min(beta, bestScore);
            }
          }

          // Alpha-beta pruning
          if (beta <= alpha) {
            break;
          }
        }
      }
    }

    bestMove[2] = bestScore;
    return bestMove;
  }

  private int evaluate(TicTacToeBoardModel board, char aiSymbol) {
    // Simple evaluation function: Assign scores based on winning, losing, or drawing states.
    if (board.getWinner() == aiSymbol) {
      return 10;
    } else if (board.getWinner() != ' ') {
      return -10;
    }
    return 0; // A draw or game still in progress
  }
}