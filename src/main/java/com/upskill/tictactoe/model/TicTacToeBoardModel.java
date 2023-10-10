package com.upskill.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicTacToeBoardModel {
  private char[][] board;

  public TicTacToeBoardModel(int size) {
    this.board = new char[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        board[i][j] = ' ';
      }
    }
  }

  public boolean makeMove(int row, int col, char playerSymbol) {
    if (row < 0 || row >= board.length || col < 0 || col >= board.length || board[row][col] != ' ') {
      return false; // Invalid move
    }
    board[row][col] = playerSymbol;
    return true;
  }

  public boolean isGameOver(int row, int col, char playerSymbol) {
    if (checkHorizontalWin(row, col, playerSymbol)) {
      return true;
    }

    if (checkVerticalWin(row, col, playerSymbol)) {
      return true;
    }

    if (checkDiagonalWin(row, col, playerSymbol)) {
      return true;
    }

    if (checkReverseDiagonalWin(row, col, playerSymbol)) {
      return true;
    }

    return false;
  }

  public boolean isDraw() {
    for (char[] chars : board) {
      for (char aChar : chars) {
        if (aChar == ' ') {
          return false;
        }
      }
    }
    return true;
  }

  private boolean checkReverseDiagonalWin(int row, int col, char playerSymbol) {
    int count;
    count = 1;
    for (int i = row - 1, j = col + 1; i >= 0 && j < board[row].length && board[i][j] == playerSymbol; i--, j++) {
      count++;
    }
    for (int i = row + 1, j = col - 1; i < board.length && j >= 0 && board[i][j] == playerSymbol; i++, j--) {
      count++;
    }
    return count >= 3;
  }

  private boolean checkDiagonalWin(int row, int col, char playerSymbol) {
    int count;
    count = 1;
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0 && board[i][j] == playerSymbol; i--, j--) {
      count++;
    }
    for (int i = row + 1, j = col + 1; i < board.length && j < board[row].length && board[i][j] == playerSymbol; i++, j++) {
      count++;
    }
    return count >= 3;
  }

  private boolean checkVerticalWin(int row, int col, char playerSymbol) {
    int count;
    count = 1;
    for (int i = row - 1; i >= 0 && board[i][col] == playerSymbol; i--) {
      count++;
    }
    for (int i = row + 1; i < board.length && board[i][col] == playerSymbol; i++) {
      count++;
    }
    return count >= 3;
  }

  private boolean checkHorizontalWin(int row, int col, char playerSymbol) {
    int count = 1;
    for (int i = col - 1; i >= 0 && board[row][i] == playerSymbol; i--) {
      count++;
    }
    for (int i = col + 1; i < board[row].length && board[row][i] == playerSymbol; i++) {
      count++;
    }
    return count >= 3;
  }
}
