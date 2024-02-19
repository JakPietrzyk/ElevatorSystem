package org.ElevatorSystem.Elevator;
import org.ElevatorSystem.Elevator.Interfaces.Elevator;
import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorStatus;
import org.ElevatorSystem.Elevator.Models.ElevatorTask;
import org.ElevatorSystem.Managers.ElevatorQueueManager;
import org.ElevatorSystem.Constants.ElevatorSettings;
import org.ElevatorSystem.Managers.Interfaces.QueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class StandardElevator implements Elevator {
    private static final Logger logger = LoggerFactory.getLogger(StandardElevator.class);
    private final int id;
    private int currentFloor;
    private int destinationFloor;
    private ElevatorDirection direction;
    private QueueManager tasks;

    public StandardElevator(int id)
    {
        this(id, new ElevatorQueueManager());
    }

    public StandardElevator(int id, QueueManager queueManager)
    {
        this.id = id;
        this.currentFloor = ElevatorSettings.LOWEST_FLOOR_NUMBER;
        this.destinationFloor = this.currentFloor;
        this.tasks = queueManager;
        this.direction = ElevatorDirection.Idle;
    }
    public StandardElevator(int id, int currentFloor, QueueManager queueManager)
    {
        this(id, queueManager);
        this.currentFloor = currentFloor;
        this.destinationFloor = this.currentFloor;
    }
    @Override
    public ElevatorStatus status()
    {
        return new ElevatorStatus(this.id, this.currentFloor, this.destinationFloor, this.direction);
    }

    @Override
    public String toString() {
        return "Elevator id: " + this.id + " current Floor: " + currentFloor + " destination floor: " + destinationFloor;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public int getCurrentFloor()
    {
        return this.currentFloor;
    }
    public int getDestinationFloor()
    {
        return this.destinationFloor;
    }

    public int getId() {
        return id;
    }

    @Override
    public void addRequest(int floor)
    {
        if(tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor)
        {
            this.direction = ElevatorDirection.Idle;
        }
        tasks.addRequest(floor, this.currentFloor, this.getDirection());
        if(this.direction == ElevatorDirection.Idle && !this.tasks.isEmptyQueue(this.direction))
        {
            this.destinationFloor = this.tasks.getTask(this.direction);
            this.direction = (this.destinationFloor > this.currentFloor) ? ElevatorDirection.Up : ElevatorDirection.Down;
        }
        else
        {
            checkAndAdjustDestination(this.direction);
        }
    }

    private void checkAndAdjustDestination(ElevatorDirection direction)
    {
        int mayBeNewDestination;
        switch (direction)
        {
            case Up -> {
                if(this.tasks.isEmptyUpQueue()) return;
                mayBeNewDestination = this.tasks.peekTask(direction);
            }
            case Down -> {
                if(this.tasks.isEmptyDownQueue()) return;
                mayBeNewDestination = this.tasks.peekTask(direction);
            }
            default -> {
                return;
            }
        }

        if(Math.abs(this.currentFloor - mayBeNewDestination) < Math.abs(this.currentFloor - this.destinationFloor))
        {
            this.tasks.addRequest(this.destinationFloor, this.currentFloor, this.direction);
            this.destinationFloor = this.tasks.getTask(this.direction);
        }

    }

    @Override
    public void addRequestInside(int floor)
    {
        if(tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor)
        {
            this.direction = ElevatorDirection.Idle;
        }
        tasks.addRequest(floor, this.currentFloor, this.direction);
        if(this.direction == ElevatorDirection.Idle && !this.tasks.isEmptyQueue(this.direction))
        {
            this.destinationFloor = this.tasks.getTask(this.direction);
            this.direction = (this.destinationFloor > this.currentFloor) ? ElevatorDirection.Up : ElevatorDirection.Down;
        }
    }

    @Override
    public void makeStep(LinkedHashSet<ElevatorTask> waitingTasks) {
        if (this.currentFloor == this.destinationFloor) {
            if(this.direction != ElevatorDirection.Idle) logger.debug("Elevator id: " + this.id + " reached destination");

            if(this.direction != ElevatorDirection.Idle)
            {
                System.out.println("\tEnter destination floor number:");
                Scanner scanner = new Scanner(System.in);
                try{
                    int destinationFloor = scanner.nextInt();
                    logger.debug("Adding new floor from inside elevator: " + destinationFloor);
                    addRequestInside(destinationFloor);
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Wrong destination floor number");
                }
            }

            if (!this.tasks.isEmptyQueue(this.direction)) {
                this.destinationFloor = this.tasks.getTask(this.direction);
                logger.debug("Elevator id: " + this.id + " is setting new destination: " + this.destinationFloor + " " + this.direction);
                return;
            } else if (this.tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor && this.direction != ElevatorDirection.Idle){
                this.direction = ElevatorDirection.Idle;
                logger.debug("Elevator id: " + this.id + " is now Idle");
            }

            if(this.tasks.isEmptyQueue(this.direction) && !waitingTasks.isEmpty())
            {
                logger.debug("Elevator id: " + this.id + " finished queue, fetching waiting queue");
                this.direction = processWaitingTasks(waitingTasks);
                logger.debug("Elevator id: " + this.id + " new direction: " + this.direction);
            }
        }

        if (this.direction == ElevatorDirection.Up) {
            this.currentFloor++;
        } else if (this.direction == ElevatorDirection.Down) {
            this.currentFloor--;
        }
    }

    private ElevatorDirection processWaitingTasks(LinkedHashSet<ElevatorTask> waitingRequests)
    {
        if(waitingRequests.isEmpty()) return ElevatorDirection.Idle;

        var firstRequest = waitingRequests.removeFirst();
        this.direction = determineDirection(this.currentFloor, firstRequest.currentFloor());
        List<ElevatorTask> processedRequests = waitingRequests.stream()
                .filter(x -> x.direction() == this.direction && IsFloorInRange(x.currentFloor()))
                .peek(x -> addRequest(x.currentFloor()))
                .toList();

        processedRequests.forEach(waitingRequests::remove);
        addRequest(firstRequest.currentFloor());
        return this.direction;
    }
    private ElevatorDirection determineDirection(int currentFloor, int destinationFloor) {
        return currentFloor < destinationFloor ? ElevatorDirection.Up : ElevatorDirection.Down;
    }
    @Override
    public boolean IsFloorInRange(int floor)
    {
        switch (this.direction)
        {
            case Up -> {
                return ElevatorSettings.HIGHEST_FLOOR_NUMBER >= floor && floor >= this.currentFloor;
            }
            case Down ->
            {
                return this.currentFloor >= floor && floor >= ElevatorSettings.LOWEST_FLOOR_NUMBER;
            }
        }
        return true;
    }

    @Override
    public void update(int floor, ElevatorDirection direction)
    {
        this.currentFloor = floor;
        this.destinationFloor = this.currentFloor;
        this.direction = direction;
    }

}
