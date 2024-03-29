package com.upskill.tictactoe.service;

import com.upskill.tictactoe.model.*;
import com.upskill.tictactoe.service.ai.AIInterface;
import com.upskill.tictactoe.service.ai.ImprovedMiniMaxAI;
import com.upskill.tictactoe.service.ai.MiniMaxAI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicTacToeAIService {
  private final GameSessionService gameSessionService;
  private final GameLogService gameLogService;

  //TODO: Our algos for AI support playing for X and for O. Will fix in the future.
  private final char aiPlayerSymbol = 'O';

  private final HashMap<String, AIInterface> aiHashMap = new HashMap<>(){{
    AIInterface minimax = new MiniMaxAI();
    AIInterface improvedMiniMax = new ImprovedMiniMaxAI();
    put(minimax.getId(), minimax);
    put(improvedMiniMax.getId(), improvedMiniMax);
    // Register new AI algorithms here
  }};

  public ResponseEntity<MessageModel> move(final String gameId) {
    AIInterface ai = getAIById(gameSessionService.getGame(gameId).getGameModel().getAiId());
    final GameSessionData gameSessionData = gameSessionService.getGame(gameId);
    TicTacToeGameModel gameModel = gameSessionData.getGameModel();

    if (gameModel.isGameOver()) {
      return ResponseEntity.badRequest().body(new MessageModel("Game is already over."));
    }

    if (gameModel.isDraw()) {
      return ResponseEntity.badRequest().body(new MessageModel("It's a draw!"));
    }

    TicTacToeBoardModel boardModel = gameModel.getBoard();
    Move aiMove = ai.calculateMove(boardModel, aiPlayerSymbol);

    if (aiMove == null) {
      return ResponseEntity.badRequest().body(new MessageModel("AI couldn't find a valid move."));
    }

    int row = aiMove.getRow();
    int col = aiMove.getCol();

    gameLogService.log(gameId, "move," + aiPlayerSymbol + "," + row + "," + col);

    if (boardModel.makeMove(row, col, aiPlayerSymbol)) {
      if (boardModel.isDraw()) {
        gameLogService.log(gameId, "end,draw,,", true);
        gameModel.setDraw(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("It's a draw!"));
      }
      if (boardModel.isGameOver(row, col, aiPlayerSymbol)) {
        gameLogService.log(gameId, "end,win," + aiPlayerSymbol + ",", true);
        gameModel.setGameOver(true);
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel(aiPlayerSymbol + " wins!"));
      } else {
        gameModel.setCurrentPlayerModel(gameModel.getPlayerModelX());
        gameSessionData.getAwaiter().notifyUpdated();
        return ResponseEntity.ok(new MessageModel("AI move successful."));
      }
    } else {
      return ResponseEntity.badRequest().body(new MessageModel("AI made an invalid move."));
    }
  }

  private AIInterface getAIById(String id) {
    return aiHashMap.get(id);
  }

  public List<AIModel> getList() {
    List<AIModel> list = new ArrayList<>();
    aiHashMap.forEach((String id, AIInterface ai) -> list.add(
            AIModel.builder()
                    .id(ai.getId())
                    .name(ai.getName())
                    .maxBoardSize(ai.getMaxBoardSize())
                    .winNumber(ai.getWinNumber())
                    .build())
    );
    return list;
  }
}
