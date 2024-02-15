package org.example;

public interface Manager {
    void makeStep();
    void addRequest(int floor, int direction);
    void update(int elevatorId, int floor, int direction);
}
