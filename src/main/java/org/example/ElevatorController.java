package org.example;

import java.util.ArrayList;

public class ElevatorController implements ElevatorSystem{
    private final ElevatorManager elevatorManager;

    public ElevatorController(int numberOfElevators)
    {
        elevatorManager = new ElevatorManager(numberOfElevators);
    }
    @Override
    public void pickup(int floor, int direction) {
        elevatorManager.addRequest(floor, direction);
    }

    @Override
    public void update(int elevatorId, int floor, int direction) {
        elevatorManager.update(elevatorId, floor, direction);
    }

    @Override
    public void step() {
        elevatorManager.makeStep();
        System.out.println(elevatorManager.status());
    }

    @Override
    public ArrayList<ArrayList<Integer>> status() {
        return  elevatorManager.status();
    }
}
