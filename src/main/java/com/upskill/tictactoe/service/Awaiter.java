package com.upskill.tictactoe.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;

public class Awaiter {
  private final AtomicInteger state = new AtomicInteger(1);
  private final BlockingQueue<Integer> awaiter = new ArrayBlockingQueue<>(1);

  public void notifyUpdated() {
    awaiter.offer(state.incrementAndGet());
  }

  @SneakyThrows(InterruptedException.class)
  public int awaitUpdate(int lastState) {
    if (state.get() <= lastState) {
      awaiter.poll(10, TimeUnit.SECONDS);
    }
    return state.get();
  }
}
