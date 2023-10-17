package com.upskill.tictactoe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.upskill.tictactoe.model.GameIdResponse;
import com.upskill.tictactoe.model.MessageModel;
import com.upskill.tictactoe.model.TicTacToeGameModel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicTacToeService {
  private final GameSessionService gameSessionService;

  public ResponseEntity<MessageModel> move(final String gameId, final int row, final int col) {
    // TODO [WARNING] ticTacToeGameModel is not thread safe
    final TicTacToeGameModel ticTacToeGameModel = gameSessionService.getGame(gameId);

    if (ticTacToeGameModel.isGameOver() || ticTacToeGameModel.isDraw()) {
      return ResponseEntity.badRequest().body(new MessageModel("Game is already over."));
    }

    char currentPlayerSymbol = ticTacToeGameModel.getCurrentPlayerModel().getSymbol();

    if (ticTacToeGameModel.getBoard().makeMove(row, col, currentPlayerSymbol)) {
      if (ticTacToeGameModel.getBoard().isGameOver(row, col, currentPlayerSymbol)) {
        ticTacToeGameModel.setGameOver(true);
        return ResponseEntity.ok(new MessageModel(currentPlayerSymbol + " wins!"));
      } else if (ticTacToeGameModel.getBoard().isDraw()) {
        ticTacToeGameModel.setDraw(true);
        return ResponseEntity.ok(new MessageModel("It's a draw!"));
      } else {
        ticTacToeGameModel.setCurrentPlayerModel(
            (currentPlayerSymbol == 'X') ? ticTacToeGameModel.getPlayerModelO() : ticTacToeGameModel.getPlayerModelX()
        );
        return ResponseEntity.ok(new MessageModel("Move successful."));
      }
    } else {
      return ResponseEntity.badRequest().body(new MessageModel("Invalid move. Try again."));
    }
  }

  public ResponseEntity<GameIdResponse> startNewGame(int size) {
    final String gameSessionId = gameSessionService.newGame(new TicTacToeGameModel(size));
    return ResponseEntity.ok(new GameIdResponse(gameSessionId));
  }

  public ResponseEntity<TicTacToeGameModel> getCurrentState(final String gameId) {
    final TicTacToeGameModel ticTacToeGameModel = gameSessionService.getGame(gameId);
    return ResponseEntity.ok(ticTacToeGameModel);
  }

  public ResponseEntity<MessageModel> restartCurrentGame(final String gameId) {
    final TicTacToeGameModel ticTacToeGameModel = gameSessionService.getGame(gameId);
    gameSessionService.updateGame(gameId, new TicTacToeGameModel(ticTacToeGameModel.getBoard().getBoard().length));
    return ResponseEntity.ok(new MessageModel("Game restarted."));
  }
}
