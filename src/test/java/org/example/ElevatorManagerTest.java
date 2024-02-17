package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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