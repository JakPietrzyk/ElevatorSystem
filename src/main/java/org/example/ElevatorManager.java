package org.example;

import constants.ElevatorSettings;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;

public class ElevatorManager implements Manager{
    private ArrayList<Elevator> elevators;
    private static int lastElevatorId;
    private int numberOfElevators;
    private int lowestPossibleFloor = ElevatorSettings.LOWEST_FLOOR_NUMBER;
    private int highestPossibleFloor = ElevatorSettings.HIGHEST_FLOOR_NUMBER;
    private ArrayList<ElevatorTask> waitingRequests;
    public ElevatorManager(int numberOfElevators)
    {
        this.elevators = new ArrayList<>();
        for(int i = 0; i < numberOfElevators; i++)
        {
            this.elevators.add(new Elevator(lastElevatorId++));
        }
        this.numberOfElevators = numberOfElevators;
        this.waitingRequests = new ArrayList<>();
    }

    public ArrayList<ElevatorStatus> status()
    {
        ArrayList<ElevatorStatus> result = new ArrayList<>();
        for(var elevator : this.elevators)
        {
            result.add(elevator.status());
        }
        return result;
    }

    @Override
    public void makeStep() {
        for(var elevator: this.elevators)
        {
            elevator.makeStep();
        }
    }

    @Override
    public void addRequest(int floor, ElevatorDirection direction) {
        if(!isRequestValid(floor))
            try {
                throw new InvalidAttributeValueException(); // do zmiany
            } catch (InvalidAttributeValueException e) {
                throw new RuntimeException(e);
            }
        for (Elevator elevator : elevators) {
            ElevatorDirection elevatorDirection = elevator.getDirection();
            if (elevatorDirection == ElevatorDirection.Idle
                    || (elevatorDirection == direction &&  elevator.IsFloorInRange(floor))) {
                elevator.addRequest(floor);
                break;
            }
        }
        this.waitingRequests.add(new ElevatorTask(floor, direction));
    }

    @Override
    public void update(int elevatorId, int floor, ElevatorDirection direction) {

    }

    private boolean isRequestValid(int currentFloor)
    {
        return currentFloor <= highestPossibleFloor
                && lowestPossibleFloor <= currentFloor;
    }
}
