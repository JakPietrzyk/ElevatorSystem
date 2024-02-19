package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;

public interface QueueManager {
    void addRequest(int floor, int currentElevatorFloor, ElevatorDirection elevatorDirection);
    Integer getTask(ElevatorDirection direction);
    Integer peekTask(ElevatorDirection direction);
    boolean isEmptyQueue(ElevatorDirection direction);
    boolean isEmptyUpQueue();
    boolean isEmptyDownQueue();
}
