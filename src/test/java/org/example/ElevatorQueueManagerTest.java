package org.example;

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
    void Add_Should_Tasks_Size_Equals_1() {
        this.elevatorQueueManager.add(1);
        this.elevatorQueueManager.add(2);
        assertEquals(1, this.elevatorQueueManager.getTasks().size());
    }

//    @Test
//    void Get_Task_Should_Return_3_And_Remove_It_From_Queue() {
//        this.elevatorQueueManager.add(3);
//        this.elevatorQueueManager.add(5);
//        this.elevatorQueueManager.add(4);
//        assertEquals(this.elevatorQueueManager.getTask(), 3);
//        assertEquals(2, this.elevatorQueueManager.getTasks().size());
//    }
}