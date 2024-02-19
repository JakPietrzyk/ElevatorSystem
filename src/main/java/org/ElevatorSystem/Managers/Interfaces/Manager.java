package org.ElevatorSystem.Managers.Interfaces;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;

import java.util.ArrayList;

public interface Manager {
    void makeStep();

    void addRequest(int floor, ElevatorDirection direction);

    void update(int elevatorId, int floor, ElevatorDirection direction);

    ArrayList<ElevatorStatus> status();
}
