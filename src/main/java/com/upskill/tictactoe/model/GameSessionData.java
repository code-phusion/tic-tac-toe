package com.upskill.tictactoe.model;

import com.upskill.tictactoe.service.Awaiter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSessionData {
  private TicTacToeGameModel gameModel;
  private Awaiter awaiter;
}
