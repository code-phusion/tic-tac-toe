package com.upskill.tictactoe.service;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import com.upskill.tictactoe.GameNotFoundException;
import com.upskill.tictactoe.model.GameSessionData;
import com.upskill.tictactoe.model.TicTacToeGameModel;

@Service
public class GameSessionService {
  // TODO Invalidate inactive game sessions
  private final Map<String, GameSessionData> sessionStorage = new ConcurrentHashMap<>();

  public String newGame(TicTacToeGameModel ticTacToeGameModel) {
    final String gameId = generateGameId();
    sessionStorage.put(gameId, new GameSessionData(ticTacToeGameModel, new Awaiter()));
    return gameId;
  }

  private static String generateGameId() {
    final UUID uuid = UUID.randomUUID();
    final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
    byteBuffer.putLong(uuid.getMostSignificantBits());
    byteBuffer.putLong(uuid.getLeastSignificantBits());
    return new String(new Base32().encode(byteBuffer.array()))
        .replace("=", "");
  }

  public GameSessionData getGame(final String gameId) {
    final GameSessionData gameSessionData = sessionStorage.get(gameId);
    if (gameSessionData == null) {
      throw new GameNotFoundException();
    }
    return gameSessionData;
  }

  public void updateGame(final String gameId, TicTacToeGameModel ticTacToeGameModel) {
    getGame(gameId).setGameModel(ticTacToeGameModel);
  }
}
