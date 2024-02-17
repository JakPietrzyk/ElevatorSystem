package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting elevatorController");
        ElevatorController elevatorController = new ElevatorController(2);
        elevatorController.pickup(1, ElevatorDirection.Up);
        elevatorController.pickup(5, ElevatorDirection.Up);
        elevatorController.pickup(3, ElevatorDirection.Up);

        Thread elevatorThread = new Thread(() -> {
            while (true) {
                elevatorController.step();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        elevatorThread.start();
    }

}