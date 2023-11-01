package com.upskill.tictactoe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.upskill.tictactoe.dto.GameIdResponse;
import com.upskill.tictactoe.dto.GameStateResponse;
import com.upskill.tictactoe.model.Move;
import com.upskill.tictactoe.model.GameSessionData;
import com.upskill.tictactoe.model.MessageModel;
import com.upskill.tictactoe.model.TicTacToeBoardModel;
import com.upskill.tictactoe.model.TicTacToeGameModel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicTacToeService {
  private final GameSessionService gameSessionService;
  private final MiniMaxAI miniMaxAI;
  private final BoardSizeValidatorService boardSizeValidatorService;

  public ResponseEntity<MessageModel> move(final String gameId, final int row, final int col) {
    final GameSessionData gameSessionData = gameSessionService.getGame(gameId);
    // TODO [WARNING] ticTacToeGameModel is not thread safe
    final TicTacToeGameModel ticTacToeGameModel = gameSessionData.getGameModel();

    if (ticTacToeGameModel.isGameOver() || ticTacToeGameModel.isDraw()) {
      return ResponseEntity.badRequest().body(new MessageModel("Game is already over."));
    }

    char currentPlayerSymbol = ticTacToeGameModel.getCurrentPlayerModel().getSymbol();

    if (ticTacToeGameModel.getBoard().makeMove(row, col, currentPlayerSymbol)) {
      if (ticTacToeGameModel.getBoard().isDraw()) {
        ticTacToeGameModel.setDraw(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("It's a draw!"));
      }
      if (ticTacToeGameModel.getBoard().isGameOver(row, col, currentPlayerSymbol)) {
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

  public ResponseEntity<GameIdResponse> startNewGame(int size, boolean againstAI) {
    final ResponseEntity<GameIdResponse> body = boardSizeValidatorService.boardSizeValidation(size, againstAI);
    if (body != null) {
      return body;
    }
    final String gameSessionId = gameSessionService.newGame(new TicTacToeGameModel(size, againstAI));
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
    gameSessionService.updateGame(gameId, new TicTacToeGameModel(ticTacToeGameModel.getBoard().getBoard().length, ticTacToeGameModel.isAgainstAI()));
    gameSessionData.getAwaiter().notifyUpdated();
    return ResponseEntity.ok(new MessageModel("Game restarted."));
  }

  public ResponseEntity<MessageModel> makeAIMove(final String gameId) {
    final GameSessionData gameSessionData = gameSessionService.getGame(gameId);
    TicTacToeGameModel gameModel = gameSessionData.getGameModel();

    if (gameModel.isGameOver()) {
      return ResponseEntity.badRequest().body(new MessageModel("Game is already over."));
    }

    if (gameModel.isDraw()) {
      return ResponseEntity.badRequest().body(new MessageModel("It's a draw!"));
    }

    TicTacToeBoardModel boardModel = gameModel.getBoard();
    Move aiMove = miniMaxAI.calculateMove(boardModel, 'O');

    if (aiMove == null) {
      return ResponseEntity.badRequest().body(new MessageModel("AI couldn't find a valid move."));
    }

    int row = aiMove.getRow();
    int col = aiMove.getCol();

    if (boardModel.makeMove(row, col, 'O')) {
      if (boardModel.isDraw()) {
        gameModel.setDraw(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("It's a draw!"));
      }
      if (boardModel.isGameOver(row, col, 'O')) {
        gameModel.setGameOver(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("O wins!"));
      } else {
        gameModel.setCurrentPlayerModel(gameModel.getPlayerModelX());
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("AI move successful."));
      }
    } else {
      return ResponseEntity.badRequest().body(new MessageModel("AI made an invalid move."));
    }
  }
}
