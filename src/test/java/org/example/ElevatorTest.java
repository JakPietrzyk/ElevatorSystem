package org.example;

import constants.ElevatorSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

    private Elevator elevator;
    @BeforeEach
    void setUp() {
        this.elevator = new Elevator(1,0);
    }

    @Test
    void Request_First_Floor_Should_Direction_Up_And_No_Tasks() {
        assertEquals(ElevatorDirection.Idle, elevator.getDirection());
        elevator.addRequest(1);
        assertEquals(ElevatorDirection.Up, elevator.getDirection());
        assertEquals(0, elevator.getTasks(ElevatorDirection.Up).size());
    }

    @Test
    void Two_Requests_In_Range_Should_Direction_Up_And_One_Task()
    {
        elevator.addRequest(1);
        assertEquals(ElevatorDirection.Up, elevator.getDirection());
        elevator.addRequest(4);
        assertEquals(1, elevator.getTasks(ElevatorDirection.Up).size());
    }

    @Test
    void Make_Step_Idle_Elevator_Should_No_Changes_In_Current_Floor() {
        int startingCurrentFloor = elevator.getCurrentFloor();
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevator.makeStep(new ArrayList<>());
        int nextCurrentFloor = elevator.getCurrentFloor();
        assertEquals(nextCurrentFloor, startingCurrentFloor);
        assertEquals(elevator.getCurrentFloor(), elevator.getDestinationFloor());
    }

    @Test
    void Make_Step_Up_Elevator_Should_Increment_Current_Floor() {
        int startingCurrentFloor = elevator.getCurrentFloor();
        elevator.addRequest(ElevatorSettings.HIGHEST_FLOOR_NUMBER);
        elevator.makeStep(new ArrayList<>());
        int nextCurrentFloor = elevator.getCurrentFloor();
        assertEquals(nextCurrentFloor, startingCurrentFloor + 1);
        assertEquals(ElevatorDirection.Up, elevator.getDirection());
        assertNotEquals(elevator.getCurrentFloor(), elevator.getDestinationFloor());
    }

    @Test
    void IsFloorInRange_Idle_Elevator() {
        assertTrue(elevator.IsFloorInRange(ElevatorSettings.HIGHEST_FLOOR_NUMBER));
        assertTrue(elevator.IsFloorInRange(ElevatorSettings.LOWEST_FLOOR_NUMBER));
        assertTrue(elevator.IsFloorInRange(elevator.getCurrentFloor()));
    }
    @Test
    void IsFloorInRange_Up_Elevator() {
        elevator.addRequest(ElevatorSettings.HIGHEST_FLOOR_NUMBER);
        assertTrue(elevator.IsFloorInRange(ElevatorSettings.HIGHEST_FLOOR_NUMBER));
        assertFalse(elevator.IsFloorInRange(ElevatorSettings.LOWEST_FLOOR_NUMBER));
    }
    @Test
    void IsFloorInRange_Down_Elevator() {
        elevator.addRequest(ElevatorSettings.LOWEST_FLOOR_NUMBER);
        assertTrue(elevator.IsFloorInRange(ElevatorSettings.LOWEST_FLOOR_NUMBER));
        assertFalse(elevator.IsFloorInRange(ElevatorSettings.HIGHEST_FLOOR_NUMBER));
    }
}