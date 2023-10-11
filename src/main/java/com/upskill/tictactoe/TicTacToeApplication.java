package com.upskill.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicTacToeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeApplication.class, args);
  }

}

// TODO [igor] Multigame

// TODO Possibility to play with the game

// TODO [board] Restart game (and clear)
// TODO [board] Display winner (Х or 0);
// TODO [board] Configurable Win row size (now 3 in a row).
// TODO [board] Validation: board size >= win row size
// TODO [board] Validation: min board size 3x3
// TODO [board] Validation: max board size? (to avoid overload issue)
// TODO [board] Visualize crossing winner row
// TODO [board] Replace isDraw -> isGameOver
