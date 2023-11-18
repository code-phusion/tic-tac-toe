package com.upskill.tictactoe.service;

import com.upskill.tictactoe.ai.MiniMaxAI;
import com.upskill.tictactoe.model.*;
import com.upskill.tictactoe.service.ai.AIInterface;
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

  private HashMap<String, AIInterface> aiHashMap = new HashMap<>(){{
    AIInterface minimax = new MiniMaxAI();
    put(minimax.getId(), minimax);
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
    Move aiMove = ai.calculateMove(boardModel, 'O');

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
