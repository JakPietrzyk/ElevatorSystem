package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Elevator.Models.ElevatorTask;
import org.ElevatorSystem.Managers.ElevatorManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorManagerTest {
    ElevatorManager elevatorManager;
    @BeforeEach
    void setUp() {
        elevatorManager = new ElevatorManager(1);
    }

    @Test
    public void AddRequest_Adds_Request_To_Closer_Elevator()
    {
        ElevatorManager elevatorManagerWithTwoElevators = new ElevatorManager(2);
        int firstElevatorId = elevatorManagerWithTwoElevators.status().getFirst().id();
        elevatorManagerWithTwoElevators.update(firstElevatorId, 5, ElevatorDirection.Idle);
        elevatorManagerWithTwoElevators.addRequest(1, ElevatorDirection.Up);
        assertEquals(ElevatorDirection.Idle, elevatorManagerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Up, elevatorManagerWithTwoElevators.status().getLast().direction());
    }
}