package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong {
    private static final Logger logger = LoggerFactory.getLogger(PingPong.class);
    private int nextThreadIndex = 0;
    private final int threadCount = 3;

    private synchronized void action(int threadIndex) {
        int value = 1;
        int direction = 1;

        while (!Thread.currentThread().isInterrupted()) {
            try {

                while (threadIndex != nextThreadIndex) {
                    this.wait();
                }

                if (value >= 10) {
                    direction = -1;
                } else if (value <= 1) {
                    direction = 1;
                }
                logger.info("{}", value);
                value += direction;

                if (++nextThreadIndex >= threadCount) {
                    nextThreadIndex = 0;
                }

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        for (int i = 0; i < pingPong.threadCount; i++) {
            int finalI = i;
            new Thread(() -> pingPong.action(finalI)).start();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
