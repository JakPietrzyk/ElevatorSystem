package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorQueueManagerTest {

    private ElevatorQueueManager elevatorQueueManager;
    @BeforeEach
    void setUp() {
        this.elevatorQueueManager = new ElevatorQueueManager();
    }

    @Test
    public void Add_Request_Up_Adds_To_UpQueue() {
        elevatorQueueManager.addRequest(5, 3, ElevatorDirection.Up);
        assertFalse(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Up));
        assertTrue(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Down));
    }

    @Test
    public void Add_Request_Down_Adds_To_DownQueue() {
        elevatorQueueManager.addRequest(3, 5, ElevatorDirection.Down);
        assertTrue(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Up));
        assertFalse(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Down));
    }

    @Test
    public void Add_Request_Idle_Adds_To_Appropriate_Queue() {
        elevatorQueueManager.addRequest(2, 1, ElevatorDirection.Idle);
        assertFalse(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Up));
        assertTrue(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Down));
        elevatorQueueManager.getTask(ElevatorDirection.Up);
        elevatorQueueManager.addRequest(0, 1, ElevatorDirection.Idle);
        assertTrue(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Up));
        assertFalse(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Down));
    }

    @Test
    public void GetTask_Up_Returns_From_UpQueue() {
        elevatorQueueManager.addRequest(5, 3, ElevatorDirection.Up);
        assertEquals(5, elevatorQueueManager.getTask(ElevatorDirection.Up));
        assertNull(elevatorQueueManager.getTask(ElevatorDirection.Up));
    }

    @Test
    public void GetTask_Down_Returns_From_DownQueue() {
        elevatorQueueManager.addRequest(3, 5, ElevatorDirection.Down);
        assertEquals(3, elevatorQueueManager.getTask(ElevatorDirection.Down));
        assertNull(elevatorQueueManager.getTask(ElevatorDirection.Down));
    }

    @Test
    public void GetTask_Idle_Returns_From_Non_Empty_Queue() {
        elevatorQueueManager.addRequest(3, 5, ElevatorDirection.Down);
        assertEquals(3, elevatorQueueManager.getTask(ElevatorDirection.Idle));
        assertTrue(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Up));
        assertTrue(elevatorQueueManager.isEmptyQueue(ElevatorDirection.Down));
    }

    @Test
    public void Peek_Up_Returns_From_UpQueue() {
        elevatorQueueManager.addRequest(5, 3, ElevatorDirection.Up);
        assertEquals(5, elevatorQueueManager.peekTask(ElevatorDirection.Up));
    }

    @Test
    public void Peek_Down_Returns_From_DownQueue() {
        elevatorQueueManager.addRequest(3, 5, ElevatorDirection.Down);
        assertEquals(3, elevatorQueueManager.peekTask(ElevatorDirection.Down));
    }
}