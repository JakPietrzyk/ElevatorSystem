package org.example;

public class Main {
    public static void main(String[] args) {
        ElevatorController elevatorController = new ElevatorController(3);
        elevatorController.step();
        elevatorController.pickup(1, 3);
        while(true)
        {
            elevatorController.step();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}