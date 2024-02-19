package org.ElevatorSystem.Elevator.Interfaces;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;
import org.ElevatorSystem.Elevator.Models.ElevatorTask;

import java.util.ArrayList;
import java.util.LinkedList;

public interface Elevator {
    int getId();
    int getCurrentFloor();
    int getDestinationFloor();
    ElevatorDirection getDirection();
    void addRequest(int floor);
    void addRequestInside(int floor);
    void makeStep(LinkedList<ElevatorTask> waitingTasks);
    void update(int floor, ElevatorDirection direction);
    public ElevatorStatus status();
    boolean IsFloorInRange(int floor);
}
