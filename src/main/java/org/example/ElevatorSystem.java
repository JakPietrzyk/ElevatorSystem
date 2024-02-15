package org.example;

import java.util.ArrayList;

public interface ElevatorSystem {
    void pickup(int floor, int direction);
    void update(int elevatorId, int floor, int direction);
    void step();
    ArrayList<ArrayList<Integer>> status();
}

