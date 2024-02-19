package org.ElevatorSystem.Controllers.Interfaces;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;

import java.util.ArrayList;

public interface ElevatorSystem {
    void pickup(int floor, ElevatorDirection direction);

    void addRequestInsideElevator(int elevatorId, int floor);

    void update(int elevatorId, int floor, ElevatorDirection direction);

    void step();

    ArrayList<ElevatorStatus> status();
}

