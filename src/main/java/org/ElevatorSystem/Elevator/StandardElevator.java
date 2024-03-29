package org.ElevatorSystem.Elevator;

import org.ElevatorSystem.Elevator.Interfaces.Elevator;
import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;
import org.ElevatorSystem.Managers.ElevatorQueueManager;
import org.ElevatorSystem.Managers.Interfaces.QueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.ElevatorSystem.Constants.ElevatorSettings.DEFAULT_HIGHEST_FLOOR_NUMBER;
import static org.ElevatorSystem.Constants.ElevatorSettings.DEFAULT_LOWEST_FLOOR_NUMBER;

public class StandardElevator implements Elevator {
    private static final Logger logger = LoggerFactory.getLogger(StandardElevator.class);
    private final int id;
    private final int highestPossibleFloor;
    private final int lowestPossibleFloor;

    private int currentFloor;
    private int destinationFloor;
    private ElevatorDirection direction;
    private final QueueManager tasks;

    public StandardElevator(int id) {
        this(id, DEFAULT_HIGHEST_FLOOR_NUMBER, DEFAULT_LOWEST_FLOOR_NUMBER, new ElevatorQueueManager());
    }

    public StandardElevator(int id, int highestPossibleFloor, int lowestPossibleFloor, QueueManager queueManager) {
        this.id = id;
        this.highestPossibleFloor = highestPossibleFloor;
        this.lowestPossibleFloor = lowestPossibleFloor;
        this.currentFloor = lowestPossibleFloor;
        this.destinationFloor = this.currentFloor;
        this.tasks = queueManager;
        this.direction = ElevatorDirection.Idle;
    }

    @Override
    public ElevatorStatus status() {
        return new ElevatorStatus(this.id, this.currentFloor, this.destinationFloor, this.direction);
    }

    @Override
    public String toString() {
        return "Elevator id: " + this.id + " current Floor: " + currentFloor + " destination floor: " + destinationFloor;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public int getDestinationFloor() {
        return this.destinationFloor;
    }

    public int getId() {
        return id;
    }

    @Override
    public void addRequest(int floor) {
        if (tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor) {
            this.direction = ElevatorDirection.Idle;
        }
        tasks.addRequest(floor, this.currentFloor, this.getDirection());
        if (this.direction == ElevatorDirection.Idle && !this.tasks.isEmptyQueue(this.direction)) {
            this.tasks.getNextDestinationFloor(this.direction).ifPresent(destination -> this.destinationFloor = destination);
            this.direction = (this.destinationFloor > this.currentFloor) ? ElevatorDirection.Up : ElevatorDirection.Down;
        } else {
            checkAndAdjustDestination(this.direction);
        }
    }

    private void checkAndAdjustDestination(ElevatorDirection direction) {
        Optional<Integer> mayBeNewDestination = this.tasks.peekNextDestinationFloor(direction);
        if (mayBeNewDestination.isEmpty())
            return;
        if (Math.abs(this.currentFloor - mayBeNewDestination.get()) < Math.abs(this.currentFloor - this.destinationFloor)) {
            this.tasks.addRequest(this.destinationFloor, this.currentFloor, this.direction);
            this.tasks.getNextDestinationFloor(this.direction).ifPresent(destination -> this.destinationFloor = destination);
        }
    }

    @Override
    public void addRequestInside(int floor) {
        if (tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor) {
            this.direction = ElevatorDirection.Idle;
        }
        tasks.addRequest(floor, this.currentFloor, this.direction);
        if (this.direction == ElevatorDirection.Idle && !this.tasks.isEmptyQueue(this.direction)) {
            this.tasks.getNextDestinationFloor(this.direction).ifPresent(destination -> this.destinationFloor = destination);
            this.direction = (this.destinationFloor > this.currentFloor) ? ElevatorDirection.Up : ElevatorDirection.Down;
        }
    }

    @Override
    public void makeStep() {
        if (this.currentFloor == this.destinationFloor) {
            if (this.direction != ElevatorDirection.Idle)
                logger.debug("Elevator id: " + this.id + " reached destination");

            if (!this.tasks.isEmptyQueue(this.direction)) {
                this.tasks.getNextDestinationFloor(this.direction).ifPresent(destination -> this.destinationFloor = destination);
                if (this.direction == ElevatorDirection.Idle) {
                    this.direction = determineDirection(this.currentFloor, this.destinationFloor);
                }
                logger.debug("Elevator id: " + this.id + " is setting new destination: " + this.destinationFloor + " " + this.direction);
                return;
            } else if (this.tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor && this.direction != ElevatorDirection.Idle) {
                this.direction = ElevatorDirection.Idle;
                logger.debug("Elevator id: " + this.id + " is now Idle");
            }
        }

        if (this.direction == ElevatorDirection.Up) {
            this.currentFloor++;
        } else if (this.direction == ElevatorDirection.Down) {
            this.currentFloor--;
        }
    }

    private ElevatorDirection determineDirection(int currentFloor, int destinationFloor) {
        return currentFloor < destinationFloor ? ElevatorDirection.Up : ElevatorDirection.Down;
    }

    @Override
    public boolean isFloorInRange(int floor) {
        switch (this.direction) {
            case Up -> {
                return this.highestPossibleFloor >= floor && floor >= this.currentFloor;
            }
            case Down -> {
                return this.currentFloor >= floor && floor >= this.lowestPossibleFloor;
            }
        }
        return true;
    }

    @Override
    public void update(int floor, ElevatorDirection direction) {
        this.currentFloor = floor;
        this.destinationFloor = this.currentFloor;
        this.direction = direction;
    }

}
