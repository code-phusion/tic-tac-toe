package com.upskill.tictactoe.model;

import lombok.Data;

@Data
public class TicTacToeBoardModel {
  private char[][] board;
  private int size;
  private int winNumber;
  private Move lastMove;

  public TicTacToeBoardModel(int size, int winNumber) {
    this.size = size;
    this.winNumber = winNumber;
    this.lastMove = Move.builder()
            .row(-1)
            .col(-1)
            .build();
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
      return false;
    }
    board[row][col] = playerSymbol;
    this.lastMove.setRow(row);
    this.lastMove.setCol(col);
    return true;
  }

  public boolean isGameOver(int row, int col, char playerSymbol) {
    return checkingAllWinConditions(row, col, playerSymbol) || isDraw();
  }

  public boolean isWinning(char playerSymbol) {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (board[i][j] == playerSymbol) {
          if (checkingAllWinConditions(i, j, playerSymbol)) {
            return true;
          }
        }
      }
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
    int count = 1;
    for (int i = row - 1, j = col + 1; i >= 0 && j < board[row].length && board[i][j] == playerSymbol; i--, j++) {
      count++;
    }
    for (int i = row + 1, j = col - 1; i < board.length && j >= 0 && board[i][j] == playerSymbol; i++, j--) {
      count++;
    }
    return count >= winNumber;
  }

  private boolean checkDiagonalWin(int row, int col, char playerSymbol) {
    int count = 1;
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0 && board[i][j] == playerSymbol; i--, j--) {
      count++;
    }
    for (int i = row + 1, j = col + 1; i < board.length && j < board[row].length && board[i][j] == playerSymbol; i++, j++) {
      count++;
    }
    return count >= winNumber;
  }

  private boolean checkVerticalWin(int row, int col, char playerSymbol) {
    int count = 1;
    for (int i = row - 1; i >= 0 && board[i][col] == playerSymbol; i--) {
      count++;
    }
    for (int i = row + 1; i < board.length && board[i][col] == playerSymbol; i++) {
      count++;
    }
    return count >= winNumber;
  }

  private boolean checkHorizontalWin(int row, int col, char playerSymbol) {
    int count = 1;
    for (int i = col - 1; i >= 0 && board[row][i] == playerSymbol; i--) {
      count++;
    }
    for (int i = col + 1; i < board[row].length && board[row][i] == playerSymbol; i++) {
      count++;
    }
    return count >= winNumber;
  }

  public boolean checkingAllWinConditions(int row, int col, char playerSymbol) {
    return checkHorizontalWin(row, col, playerSymbol) ||
        checkVerticalWin(row, col, playerSymbol) ||
        checkDiagonalWin(row, col, playerSymbol) ||
        checkReverseDiagonalWin(row, col, playerSymbol);
  }
}