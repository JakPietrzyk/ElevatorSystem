package org.example;

public class ElevatorStatus {
    private final int id;
    private final int currentFloor;
    private final int destinationFloor;

    public ElevatorStatus(int id, int currentFloor, int destinationFloor)
    {
        this.id = id;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    @Override
    public String toString() {
        return "[id: " + this.id + " current floor: " + this.currentFloor + " destination floor: " + this.destinationFloor + "]";
    }
}
