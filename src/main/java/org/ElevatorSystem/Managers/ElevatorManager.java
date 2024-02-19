package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Elevator.Elevator;
import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorTask;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;
import org.ElevatorSystem.Constants.ElevatorSettings;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.Optional;

public class ElevatorManager implements Manager {
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
            this.elevators.add(new Elevator(lastElevatorId++, new ElevatorQueueManager()));
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
            elevator.makeStep(this.waitingRequests);
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
            if (elevatorDirection == direction &&  elevator.IsFloorInRange(floor)) {
                elevator.addRequest(floor);
                return;
            }
        }

        Optional<Elevator> bestElevator = getBestElevator(floor);
        if (bestElevator.isPresent()) {
            bestElevator.get().addRequest(floor);
        } else {
            this.waitingRequests.add(new ElevatorTask(floor, direction));
        }
    }

    private Optional<Elevator> getBestElevator(int floor) {
        Elevator bestElevator = null;
        int lowestDistance = Integer.MAX_VALUE;
        for(Elevator elevator :elevators)
        {
            if(elevator.getDirection() == ElevatorDirection.Idle)
            {
                int distance = Math.abs(floor - elevator.getCurrentFloor());
                if(lowestDistance > distance)
                {
                    lowestDistance = distance;
                    bestElevator = elevator;
                }
            }
        }

        return Optional.ofNullable(bestElevator);
    }

    public void addRequestInsideElevator(int elevatorId ,int floor)
    {
        if(!isRequestValid(floor))
            try {
                throw new InvalidAttributeValueException();
            } catch (InvalidAttributeValueException e) {
                throw new RuntimeException(e);
            }

        Elevator elevator = getElevatorById(elevatorId);
        elevator.addRequestInside(floor);
    }


    @Override
    public void update(int elevatorId, int floor, ElevatorDirection direction) {
        getElevatorById(elevatorId).update(floor, direction);
    }

    private Elevator getElevatorById(int elevatorId) {
        return elevators.stream()
                .filter(elevator -> elevator.getId() == elevatorId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid elevatorId"));
    }

    private boolean isRequestValid(int currentFloor)
    {
        return currentFloor <= highestPossibleFloor
                && lowestPossibleFloor <= currentFloor;
    }
}
