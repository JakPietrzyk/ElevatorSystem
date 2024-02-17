package org.example;

import constants.ElevatorSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorControllerTest {
    ElevatorController elevatorControllerWithSingleElevator;
    ElevatorController elevatorControllerWithTwoElevators;
    @BeforeEach
    void setUp() {
        elevatorControllerWithSingleElevator = new ElevatorController(1);
        elevatorControllerWithTwoElevators = new ElevatorController(2);
    }

    @Test
    void Pickup_From_Highest_Floor_Should_Set_Destination_Floor()
    {
        elevatorControllerWithSingleElevator.pickup(ElevatorSettings.HIGHEST_FLOOR_NUMBER, ElevatorDirection.Down);
        assertEquals(ElevatorSettings.HIGHEST_FLOOR_NUMBER, elevatorControllerWithSingleElevator.status().getFirst().getDestinationFloor());
    }

    @Test
    void Pickup_To_Other_Directions_Should_Both_Elevators_Not_Idle()
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


        assertEquals(elevatorControllerWithTwoElevators.status().getFirst().getDirection(), ElevatorDirection.Up);
        assertEquals(elevatorControllerWithTwoElevators.status().getLast().getDirection(), ElevatorDirection.Up);
    }


}