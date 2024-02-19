package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Elevator.Interfaces.Elevator;
import org.ElevatorSystem.Elevator.StandardElevator;
import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorTask;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;
import org.ElevatorSystem.Constants.ElevatorSettings;
import org.ElevatorSystem.Managers.Interfaces.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ElevatorManager implements Manager {
    private static final Logger logger = LoggerFactory.getLogger(ElevatorManager.class);
    private ArrayList<Elevator> elevators;
    private static int lastElevatorId;
    private int numberOfElevators;
    private int lowestPossibleFloor = ElevatorSettings.LOWEST_FLOOR_NUMBER;
    private int highestPossibleFloor = ElevatorSettings.HIGHEST_FLOOR_NUMBER;
    private LinkedHashSet<ElevatorTask> waitingRequests;
    public ElevatorManager(int numberOfElevators)
    {
        this.elevators = new ArrayList<>();
        for(int i = 0; i < numberOfElevators; i++)
        {
            this.elevators.add(new StandardElevator(lastElevatorId++, new ElevatorQueueManager()));
        }
        this.numberOfElevators = numberOfElevators;
        this.waitingRequests = new LinkedHashSet<>();
    }

    @Override
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
        if (!isRequestValid(floor))
        {
            logger.error("Invalid floor: " + floor + " not in range: "
                    + ElevatorSettings.LOWEST_FLOOR_NUMBER + "-" + ElevatorSettings.HIGHEST_FLOOR_NUMBER);
            return;
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
        if (!isRequestValid(floor))
        {
            logger.error("Invalid floor: " + floor + " not in range: "
                    + ElevatorSettings.LOWEST_FLOOR_NUMBER + "-" + ElevatorSettings.HIGHEST_FLOOR_NUMBER);
            return;
        }
        try{
            Elevator elevator = getElevatorById(elevatorId);
            elevator.addRequestInside(floor);
        }
        catch (IllegalStateException ex)
        {
            logger.error("Invalid elevator id: " + elevatorId);
        }
    }


    @Override
    public void update(int elevatorId, int floor, ElevatorDirection direction) {
        try{
            getElevatorById(elevatorId).update(floor, direction);
        }
        catch (IllegalStateException ex)
        {
            logger.error("Invalid elevator id: " + elevatorId + " update method failed");
        }
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

    public boolean isWaitingRequestsEmpty()
    {
        return this.waitingRequests.isEmpty();
    }
}
