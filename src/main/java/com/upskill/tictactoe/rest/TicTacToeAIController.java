package com.upskill.tictactoe.rest;

import com.upskill.tictactoe.dto.AIListResponse;
import com.upskill.tictactoe.model.MessageModel;
import com.upskill.tictactoe.service.TicTacToeAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class TicTacToeAIController {

  private final TicTacToeAIService ticTacToeAIService;

  @GetMapping
  public ResponseEntity<AIListResponse> getAIList() {
    return ResponseEntity.ok(AIListResponse.builder()
            .list(ticTacToeAIService.getList())
            .build());
  }

  @PostMapping("/{gameId}/move")
  public ResponseEntity<MessageModel> makeAIMove(@PathVariable String gameId) {
    return ticTacToeAIService.move(gameId);
  }

}
