package com.upskill.tictactoe.service;

import com.upskill.tictactoe.dto.GameIdResponse;
import com.upskill.tictactoe.dto.GameStateResponse;
import com.upskill.tictactoe.model.GameSessionData;
import com.upskill.tictactoe.model.MessageModel;
import com.upskill.tictactoe.model.TicTacToeGameModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicTacToeService {
  private final GameSessionService gameSessionService;
  private final BoardSizeValidatorService boardSizeValidatorService;
  private final GameLogService gameLogService;

  public ResponseEntity<MessageModel> move(final String gameId, final int row, final int col) {
    final GameSessionData gameSessionData = gameSessionService.getGame(gameId);
    // TODO [WARNING] ticTacToeGameModel is not thread safe
    final TicTacToeGameModel ticTacToeGameModel = gameSessionData.getGameModel();

    if (ticTacToeGameModel.isGameOver() || ticTacToeGameModel.isDraw()) {
      return ResponseEntity.badRequest().body(new MessageModel("Game is already over."));
    }

    char currentPlayerSymbol = ticTacToeGameModel.getCurrentPlayerModel().getSymbol();

    gameLogService.log(gameId, "move," + currentPlayerSymbol + "," + row + "," + col);

    if (ticTacToeGameModel.getBoard().makeMove(row, col, currentPlayerSymbol)) {
      if (ticTacToeGameModel.getBoard().isDraw()) {
        gameLogService.log(gameId, "end,draw,,", true);
        ticTacToeGameModel.setDraw(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("It's a draw!"));
      }
      if (ticTacToeGameModel.getBoard().isGameOver(row, col, currentPlayerSymbol)) {
        gameLogService.log(gameId, "end,win," + currentPlayerSymbol + ",", true);
        ticTacToeGameModel.setGameOver(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel(currentPlayerSymbol + " wins!"));
      } else {
        ticTacToeGameModel.setCurrentPlayerModel(
            (currentPlayerSymbol == 'X') ? ticTacToeGameModel.getPlayerModelO() : ticTacToeGameModel.getPlayerModelX()
        );
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("Move successful."));
      }
    } else {
      return ResponseEntity.badRequest().body(new MessageModel("Invalid move. Try again."));
    }
  }

  public ResponseEntity<GameIdResponse> startNewGame(int size, boolean againstAI, int winNumber, String aiId) {
    final ResponseEntity<GameIdResponse> body = boardSizeValidatorService.boardSizeValidation(size);
    if (body != null) {
      return body;
    }

    final String gameSessionId = gameSessionService.newGame(new TicTacToeGameModel(size, againstAI, winNumber, aiId));

    gameLogService.log(gameSessionId, "start," + size + "," + winNumber + "," + (againstAI ? aiId : ""));

    return ResponseEntity.ok(new GameIdResponse(gameSessionId));
  }

  public ResponseEntity<GameStateResponse> getCurrentState(final String gameId, final int stateId) {
    final GameSessionData gameSessionData = gameSessionService.getGame(gameId);
    final int newStateId = gameSessionData.getAwaiter().awaitUpdate(stateId);
    final TicTacToeGameModel ticTacToeGameModel = gameSessionData.getGameModel();
    return ResponseEntity.ok(new GameStateResponse(ticTacToeGameModel, newStateId));
  }

  public ResponseEntity<MessageModel> clearBoard(final String gameId) {
    final GameSessionData gameSessionData = gameSessionService.getGame(gameId);
    final TicTacToeGameModel ticTacToeGameModel = gameSessionData.getGameModel();
    gameLogService.log(gameId, "start," + gameSessionData.getGameModel().getBoard().getSize()
            + "," + gameSessionData.getGameModel().getWinNumber()
            + "," + (gameSessionData.getGameModel().isAgainstAI() ? gameSessionData.getGameModel().getAiId() : ""));
    gameSessionService.updateGame(gameId, new TicTacToeGameModel(
            ticTacToeGameModel.getBoard().getBoard().length,
            ticTacToeGameModel.isAgainstAI(),
            ticTacToeGameModel.getWinNumber(),
            ticTacToeGameModel.getAiId()));
    gameSessionData.getAwaiter().notifyUpdated();
    return ResponseEntity.ok(new MessageModel("Game restarted."));
  }
}
