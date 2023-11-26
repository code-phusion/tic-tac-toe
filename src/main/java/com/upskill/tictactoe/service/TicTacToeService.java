package com.upskill.tictactoe.service;

import com.upskill.tictactoe.dto.GameIdResponse;
import com.upskill.tictactoe.dto.GameStateResponse;
import com.upskill.tictactoe.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@Service
@RequiredArgsConstructor
public class TicTacToeService {
  private final GameSessionService gameSessionService;
  private final MiniMaxAI miniMaxAI;
  private final BoardSizeValidatorService boardSizeValidatorService;
  private final String gameLogsFolder = "games-logs";

  public ResponseEntity<MessageModel> move(final String gameId, final int row, final int col) {
    final GameSessionData gameSessionData = gameSessionService.getGame(gameId);
    // TODO [WARNING] ticTacToeGameModel is not thread safe
    final TicTacToeGameModel ticTacToeGameModel = gameSessionData.getGameModel();

    if (ticTacToeGameModel.isGameOver() || ticTacToeGameModel.isDraw()) {
      return ResponseEntity.badRequest().body(new MessageModel("Game is already over."));
    }

    char currentPlayerSymbol = ticTacToeGameModel.getCurrentPlayerModel().getSymbol();

    log(gameId, "move," + currentPlayerSymbol + "," + row + "," + col);

    if (ticTacToeGameModel.getBoard().makeMove(row, col, currentPlayerSymbol)) {
      if (ticTacToeGameModel.getBoard().isDraw()) {
        log(gameId, "end,draw,,");
        ticTacToeGameModel.setDraw(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("It's a draw!"));
      }
      if (ticTacToeGameModel.getBoard().isGameOver(row, col, currentPlayerSymbol)) {
        log(gameId, "end,win," + currentPlayerSymbol + ",");
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

  private void log(String gameId, String message) {
    try {
      FileOutputStream fos = new FileOutputStream(new File(gameLogsFolder + "/" + gameId + ".log"), true);
      PrintWriter writer = new PrintWriter(fos);
      writer.println(message);
      writer.close();
    }
    // TODO: handle exception
    catch (IOException e) {}
  }

  public ResponseEntity<GameIdResponse> startNewGame(int size, boolean againstAI) {
    final ResponseEntity<GameIdResponse> body = boardSizeValidatorService.boardSizeValidation(size, againstAI);
    if (body != null) {
      return body;
    }
    final String gameSessionId = gameSessionService.newGame(new TicTacToeGameModel(size, againstAI));

    log(gameSessionId, "start," + size + ",,");

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

    // Record the start time before AI makes a move
    long startTime = System.currentTimeMillis();

    Move aiMove = miniMaxAI.calculateMove(boardModel, 'O');

    if (aiMove == null) {
      return ResponseEntity.badRequest().body(new MessageModel("AI couldn't find a valid move."));
    }

    int row = aiMove.getRow();
    int col = aiMove.getCol();

    if (boardModel.makeMove(row, col, 'O')) {
      // Calculate and log the time in seconds after the AI makes its move
      logTime(startTime);

      if (boardModel.isDraw()) {
        log(gameId, "end,draw,,");
        gameModel.setDraw(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("It's a draw!"));
      }

      if (boardModel.isGameOver(row, col, 'O')) {
        log(gameId, "end,win,O,");
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

  private void logTime(long startTime) {
    long endTime = System.currentTimeMillis();
    double seconds = (endTime - startTime) / 1000.0;
    System.out.println("Last AI move completed" + ": " + seconds + " seconds");
  }
}