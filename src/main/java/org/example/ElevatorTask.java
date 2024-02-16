package org.example;

public class ElevatorTask {
    private final int currentFloor;
    private final ElevatorDirection direction;
    public ElevatorTask(int currentFloor, ElevatorDirection direction) {
        this.currentFloor = currentFloor;
        this.direction = direction;
    }

    public ElevatorDirection getDirection() {
        return this.direction;
    }
}
