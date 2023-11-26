package com.upskill.tictactoe.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameLogService {
  private final String gamesLogsFolder = "games-logs";
  private final Map<String, List<String>> gamesLogs = new HashMap<>();

  public void log(String gameId, String message) {
    log(gameId, message, false);
  }

  public void log(String gameId, String message, boolean save) {
    List<String> logs = gamesLogs.get(gameId);
    if (logs == null) {
      logs = new ArrayList<>();
    }
    logs.add(message);
    gamesLogs.put(gameId, logs);

    if (save) {
      saveLogs(gameId);
    }
  }

  @SneakyThrows(IOException.class)
  private void saveLogs(String gameId) {
    List<String> logs = gamesLogs.get(gameId);
    if (logs != null) {
      PrintWriter writer = new PrintWriter(gamesLogsFolder + "/" + gameId + ".log");
      logs.forEach(logLine -> writer.println(logLine));
      writer.close();
      gamesLogs.remove(gameId);
    }
  }
}
