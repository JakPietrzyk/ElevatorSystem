package org.example;

import constants.ElevatorSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorControllerTest {
    ElevatorController elevatorController;
    @BeforeEach
    void setUp() {
        elevatorController = new ElevatorController(1);
    }

    @Test
    void Pickup_From_Highest_Floor_Should_Set_Destination_Floor()
    {
        elevatorController.pickup(ElevatorSettings.HIGHEST_FLOOR_NUMBER, ElevatorDirection.Down);
        assertEquals(ElevatorSettings.HIGHEST_FLOOR_NUMBER, elevatorController.status().getFirst().getDestinationFloor());
    }

}