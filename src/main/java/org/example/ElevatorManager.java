package org.example;

import constants.ElevatorSettings;

import java.util.ArrayList;

public class ElevatorManager implements Manager{
    private ArrayList<Elevator> elevators;
    private static int lastElevatorId;
    private int numberOfElevators;
    private int lowestPossibleFloor = ElevatorSettings.LOWEST_FLOOR_NUMBER;
    private int highestPossibleFloor = ElevatorSettings.HIGHEST_FLOOR_NUMBER;
    private ArrayList<ArrayList<Integer>> waitingRequests;
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

    public ArrayList<ArrayList<Integer>> status()
    {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
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
    public void addRequest(int floor, int direction) {
        if(!isRequestValid(floor))
            return; // do zmiany
        for (Elevator elevator : elevators) {
            int elevatorDirection = elevator.getDirection();
            if (elevatorDirection == 0 || elevatorDirection == direction) {
                if(TryAddRequest(elevator, floor))
                {
                    break;
                }
            }
        }
        ArrayList<Integer> waitingRequest = new ArrayList<>();
        waitingRequest.add(floor);
        waitingRequest.add(direction);
        this.waitingRequests.add(waitingRequest);
    }

    @Override
    public void update(int elevatorId, int floor, int direction) {

    }

    private boolean isRequestValid(int currentFloor)
    {
        return currentFloor < highestPossibleFloor
                && lowestPossibleFloor < currentFloor;
    }

    private boolean TryAddRequest(Elevator elevator, int floor)
    {
        var tasks = elevator.getTasks();
        if(tasks.isEmpty())
        {
            elevator.addRequest(floor);
            return true;
        }
        return false;
    }

}
