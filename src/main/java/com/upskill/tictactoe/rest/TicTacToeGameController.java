package com.upskill.tictactoe.rest;

import com.upskill.tictactoe.dto.GameIdResponse;
import com.upskill.tictactoe.dto.GameStateResponse;
import com.upskill.tictactoe.model.MessageModel;
import com.upskill.tictactoe.service.TicTacToeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class TicTacToeGameController {

  private final TicTacToeService ticTacToeService;

  @PostMapping("/new")
  public ResponseEntity<GameIdResponse> startNewGame(@RequestParam int size, @RequestParam boolean againstAI,
                                                     @RequestParam int winNumber, @RequestParam String aiId) {
    return ticTacToeService.startNewGame(size, againstAI, winNumber, aiId);
  }

  @PostMapping("/{gameId}/move")
  public ResponseEntity<MessageModel> makeMove(@PathVariable String gameId, @RequestParam int row, @RequestParam int col) {
    return ticTacToeService.move(gameId, row, col);
  }

  @GetMapping("/{gameId}/state/{stateId}")
  public ResponseEntity<GameStateResponse> getGameState(@PathVariable String gameId, @PathVariable int stateId) {
    return ticTacToeService.getCurrentState(gameId, stateId);
  }

  @PostMapping("/{gameId}/clear-board")
  public ResponseEntity<MessageModel> clearBoard(@PathVariable String gameId) {
    return ticTacToeService.clearBoard(gameId);
  }

}
