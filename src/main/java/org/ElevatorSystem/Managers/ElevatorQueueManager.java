package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Managers.Interfaces.QueueManager;

import java.util.*;

public class ElevatorQueueManager implements QueueManager {
    private PriorityQueue<Integer> upQueue;
    private PriorityQueue<Integer> downQueue;
    public ElevatorQueueManager()
    {
        this.upQueue = new PriorityQueue<>();
        this.downQueue = new PriorityQueue<>(Comparator.reverseOrder());
    }

    @Override
    public void addRequest(int floor, int currentElevatorFloor, ElevatorDirection elevatorDirection)
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
                addRequest(floor, currentElevatorFloor, determineDirection(currentElevatorFloor, floor));
        }
    }

    private ElevatorDirection determineDirection(int currentFloor, int destinationFloor) {
        return currentFloor < destinationFloor ? ElevatorDirection.Up : ElevatorDirection.Down;
    }

    @Override
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

    @Override
    public Integer peekTask(ElevatorDirection direction)
    {
        switch (direction)
        {
            case Up -> {
                return this.upQueue.peek();
            }
            case Down -> {
                return this.downQueue.peek();
            }
            case Idle -> {
                if(!this.upQueue.isEmpty()) return this.upQueue.peek();
                else if(!this.downQueue.isEmpty()) return this.downQueue.peek();
            }
        }
        return null;
    }

    @Override
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
    @Override
    public boolean isEmptyUpQueue()
    {
        return this.upQueue.isEmpty();
    }
    @Override
    public boolean isEmptyDownQueue()
    {
        return this.downQueue.isEmpty();
    }
}
