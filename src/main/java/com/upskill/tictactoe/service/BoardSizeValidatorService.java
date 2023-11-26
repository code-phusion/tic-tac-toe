package com.upskill.tictactoe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.upskill.tictactoe.dto.GameIdResponse;

@Service
public class BoardSizeValidatorService {
  public ResponseEntity<GameIdResponse> boardSizeValidation(int size, boolean againstAI) {
    if (againstAI && (size < 5 || size > 8)) {
      return ResponseEntity.badRequest().body(new GameIdResponse("Sorry. AI currently only supports board sizes between 5x5 and 8x8."));
    }
    if (size < 5) {
      return ResponseEntity.badRequest().body(new GameIdResponse("Please enter a size of 3 or more."));
    }

    if (size > 50) {
      return ResponseEntity.badRequest().body(new GameIdResponse("Please enter a size of 50 or less."));
    }
    return null;
  }
}