package com.upskill.tictactoe.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AwaiterTest {

  private static final int THREADS = 100;

  @Test
  void testAwaiter() throws InterruptedException {
    final Awaiter awaiter = new Awaiter();

    final AtomicInteger runningThreads = new AtomicInteger();
    final AtomicInteger blockedThreads = new AtomicInteger();

    final int lastState = awaiter.awaitUpdate(0);

    ExecutorService executorService = Executors.newCachedThreadPool();
    try {
      CountDownLatch countDownLatch = new CountDownLatch(THREADS);
      for (int i = 0; i < THREADS; i++) {
        runningThreads.incrementAndGet();
        executorService.submit(() -> {
          countDownLatch.countDown();
          final long executionTime = measure(() -> awaiter.awaitUpdate(lastState));
          if (executionTime >= 1000) {
            blockedThreads.incrementAndGet();
          }
          runningThreads.decrementAndGet();
        });
      }
      countDownLatch.await();
      executorService.submit(awaiter::notifyUpdated);
    } finally {
      executorService.shutdown();
      final boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
      if (!terminated) {
        executorService.shutdownNow();
      }
    }
    assertThat(blockedThreads.get()).as("There are blocked threads").isZero();
    assertThat(runningThreads.get()).as("There are running threads").isZero();
  }

  private long measure(Runnable runnable) {
    final long start = System.currentTimeMillis();
    final long end;
    try {
      runnable.run();
    } finally {
      end = System.currentTimeMillis();
    }
    return end - start;
  }
}
