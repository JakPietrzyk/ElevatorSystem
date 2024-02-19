package org.ElevatorSystem.Managers.Interfaces;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;

import java.util.Optional;

public interface QueueManager {
    void addRequest(int floor, int currentElevatorFloor, ElevatorDirection elevatorDirection);

    Optional<Integer> getNextDestinationFloor(ElevatorDirection direction);

    Optional<Integer> peekNextDestinationFloor(ElevatorDirection direction);

    boolean isEmptyQueue(ElevatorDirection direction);
}
