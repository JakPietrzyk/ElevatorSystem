package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ElevatorController elevatorController = new ElevatorController(3);
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