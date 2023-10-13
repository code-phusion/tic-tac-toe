package com.upskill.tictactoe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upskill.tictactoe.dto.GameIdResponse;
import com.upskill.tictactoe.dto.GameStateResponse;
import com.upskill.tictactoe.model.MessageModel;
import com.upskill.tictactoe.service.TicTacToeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class TicTacToeGameController {

  private final TicTacToeService ticTacToeService;

  @PostMapping("/new")
  public ResponseEntity<GameIdResponse> startNewGame(@RequestParam int size) {
    return ticTacToeService.startNewGame(size);
  }

  @PostMapping("/{gameId}/move")
  public ResponseEntity<MessageModel> makeMove(@PathVariable String gameId, @RequestParam int row, @RequestParam int col) {
    return ticTacToeService.move(gameId, row, col);
  }

  @GetMapping("/{gameId}/state/{stateId}")
  public ResponseEntity<GameStateResponse> getGameState(@PathVariable String gameId, @PathVariable int stateId) {
    return ticTacToeService.getCurrentState(gameId, stateId);
  }

  @PostMapping("/{gameId}/restart")
  public ResponseEntity<MessageModel> restartCurrentGame(@PathVariable String gameId) {
    return ticTacToeService.restartCurrentGame(gameId);
  }
}
