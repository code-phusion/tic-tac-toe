package com.upskill.tictactoe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.upskill.tictactoe.dto.GameIdResponse;

@Service
public class BoardSizeValidatorService {
  public ResponseEntity<GameIdResponse> boardSizeValidation(int size, boolean againstAI) {
    if (againstAI && size != 3) {
      return ResponseEntity.badRequest().body(new GameIdResponse("Sorry. AI for now only supports 3x3 board."));
    }
    if (size < 3) {
      return ResponseEntity.badRequest().body(new GameIdResponse("Please enter a size of 3 or more."));
    }

    if (size > 50) {
      return ResponseEntity.badRequest().body(new GameIdResponse("Please enter a size of 50 or less."));
    }
    return null;
  }
}