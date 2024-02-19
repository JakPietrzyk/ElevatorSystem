package org.ElevatorSystem.Elevator.Models;

public record ElevatorStatus(int id, int currentFloor, int destinationFloor, ElevatorDirection direction) {
    @Override
    public String toString() {
        return "[id: " + this.id + " current floor: " + this.currentFloor + " destination floor: " + this.destinationFloor + "]";
    }
}
