package org.ElevatorSystem.Controllers;

import org.ElevatorSystem.Elevator.Models.ElevatorDirection;
import org.ElevatorSystem.Constants.ElevatorSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorControllerTest {
    ElevatorController elevatorControllerWithSingleElevator;
    ElevatorController elevatorControllerWithTwoElevators;
    @BeforeEach
    void setUp() {
        elevatorControllerWithSingleElevator = new ElevatorController(1, -1, 5);
        elevatorControllerWithTwoElevators = new ElevatorController(2, -1 ,5);
    }

    @Test
    void Pickup_From_Highest_Floor_Should_Set_Destination_Floor()
    {
        elevatorControllerWithSingleElevator.pickup(ElevatorSettings.DEFAULT_HIGHEST_FLOOR_NUMBER, ElevatorDirection.Down);
        assertEquals(ElevatorSettings.DEFAULT_HIGHEST_FLOOR_NUMBER, elevatorControllerWithSingleElevator.status().getFirst().destinationFloor());
    }

    @Test
    void Pickup_To_Other_Directions_Should_Both_Elevators_Not_Idle_Finishes_On_5_And_2_Floors()
    {
        elevatorControllerWithTwoElevators.pickup(5, ElevatorDirection.Up);
        elevatorControllerWithTwoElevators.pickup(3, ElevatorDirection.Up);
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.pickup(2, ElevatorDirection.Down);
        elevatorControllerWithTwoElevators.step();
        assertEquals(elevatorControllerWithTwoElevators.status().getFirst().direction(), ElevatorDirection.Up);
        assertEquals(elevatorControllerWithTwoElevators.status().getLast().direction(), ElevatorDirection.Up);

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        assertEquals(5, elevatorControllerWithTwoElevators.status().getFirst().currentFloor());
        assertEquals(2, elevatorControllerWithTwoElevators.status().getLast().currentFloor());
    }

    @Test
    void Elevators_On_Same_Floor_Gets_Pickup_To_Others_Directions_Should_Each_Elevator_Get_Request()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(3, ElevatorDirection.Up);
        elevatorControllerWithTwoElevators.pickup(0, ElevatorDirection.Down);
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Down, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(3, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        assertEquals(0, elevatorControllerWithTwoElevators.status().getLast().destinationFloor());
    }

    @Test
    void Elevators_On_Same_Floor_Pickup_To_Up_Direction_Should_One_Elevator_Get_Requests()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(5, ElevatorDirection.Up);
        elevatorControllerWithTwoElevators.pickup(3, ElevatorDirection.Up);
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Idle, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(3, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        assertEquals(5, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
    }

    @Test
    void Elevators_On_Same_Floor_Pickup_To_Down_Direction_Should_One_Elevator_Get_Requests()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(0, ElevatorDirection.Down);
        elevatorControllerWithTwoElevators.pickup(-1, ElevatorDirection.Down);
        assertEquals(ElevatorDirection.Down, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Idle, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(0, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        assertEquals(-1, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
    }

    @Test
    void Elevators_On_Same_Floor_Duplicated_Pickup_To_Down_Direction_Should_Both_Elevators_Get_One_Request()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(3, ElevatorDirection.Down);
        elevatorControllerWithTwoElevators.pickup(3, ElevatorDirection.Down);
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(3, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        assertEquals(3, elevatorControllerWithTwoElevators.status().getLast().destinationFloor());
    }

    @Test
    void Elevators_On_5_And_2_Floor_Pickup_To_0_Should_Closer_Elevator_Get_Request()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 2, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 5, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(0, ElevatorDirection.Up);
        assertEquals(ElevatorDirection.Down, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Idle, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(0, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
    }

    @Test
    void Elevators_On_5_And_1_Floor_Pickup_To_3_Should_One_Elevator_Get_Request()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 5, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(3, ElevatorDirection.Up);
        assertEquals(ElevatorDirection.Down, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Idle, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(3, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
    }

    @Test
    void Elevators_On_Same_Floor_Pickup_On_Idle_Floor_Nothing_Changes()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 2, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 2, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(2, ElevatorDirection.Up);
        assertEquals(ElevatorDirection.Idle, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Idle, elevatorControllerWithTwoElevators.status().getLast().direction());
    }

    @Test
    void Elevators_On_2_And_0_Floor_Pickup_1_4_5_Should_Both_Elevator_Get_Requests()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 2, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 0, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(4, ElevatorDirection.Up);
        elevatorControllerWithTwoElevators.pickup(5, ElevatorDirection.Up);
        elevatorControllerWithTwoElevators.pickup(1, ElevatorDirection.Up);
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(4, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        assertEquals(1, elevatorControllerWithTwoElevators.status().getLast().destinationFloor());
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        assertEquals(5, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        assertEquals(1, elevatorControllerWithTwoElevators.status().getLast().destinationFloor());
    }

    @Test
    void Elevator_On_1_Second_On_3_Going_Down_Pickup_3_5_Up_4_Down_Should_First_Take_3_And_5_Than_4()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 1, ElevatorDirection.Down);
        elevatorControllerWithTwoElevators.addRequestInsideElevator(secondElevatorId,0);
        elevatorControllerWithTwoElevators.pickup(3, ElevatorDirection.Up);
        elevatorControllerWithTwoElevators.pickup(5,ElevatorDirection.Up);
        elevatorControllerWithTwoElevators.pickup(4,ElevatorDirection.Down);
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Down, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(3, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        assertEquals(0, elevatorControllerWithTwoElevators.status().getLast().destinationFloor());
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(4, elevatorControllerWithTwoElevators.status().getLast().destinationFloor());

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        assertEquals(5, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void Elevator_On_1_Pickup_3_5_Up_4_Down_Should_Take_3_And_5_Than_4()
    {
        int firstElevatorId = elevatorControllerWithSingleElevator.status().getFirst().id();
        elevatorControllerWithSingleElevator.update(firstElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithSingleElevator.pickup(3, ElevatorDirection.Up);
        elevatorControllerWithSingleElevator.pickup(5,ElevatorDirection.Up);
        elevatorControllerWithSingleElevator.pickup(4,ElevatorDirection.Down);
        assertEquals(ElevatorDirection.Up, elevatorControllerWithSingleElevator.status().getFirst().direction());
        assertEquals(3, elevatorControllerWithSingleElevator.status().getFirst().destinationFloor());
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithSingleElevator.step();
        elevatorControllerWithSingleElevator.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithSingleElevator.step();
        assertEquals(5, elevatorControllerWithSingleElevator.status().getFirst().destinationFloor());
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithSingleElevator.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithSingleElevator.step();
        elevatorControllerWithSingleElevator.step();
        assertEquals(4, elevatorControllerWithSingleElevator.status().getFirst().destinationFloor());
        assertEquals(ElevatorDirection.Down, elevatorControllerWithSingleElevator.status().getFirst().direction());
    }

    @Test
    void Elevator_On_2_Going_To_0_Pickup_Down_5_3_1_Should_Visit_0_5_3_1()
    {
        int firstElevatorId = elevatorControllerWithSingleElevator.status().getFirst().id();
        elevatorControllerWithSingleElevator.update(firstElevatorId, 0, ElevatorDirection.Up);
        elevatorControllerWithSingleElevator.addRequestInsideElevator(firstElevatorId, 2);
        elevatorControllerWithSingleElevator.pickup(5, ElevatorDirection.Down);
        elevatorControllerWithSingleElevator.pickup(3,ElevatorDirection.Down);
        elevatorControllerWithSingleElevator.pickup(1,ElevatorDirection.Down);
        assertEquals(ElevatorDirection.Up, elevatorControllerWithSingleElevator.status().getFirst().direction());
        assertEquals(2, elevatorControllerWithSingleElevator.status().getFirst().destinationFloor());
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithSingleElevator.step();
        elevatorControllerWithSingleElevator.step();
        elevatorControllerWithSingleElevator.step();
        assertEquals(5, elevatorControllerWithSingleElevator.status().getFirst().destinationFloor());
        elevatorControllerWithSingleElevator.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithSingleElevator.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithSingleElevator.step();
        elevatorControllerWithSingleElevator.step();
        assertEquals(3, elevatorControllerWithSingleElevator.status().getFirst().destinationFloor());
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithSingleElevator.step();
        elevatorControllerWithSingleElevator.step();
        elevatorControllerWithSingleElevator.step();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertEquals(1, elevatorControllerWithSingleElevator.status().getFirst().destinationFloor());
        assertEquals(ElevatorDirection.Down, elevatorControllerWithSingleElevator.status().getFirst().direction());
    }

    @Test
    void Elevators_On_1_Floor_Pickup_4_AddRequestInside_Minus1_Floor_Should_One_Elevator_Visit_4_Minus1()
    {
        int firstElevatorId = elevatorControllerWithTwoElevators.status().getFirst().id();
        int secondElevatorId = elevatorControllerWithTwoElevators.status().getLast().id();
        elevatorControllerWithTwoElevators.update(firstElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.update(secondElevatorId, 1, ElevatorDirection.Idle);
        elevatorControllerWithTwoElevators.pickup(4, ElevatorDirection.Down);
        elevatorControllerWithTwoElevators.addRequestInsideElevator(firstElevatorId, -1);
        assertEquals(ElevatorDirection.Up, elevatorControllerWithTwoElevators.status().getFirst().direction());
        assertEquals(ElevatorDirection.Idle, elevatorControllerWithTwoElevators.status().getLast().direction());
        assertEquals(4, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        elevatorControllerWithTwoElevators.step();
        elevatorControllerWithTwoElevators.step();
        assertEquals(-1, elevatorControllerWithTwoElevators.status().getFirst().destinationFloor());
        assertEquals(ElevatorDirection.Down, elevatorControllerWithTwoElevators.status().getFirst().direction());
    }
}
