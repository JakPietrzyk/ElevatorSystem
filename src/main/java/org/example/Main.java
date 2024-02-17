package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting elevatorController");
        ElevatorController elevatorController = new ElevatorController(2);
        elevatorController.pickup(5, ElevatorDirection.Up);
        elevatorController.pickup(3, ElevatorDirection.Up);
        elevatorController.step();
        elevatorController.step();
        elevatorController.step();
        elevatorController.step();
        elevatorController.pickup(2, ElevatorDirection.Down);

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