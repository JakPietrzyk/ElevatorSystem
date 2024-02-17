package org.example;
import constants.ElevatorSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

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
        return new ElevatorStatus(this.id, this.currentFloor, this.destinationFloor); //tutaj zastanowić się czy na pewno to dobra struktura
    }

    @Override
    public String toString() {
        return "Elevator id: " + this.id + " current Floor: " + currentFloor + " destination floor: " + destinationFloor;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public LinkedList<Integer> getTasks() {
        return this.tasks.getTasks();
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
        if(tasks.isEmptyQueue() && this.currentFloor == this.destinationFloor)
        {
            this.direction = ElevatorDirection.Idle;
        }
        tasks.addRequestInside(floor, this.currentFloor, this.getDirection());
        if(this.direction == ElevatorDirection.Idle)
        {
            this.destinationFloor = this.tasks.getTask();
            this.direction = (this.destinationFloor > this.currentFloor) ? ElevatorDirection.Up : ElevatorDirection.Down;
        }
    }

    public void addRequestInside(int floor)
    {
        if(tasks.isEmptyQueue() && this.currentFloor == this.destinationFloor)
        {
            this.direction = ElevatorDirection.Idle;
        }
        tasks.addRequestInside(floor, this.currentFloor,this.direction);
        if(this.direction == ElevatorDirection.Idle)
        {
            this.destinationFloor = this.tasks.getTask();
            this.direction = (this.destinationFloor > this.currentFloor) ? ElevatorDirection.Up : ElevatorDirection.Down;
        }
    }

    public void makeStep() {
        if (this.currentFloor == this.destinationFloor) {
            logger.debug("Elevator id: " + this.id + "reached destination");
            if(this.tasks.isEmptyQueue())
            {

            }
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

            if (!this.tasks.isEmptyQueue()) {
                this.destinationFloor = this.tasks.getTask();
                logger.debug("Elevator id: " + this.id + "is setting new destination: " + this.destinationFloor);
            } else if (this.tasks.isEmptyQueue() && this.currentFloor == this.destinationFloor){
                this.direction = ElevatorDirection.Idle;
                logger.debug("Elevator id: " + this.id + "is now Idle");
            }
        }

        if (this.direction == ElevatorDirection.Up) {
            this.currentFloor++;
        } else if (this.direction == ElevatorDirection.Down) {
            this.currentFloor--;
        }
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

}
