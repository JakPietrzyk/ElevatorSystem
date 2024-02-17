package org.example;

import constants.ElevatorSettings;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.Optional;

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
                throw new InvalidAttributeValueException();
            } catch (InvalidAttributeValueException e) {
                throw new RuntimeException(e);
            }
        for (Elevator elevator : elevators) {
            ElevatorDirection elevatorDirection = elevator.getDirection();
            if (elevatorDirection == ElevatorDirection.Idle
                    || (elevatorDirection == direction &&  elevator.IsFloorInRange(floor))) {
                elevator.addRequest(floor);
                return;
            }
        }
        this.waitingRequests.add(new ElevatorTask(floor, direction));
    }

    public void addRequestInsideElevator(int elevatorId ,int floor)
    {
        if(!isRequestValid(floor))
            try {
                throw new InvalidAttributeValueException();
            } catch (InvalidAttributeValueException e) {
                throw new RuntimeException(e);
            }

        Optional<Elevator> elevatorOptional = elevators.stream()
                .filter(elevator -> elevator.getId() == elevatorId)
                .findFirst();
        if (elevatorOptional.isPresent()) {
            Elevator elevator = elevatorOptional.get();
            elevator.addRequestInside(floor);
        } else {
            throw new RuntimeException("Invalid elevatorId");
        }
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
