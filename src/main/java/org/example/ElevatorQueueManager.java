package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class ElevatorQueueManager {
    private LinkedList<Integer> elevatorQueue;
    public ElevatorQueueManager()
    {
        this.elevatorQueue = new LinkedList<>();
    }

    public LinkedList<Integer> getTasks()
    {
        return this.elevatorQueue;
    }
    public void add(int floor)
    {
        this.elevatorQueue.add(floor);
        Collections.sort(this.elevatorQueue);
    }

    public Integer getTask()
    {
        return this.elevatorQueue.pop();
    }
    public boolean isEmpty()
    {
        return this.elevatorQueue.isEmpty();
    }

}
