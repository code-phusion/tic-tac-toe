package com.upskill.tictactoe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.upskill.tictactoe.dto.GameIdResponse;

@Service
public class BoardSizeValidatorService {
  public ResponseEntity<GameIdResponse> boardSizeValidation(int size) {
    if (size > 50) {
      return ResponseEntity.badRequest().body(new GameIdResponse("Please enter a size of 50 or less."));
    }
    return null;
  }
}