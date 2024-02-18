package org.example;
import constants.ElevatorSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Elevator {
    private static final Logger logger = LoggerFactory.getLogger(Elevator.class);
    private final int id;
    private int currentFloor;
    private int destinationFloor;
    private ElevatorDirection direction;
    private ElevatorQueueManager tasks;
    public Elevator(int id)
    {
        this.id = id;
        this.currentFloor = ElevatorSettings.LOWEST_FLOOR_NUMBER;
        this.destinationFloor = this.currentFloor;
        this.tasks = new ElevatorQueueManager();
        this.direction = ElevatorDirection.Idle;
    }
    public Elevator(int id, int currentFloor)
    {
        this(id);
        this.currentFloor = currentFloor;
        this.destinationFloor = this.currentFloor;
    }
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

    public PriorityQueue<Integer> getTasks(ElevatorDirection direction) {
        return this.tasks.getTasks(direction);
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

    public void addRequest(int floor)
    {
        if(tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor)
        {
            this.direction = ElevatorDirection.Idle;
        }
        tasks.addRequestInside(floor, this.currentFloor, this.getDirection());
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

    public void checkAndAdjustDestination(ElevatorDirection direction)
    {
        var elevatorQueue = this.tasks.getTasks(direction);
        if(elevatorQueue.isEmpty()) return;
        int mayBeNewDestination = elevatorQueue.peek();
        if(Math.abs(this.currentFloor - mayBeNewDestination) < Math.abs(this.currentFloor - this.destinationFloor))
        {
            this.tasks.addRequestInside(this.destinationFloor, this.currentFloor, this.direction);
            this.destinationFloor = this.tasks.getTask(this.direction);
        }

    }

    public void addRequestInside(int floor)
    {
        if(tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor)
        {
            this.direction = ElevatorDirection.Idle;
        }
        tasks.addRequestInside(floor, this.currentFloor, this.direction);
        if(this.direction == ElevatorDirection.Idle && !this.tasks.isEmptyQueue(this.direction))
        {
            this.destinationFloor = this.tasks.getTask(this.direction);
            this.direction = (this.destinationFloor > this.currentFloor) ? ElevatorDirection.Up : ElevatorDirection.Down;
        }
    }

    public void makeStep(ArrayList<ElevatorTask> waitingTasks) {
        if (this.currentFloor == this.destinationFloor) {
            if(this.direction != ElevatorDirection.Idle) logger.debug("Elevator id: " + this.id + " reached destination");
            if(this.tasks.isEmptyQueue(this.direction) && !waitingTasks.isEmpty())
            {
                logger.debug("Elevator id: " + this.id + " finished queue, fetching waiting queue");
                this.direction = processWaitingTasks(waitingTasks);
                logger.debug("Elevator id: " + this.id + " new direction: " + this.direction);
            }
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
            } else if (this.tasks.isEmptyQueue(this.direction) && this.currentFloor == this.destinationFloor && this.direction != ElevatorDirection.Idle){
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

    private ElevatorDirection processWaitingTasks(ArrayList<ElevatorTask> waitingRequests)
    {
        if(waitingRequests.isEmpty()) return ElevatorDirection.Idle;

        var firstRequest = waitingRequests.getFirst();
        this.direction = determineDirection(this.currentFloor, firstRequest.getCurrentFloor());
        waitingRequests.stream()
                .filter(x -> x.getDirection() == this.direction
                        && IsFloorInRange(x.getCurrentFloor()))
                .forEach(x -> addRequest(x.getCurrentFloor()));
        if(this.direction != firstRequest.getDirection())
        {
            addRequest(firstRequest.getCurrentFloor());
        }
        return this.direction;
    }
    private ElevatorDirection determineDirection(int currentFloor, int destinationFloor) {
        return currentFloor < destinationFloor ? ElevatorDirection.Up : ElevatorDirection.Down;
    }
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
        return true; // idle
    }

    public void update(int floor, ElevatorDirection direction)
    {
        this.currentFloor = floor;
        this.destinationFloor = this.currentFloor;
        this.direction = direction;
    }

}
