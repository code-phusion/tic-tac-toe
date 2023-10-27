package com.upskill.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicTacToeBoardModel {
  private char[][] board;
  private int size;

  public TicTacToeBoardModel(int size) {
    this.size = size;
    board = new char[size][size];
    initializeBoard();
  }

  private void initializeBoard() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        board[i][j] = ' ';
      }
    }
  }

  public boolean isEmpty(int row, int col) {
    return board[row][col] == ' ';
  }

  public void undoMove(int row, int col) {
    board[row][col] = ' ';
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

  public char getWinner() {
    for (int i = 0; i < board.length; i++) {
      if (checkHorizontalWin(i, 0, board[i][0])) {
        return board[i][0];
      }
    }

    for (int i = 0; i < board[0].length; i++) {
      if (checkVerticalWin(0, i, board[0][i])) {
        return board[0][i];
      }
    }

    if (checkDiagonalWin(0, 0, board[0][0])) {
      return board[0][0];
    }

    if (checkReverseDiagonalWin(0, board[0].length - 1, board[0][board[0].length - 1])) {
      return board[0][board[0].length - 1];
    }

    return ' ';
  }

  public boolean isGameOver() {
    for (int i = 0; i < board.length; i++) {
      if (checkHorizontalWin(i, 0, board[i][0])) {
        return true;
      }
    }

    for (int i = 0; i < board[0].length; i++) {
      if (checkVerticalWin(0, i, board[0][i])) {
        return true;
      }
    }

    if (checkDiagonalWin(0, 0, board[0][0])) {
      return true;
    }

    if (checkReverseDiagonalWin(0, board[0].length - 1, board[0][board[0].length - 1])) {
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
    return count >= 5;
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
    return count >= 5;
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
    return count >= 5;
  }

  private boolean checkHorizontalWin(int row, int col, char playerSymbol) {
    int count = 1;
    for (int i = col - 1; i >= 0 && board[row][i] == playerSymbol; i--) {
      count++;
    }
    for (int i = col + 1; i < board[row].length && board[row][i] == playerSymbol; i++) {
      count++;
    }
    return count >= 5;
  }
}
