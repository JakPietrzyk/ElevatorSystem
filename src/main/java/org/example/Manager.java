package org.example;

public interface Manager {
    void makeStep();
    void addRequest(int floor, ElevatorDirection direction);
    void update(int elevatorId, int floor, ElevatorDirection direction);
}
