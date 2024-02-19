package org.ElevatorSystem.Elevator.Interfaces;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;

public interface Elevator {
    int getId();

    int getCurrentFloor();

    int getDestinationFloor();

    ElevatorDirection getDirection();

    void addRequest(int floor);

    void addRequestInside(int floor);

    void makeStep();

    void update(int floor, ElevatorDirection direction);

    ElevatorStatus status();

    boolean isFloorInRange(int floor);
}
