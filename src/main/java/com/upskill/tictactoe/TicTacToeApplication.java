package com.upskill.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicTacToeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeApplication.class, args);
  }

}

// TODO Possibility to play with different game engines (including different CPU algorythms, self, online)

// TODO [board] Restart game (and clear)
// TODO [board] Display winner (Х or 0);
// TODO [board] Configurable Win row size (now 3 in a row).
// TODO [board] Validation: board size >= win row size
// TODO [board] Validation: min board size 3x3
// TODO [board] Validation: max board size? (to avoid overload issue)
// TODO [board] Visualize crossing winner row
// TODO [board] Replace isDraw -> isGameOver
// TODO [board] Improve UI rendering with big board size
// TODO [board] Add possibility to choose X or 0 for player
// TODO [board] Add history of moves with possibility to go back to any move like in chess

// TODO [gui] Warning: Do not use Array index in keys (TicTacToe.js)

// TODO [multiplayer] Distinguish X or 0 player
// TODO [multiplayer] Long pooling (Awaiter) -> WebSocket
// TODO [multiplayer] stateId -> moveCount
// TODO [online] Add possibility to create room with game [with room_id and password]
// TODO [online] Add possibility to join room with game [with room_id and password]
// TODO [online] Add possibility to play with other player [probably we will need to change our API]
// TODO [online] Add list of available rooms

// TODO [AI] Create AI using algorithms like: MinMax or AlphaBeta [we can discuss what to use]
// TODO [AI] Add possibility to play with AI
// TODO [AI] Add possibility to choose AI level

// TODO [FUTURE-IDEAS] Implement AI move history (for debugging and analysis)
// TODO [FUTURE-IDEAS] Implement AI learning capabilities
// TODO [FUTURE-IDEAS] Optimize AI algorithms for better performance
// TODO [FUTURE-IDEAS] Implement AI vs AI matches for testing and development purposes
// TODO [FUTURE-IDEAS] Add AI vs Human mode with adjustable difficulty levels
// TODO [FUTURE-IDEAS] Create an AI training mode to improve AI skills over time
// TODO [FUTURE-IDEAS] Implement AI self-improvement mechanisms based on player feedback
// TODO [FUTURE-IDEAS] Investigate neural network-based AI approaches for more advanced gameplay
// TODO [FUTURE-IDEAS] Implement tournament mode for AI vs AI matches and for human players
