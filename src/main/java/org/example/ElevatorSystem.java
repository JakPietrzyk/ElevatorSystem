package org.example;

import java.util.ArrayList;

public interface ElevatorSystem {
    void pickup(int floor, ElevatorDirection direction);
    void update(int elevatorId, int floor, ElevatorDirection direction);
    void step();
    ArrayList<ElevatorStatus> status();
}

