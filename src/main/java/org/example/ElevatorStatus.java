package org.example;

public class ElevatorStatus {
    private final int id;
    private final int currentFloor;
    private final int destinationFloor;
    private final ElevatorDirection direction;

    public ElevatorStatus(int id, int currentFloor, int destinationFloor, ElevatorDirection direction)
    {
        this.id = id;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public ElevatorDirection getDirection()
    {
        return this.direction;
    }


    @Override
    public String toString() {
        return "[id: " + this.id + " current floor: " + this.currentFloor + " destination floor: " + this.destinationFloor + "]";
    }
}
