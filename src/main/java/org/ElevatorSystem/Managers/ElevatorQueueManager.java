package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Managers.Interfaces.QueueManager;

import java.util.*;
import java.util.function.Function;

public class ElevatorQueueManager implements QueueManager {
    private final PriorityQueue<Integer> upQueue;
    private final PriorityQueue<Integer> downQueue;

    public ElevatorQueueManager() {
        this.upQueue = new PriorityQueue<>();
        this.downQueue = new PriorityQueue<>(Comparator.reverseOrder());
    }

    @Override
    public void addRequest(int floor, int currentElevatorFloor, ElevatorDirection elevatorDirection) {
        if (currentElevatorFloor == floor) return;
        if (floor > currentElevatorFloor) {
            this.upQueue.add(floor);
        } else {
            this.downQueue.add(floor);
        }
    }

    @Override
    public Optional<Integer> getNextDestinationFloor(ElevatorDirection direction) {
        return acquireNextDestinationFloor(direction, PriorityQueue::poll);
    }

    @Override
    public Optional<Integer> peekNextDestinationFloor(ElevatorDirection direction) {
        return acquireNextDestinationFloor(direction, PriorityQueue::peek);
    }

    private Optional<Integer> acquireNextDestinationFloor(ElevatorDirection direction,
                                                          Function<PriorityQueue<Integer>, Integer> queueIntegerFunction) {
        return switch (direction) {
            case Up -> Optional.ofNullable(queueIntegerFunction.apply(this.upQueue));
            case Down -> Optional.ofNullable(queueIntegerFunction.apply(this.downQueue));
            case Idle -> {
                Optional<Integer> task;
                if (!this.upQueue.isEmpty()) task = Optional.ofNullable(queueIntegerFunction.apply(this.upQueue));
                else if (!this.downQueue.isEmpty())
                    task = Optional.ofNullable(queueIntegerFunction.apply(this.downQueue));
                else task = Optional.empty();
                yield task;
            }
        };
    }

    @Override
    public boolean isEmptyQueue(ElevatorDirection direction) {
        switch (direction) {
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

    private boolean isEmptyUpQueue() {
        return this.upQueue.isEmpty();
    }

    private boolean isEmptyDownQueue() {
        return this.downQueue.isEmpty();
    }
}
