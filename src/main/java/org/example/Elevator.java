package org.example;
import constants.ElevatorSettings;

import java.util.ArrayList;
public class Elevator {
    private final int id;
    private int currentFloor;
    private int destinationFloor;
    private int direction;
    private ArrayList<Integer> tasks;
    public Elevator(int id)
    {
        this.id = id;
        this.currentFloor = ElevatorSettings.LOWEST_FLOOR_NUMBER;
        this.destinationFloor = this.currentFloor;
        this.tasks = new ArrayList<>();
        this.direction = 0;
    }

    public void AddSingleTask(ElevatorTask task)
    {
        this.tasks.add(task.getDestinationFloor()); // dodać sprawdzenie czy na pewno request jest na piętro pomiędzy dostępnymi
    }
    public ArrayList<Integer> status()
    {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(id);
        result.add(currentFloor);
        result.add(destinationFloor);
        return result; //tutaj zastanowić się czy na pewno to dobra struktura
    }

    @Override
    public String toString() {
        return "Elevator id: " + this.id + " current Floor: " + currentFloor + " destination floor: " + destinationFloor;
    }

    public int getDirection() {
        return direction;
    }

    public ArrayList<Integer> getTasks() {
        return tasks;
    }

    public void addRequest(int floor)
    {
        tasks.add(floor);
        if(this.direction == 0)
        {
            if(floor - currentFloor > 0)
            {
                this.direction = 1;
            }
            else if(floor - currentFloor < 0)
            {
                this.direction = -1;
            }
        }
    }

    public void makeStep()
    {
        if(this.direction > 0)
        {
            this.currentFloor++;
        }
        else if(this.direction < 0)
        {
            this.currentFloor--;
        }
        if(this.currentFloor == this.destinationFloor && !this.tasks.isEmpty())
        {
            this.destinationFloor = this.tasks.getLast();
            this.tasks.removeLast();
        }
        else if(this.tasks.isEmpty())
        {
            this.direction = 0;
        }

    }


}
