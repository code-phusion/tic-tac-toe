package com.upskill.tictactoe.dto;

import com.upskill.tictactoe.model.TicTacToeGameModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStateResponse {
  private TicTacToeGameModel game;
  private int stateId;
}
