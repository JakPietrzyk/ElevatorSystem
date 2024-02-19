package org.ElevatorSystem.Managers;

import org.ElevatorSystem.Managers.ElevatorManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorManagerTest {
    ElevatorManager elevatorManager;
    @BeforeEach
    void setUp() {
        elevatorManager = new ElevatorManager(3);
    }

    @Test
    void addRequest() {

    }
}