package org.example;

import java.util.Collections;
import java.util.Comparator;
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
        //zdecydować czy floor pasuje do kierunku jazdy -> jak nie pasuje to dodać do listy oczekujących
        if(this.elevatorQueue.getFirst() < floor && floor < this.elevatorQueue.getLast())
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
