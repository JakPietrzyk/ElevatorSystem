package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ElevatorController implements ElevatorSystem{
    private static final Logger logger = LoggerFactory.getLogger(ElevatorController.class);
    private final ElevatorManager elevatorManager;

    public ElevatorController(int numberOfElevators)
    {
        elevatorManager = new ElevatorManager(numberOfElevators);
    }
    @Override
    public void pickup(int floor, ElevatorDirection direction) {
        elevatorManager.addRequest(floor, direction);
    }

    @Override
    public void update(int elevatorId, int floor, ElevatorDirection direction) {
        elevatorManager.update(elevatorId, floor, direction);
    }

    public void addRequestInsideElevator(int elevatorId, int floor)
    {
        elevatorManager.addRequestInsideElevator(elevatorId, floor);
    }

    @Override
    public void step() {
        logger.info("makeStep Invoked");
        elevatorManager.makeStep();
        System.out.println(elevatorManager.status());
    }

    @Override
    public ArrayList<ElevatorStatus> status() {
        return  elevatorManager.status();
    }
}
