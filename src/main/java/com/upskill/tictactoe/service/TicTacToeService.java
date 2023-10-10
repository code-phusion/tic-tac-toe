package com.upskill.tictactoe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.MessageModel;
import com.upskill.tictactoe.model.TicTacToeGameModel;

@Service
public class TicTacToeService {
  private static TicTacToeGameModel currentTicTacToeGameModel;

  public ResponseEntity<MessageModel> move(int row, int col) {
    if (currentTicTacToeGameModel.isGameOver() || currentTicTacToeGameModel.isDraw()) {
      return ResponseEntity.badRequest().body(new MessageModel("Game is already over."));
    }

    char currentPlayerSymbol = currentTicTacToeGameModel.getCurrentPlayerModel().getSymbol();

    if (currentTicTacToeGameModel.getBoard().makeMove(row, col, currentPlayerSymbol)) {
      if (currentTicTacToeGameModel.getBoard().isGameOver(row, col, currentPlayerSymbol)) {
        currentTicTacToeGameModel.setGameOver(true);
        return ResponseEntity.ok(new MessageModel(currentPlayerSymbol + " wins!"));
      } else if (currentTicTacToeGameModel.getBoard().isDraw()) {
        currentTicTacToeGameModel.setDraw(true);
        return ResponseEntity.ok(new MessageModel("It's a draw!"));
      } else {
        currentTicTacToeGameModel.setCurrentPlayerModel(
            (currentPlayerSymbol == 'X') ? currentTicTacToeGameModel.getPlayerModelO() : currentTicTacToeGameModel.getPlayerModelX()
        );
        return ResponseEntity.ok(new MessageModel("Move successful."));
      }
    } else {
      return ResponseEntity.badRequest().body(new MessageModel("Invalid move. Try again."));
    }
  }

  public ResponseEntity<MessageModel> startNewGame(int size) {
    currentTicTacToeGameModel = new TicTacToeGameModel(size);
    return ResponseEntity.ok(new MessageModel("New game started."));
  }

  public ResponseEntity<TicTacToeGameModel> getCurrentState() {
    return ResponseEntity.ok(currentTicTacToeGameModel);
  }

  public ResponseEntity<MessageModel> restartCurrentGame() {
    currentTicTacToeGameModel = new TicTacToeGameModel(currentTicTacToeGameModel.getBoard().getBoard().length);
    return ResponseEntity.ok(new MessageModel("Game restarted."));
  }
}
