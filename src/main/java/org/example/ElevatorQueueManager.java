package org.example;

import java.util.*;

public class ElevatorQueueManager {
    private PriorityQueue<Integer> upQueue;
    private PriorityQueue<Integer> downQueue;
    public ElevatorQueueManager()
    {
        this.upQueue = new PriorityQueue<>();
        this.downQueue = new PriorityQueue<>(Comparator.reverseOrder());
    }
    public PriorityQueue<Integer> getTasks(ElevatorDirection direction)
    {
        switch (direction)
        {
            case Up -> {
                return this.upQueue;
            }
            case Down -> {
                return this.downQueue;
            }
            default -> {
                return new PriorityQueue<>();
            }
        }
    }

    public void addRequestInside(int floor, int currentElevatorFloor, ElevatorDirection elevatorDirection)
    {
        if(currentElevatorFloor == floor) return;
        switch (elevatorDirection)
        {
            case Up -> {
                if(floor > currentElevatorFloor)
                {
                    this.upQueue.add(floor);
                }
            }
            case Down -> {
                if(floor < currentElevatorFloor)
                {
                    this.downQueue.add(floor);
                }
            }
            case Idle ->
                addRequestInside(floor, currentElevatorFloor, determineDirection(currentElevatorFloor, floor));
        }
    }

    private ElevatorDirection determineDirection(int currentFloor, int destinationFloor) {
        return currentFloor < destinationFloor ? ElevatorDirection.Up : ElevatorDirection.Down;
    }

    public Integer getTask(ElevatorDirection direction)
    {
        switch (direction)
        {
            case Up -> {
                return this.upQueue.poll();
            }
            case Down -> {
                return this.downQueue.poll();
            }
            case Idle -> {
                if(!this.upQueue.isEmpty()) return this.upQueue.poll();
                else if(!this.downQueue.isEmpty()) return this.downQueue.poll();
            }
        }
        return null;
    }
    public boolean isAnyTask()
    {
        return !this.upQueue.isEmpty() || !this.downQueue.isEmpty();
    }
    public boolean isEmptyQueue(ElevatorDirection direction)
    {
        switch (direction)
        {
            case Up -> {
            return this.upQueue.isEmpty();
        }
            case Down -> {
                return this.downQueue.isEmpty();
            }
            default -> {
                return isEmptyDownQueue() && isEmptyUpQueue();
            }
        }
    }

    public boolean isEmptyUpQueue()
    {
        return this.upQueue.isEmpty();
    }
    public boolean isEmptyDownQueue()
    {
        return this.downQueue.isEmpty();
    }
}
