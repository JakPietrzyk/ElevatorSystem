package org.example;

public class ElevatorTask {
    private final int currentFloor;
    private final int destinationFloor;
    public ElevatorTask(int currentFloor, int destinationFloor) {
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }
}
