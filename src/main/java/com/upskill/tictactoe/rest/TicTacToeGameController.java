package com.upskill.tictactoe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upskill.tictactoe.model.MessageModel;
import com.upskill.tictactoe.model.TicTacToeGameModel;
import com.upskill.tictactoe.service.TicTacToeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class TicTacToeGameController {

  private final TicTacToeService ticTacToeService;

  @PostMapping("/new")
  public ResponseEntity<MessageModel> startNewGame(@RequestParam int size) {
    return ticTacToeService.startNewGame(size);
  }

  @PostMapping("/move")
  public ResponseEntity<MessageModel> makeMove(@RequestParam int row, @RequestParam int col) {
    return ticTacToeService.move(row, col);
  }

  @GetMapping("/state")
  public ResponseEntity<TicTacToeGameModel> getGameState() {
    return ticTacToeService.getCurrentState();
  }

  @PostMapping("/restart")
  public ResponseEntity<MessageModel> restartCurrentGame() {
    return ticTacToeService.restartCurrentGame();
  }
}
