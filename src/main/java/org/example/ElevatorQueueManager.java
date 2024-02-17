package org.example;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class ElevatorQueueManager {
    private LinkedList<Integer> elevatorQueue;
    private LinkedList<Integer> elevatorWaitingQueue;
    public ElevatorQueueManager()
    {
        this.elevatorQueue = new LinkedList<>();
        this.elevatorWaitingQueue = new LinkedList<>();
    }

    public LinkedList<Integer> getTasks()
    {
        return this.elevatorQueue;
    }
    public void add(int floor)
    {
        if(this.elevatorQueue.contains(floor)) return;
        if(this.elevatorQueue.isEmpty() || (this.elevatorQueue.getFirst() < floor && floor < this.elevatorQueue.getLast()))
        {
            this.elevatorQueue.add(floor);
        }
        else
        {
            this.elevatorWaitingQueue.add(floor);
        }
        Collections.sort(this.elevatorQueue);
    }

    public void addRequestInside(int floor, int currentElevatorFloor, ElevatorDirection elevatorDirection)
    {
        if(this.elevatorQueue.contains(floor) /*|| this.elevatorWaitingQueue.contains(floor)*/) return;
        switch (elevatorDirection)
        {
            case Up -> {
                if(floor >= currentElevatorFloor)
                {
                    this.elevatorQueue.add(floor);
                    Collections.sort(this.elevatorQueue);
                }
                else
                {
                    this.elevatorWaitingQueue.add(floor);
                    Collections.sort(this.elevatorWaitingQueue);
                }
            }
            case Down -> {
                if(floor <= currentElevatorFloor)
                {
                    this.elevatorQueue.add(floor);
                    this.elevatorQueue.sort(Comparator.reverseOrder());
                }
                else
                {
                    this.elevatorWaitingQueue.add(floor);
                    Collections.sort(this.elevatorWaitingQueue);
                }
            }
            case Idle -> this.elevatorQueue.add(floor);
        }
    }

    public ElevatorDirection processWaitingTasks(int currentFloor)
    {
        if(this.elevatorWaitingQueue.isEmpty()) return ElevatorDirection.Idle;

        int firstTask = this.elevatorWaitingQueue.peek();
        ElevatorDirection newDirection = determineDirection(currentFloor, firstTask);
        Iterator<Integer> iterator = this.elevatorWaitingQueue.iterator();
        while (iterator.hasNext()) {
            int task = iterator.next();
            ElevatorDirection taskDirection = determineDirection(currentFloor, task);
            if (taskDirection == newDirection) {
                this.elevatorQueue.add(task);
                iterator.remove();
            }
        }
        switch (newDirection)
        {
            case Up -> {this.elevatorQueue.sort(Comparator.naturalOrder());}
            case Down -> {this.elevatorQueue.sort(Comparator.reverseOrder());}
        }
        return newDirection;
    }
    private ElevatorDirection determineDirection(int currentFloor, int destinationFloor) {
        return currentFloor < destinationFloor ? ElevatorDirection.Up : ElevatorDirection.Down;
    }

    public Integer getTask()
    {
        return this.elevatorQueue.pop();
    }
    public boolean isAnyTask()
    {
        return !this.elevatorQueue.isEmpty() || !this.elevatorWaitingQueue.isEmpty();
    }
    public boolean isEmptyQueue()
    {
        return this.elevatorQueue.isEmpty();
    }

    public boolean isEmptyWaitingQueue()
    {
        return this.elevatorWaitingQueue.isEmpty();
    }
}
