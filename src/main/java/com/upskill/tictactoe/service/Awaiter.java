package com.upskill.tictactoe.service;

import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

public class Awaiter {
  private final Object semaphore = new Object();
  private int state = 1;

  public void notifyUpdated() {
    synchronized (semaphore) {
      state++;
      semaphore.notifyAll();
    }
  }

  @SneakyThrows(InterruptedException.class)
  public int awaitUpdate(int lastState) {
    synchronized (semaphore) {
      if (state <= lastState) {
        semaphore.wait(TimeUnit.SECONDS.toMillis(10));
      }
      return state;
    }
  }
}
