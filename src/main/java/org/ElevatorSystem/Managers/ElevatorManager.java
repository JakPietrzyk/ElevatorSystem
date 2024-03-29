package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Elevator.Interfaces.Elevator;
import org.ElevatorSystem.Elevator.StandardElevator;
import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorTask;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;
import org.ElevatorSystem.Managers.Interfaces.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ElevatorManager implements Manager {
    private static final Logger logger = LoggerFactory.getLogger(ElevatorManager.class);
    private final ArrayList<Elevator> elevators;
    private static int lastElevatorId;
    private final int lowestPossibleFloor;
    private final int highestPossibleFloor;
    private LinkedHashSet<ElevatorTask> waitingRequests;

    public ElevatorManager(int numberOfElevators, int lowestPossibleFloor, int highestPossibleFloor) {
        this.elevators = new ArrayList<>();
        for (int i = 0; i < numberOfElevators; i++) {
            this.elevators.add(new StandardElevator(lastElevatorId++, highestPossibleFloor, lowestPossibleFloor, new ElevatorQueueManager()));
        }
        this.waitingRequests = new LinkedHashSet<>();
        this.lowestPossibleFloor = lowestPossibleFloor;
        this.highestPossibleFloor = highestPossibleFloor;
    }

    @Override
    public ArrayList<ElevatorStatus> status() {
        ArrayList<ElevatorStatus> result = new ArrayList<>();
        for (var elevator : this.elevators) {
            result.add(elevator.status());
        }
        return result;
    }

    @Override
    public void makeStep() {
        for (var elevator : this.elevators) {
            elevator.makeStep();
        }
        var previousWaitingRequests = this.waitingRequests;
        this.waitingRequests = new LinkedHashSet<>();
        previousWaitingRequests
                .forEach(request -> addRequest(request.currentFloor(), request.direction()));
    }

    @Override
    public void addRequest(int floor, ElevatorDirection direction) {
        if (!isRequestValid(floor)) {
            logger.error("Invalid floor: " + floor + " not in range: "
                    + lowestPossibleFloor + " : " + highestPossibleFloor);
            return;
        }
        for (Elevator elevator : elevators) {
            ElevatorDirection elevatorDirection = elevator.getDirection();
            if (elevatorDirection == direction && elevator.isFloorInRange(floor)) {
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
        for (Elevator elevator : elevators) {
            if (elevator.getDirection() == ElevatorDirection.Idle) {
                int distance = Math.abs(floor - elevator.getCurrentFloor());
                if (lowestDistance > distance) {
                    lowestDistance = distance;
                    bestElevator = elevator;
                }
            }
        }

        return Optional.ofNullable(bestElevator);
    }

    public void addRequestInsideElevator(int elevatorId, int floor) {
        if (!isRequestValid(floor)) {
            logger.error("Invalid floor: " + floor + " not in range: "
                    + lowestPossibleFloor + " : " + highestPossibleFloor);
            return;
        }
        try {
            Elevator elevator = getElevatorById(elevatorId);
            elevator.addRequestInside(floor);
        } catch (IllegalStateException ex) {
            logger.error("Invalid elevator id: " + elevatorId);
        }
    }


    @Override
    public void update(int elevatorId, int floor, ElevatorDirection direction) {
        try {
            getElevatorById(elevatorId).update(floor, direction);
        } catch (IllegalStateException ex) {
            logger.error("Invalid elevator id: " + elevatorId + " update method failed");
        }
    }

    private Elevator getElevatorById(int elevatorId) {
        return elevators.stream()
                .filter(elevator -> elevator.getId() == elevatorId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid elevatorId"));
    }

    private boolean isRequestValid(int currentFloor) {
        return currentFloor <= highestPossibleFloor
                && lowestPossibleFloor <= currentFloor;
    }
}
