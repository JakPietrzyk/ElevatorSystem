package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ElevatorController elevatorController = new ElevatorController(3);
        elevatorController.step();
        elevatorController.pickup(1, ElevatorDirection.Up);
        elevatorController.pickup(5, ElevatorDirection.Up);
        elevatorController.pickup(3, ElevatorDirection.Up);

        Thread elevatorThread = new Thread(() -> {
            while (true) {
                elevatorController.step();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        elevatorThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\tEnter floor number:");
            int floor = scanner.nextInt();
            System.out.println("\tEnter direction (1 for Up, -1 for Down):");
            int directionValue = scanner.nextInt();
            ElevatorDirection direction = (directionValue > 0) ? ElevatorDirection.Up : ElevatorDirection.Down;
            elevatorController.pickup(floor, direction);
        }
    }

}