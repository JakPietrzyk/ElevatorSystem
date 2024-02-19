package org.ElevatorSystem.Elevator.Models;

public enum ElevatorDirection {
    Up,
    Idle,
    Down;

    static public ElevatorDirection parse(String value) {
        return switch (value) {
            case "UP" -> ElevatorDirection.Up;
            case "DOWN" -> ElevatorDirection.Down;
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }
}
