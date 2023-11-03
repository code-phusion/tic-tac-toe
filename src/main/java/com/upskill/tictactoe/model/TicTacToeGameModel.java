package com.upskill.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicTacToeGameModel {
  private TicTacToeBoardModel board;
  private PlayerModel playerModelX;
  private PlayerModel playerModelO;
  private PlayerModel currentPlayerModel;
  private boolean gameOver;
  private boolean isDraw;
  private boolean againstAI;

  public TicTacToeGameModel(int size, boolean againstAI) {
    this.board = new TicTacToeBoardModel(size);
    this.playerModelX = new PlayerModel('X');
    this.playerModelO = new PlayerModel('O');
    this.currentPlayerModel = playerModelX;
    this.gameOver = false;
    this.isDraw = false;
    this.againstAI = againstAI;
  }
}