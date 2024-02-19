package org.ElevatorSystem.Controllers;

import org.ElevatorSystem.Controllers.Interfaces.ElevatorSystem;
import org.ElevatorSystem.Elevator.Models.ElevatorDirection;

import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleController {
    final ElevatorSystem elevatorSystem;
    final Pattern nextPickupRegex = Pattern.compile("pickup (\\d+) (UP|DOWN)");
    final Pattern nextRequestInsideRegex = Pattern.compile("add (\\d+) (\\d+)");
    final Pattern nextStep = Pattern.compile("step");
    final Pattern actualStatus = Pattern.compile("status");

    public ConsoleController(ElevatorSystem elevatorSystem) {
        this.elevatorSystem = elevatorSystem;
    }

    public void executeNextCommand() {
        Scanner scanner = new Scanner(System.in);
        try {
            var userOption = scanner.nextLine();
            Matcher matcher = nextPickupRegex.matcher(userOption);
            if (matcher.matches()) {
                int floor = Integer.parseInt(matcher.group(1));
                ElevatorDirection direction = ElevatorDirection.parse(matcher.group(2));
                elevatorSystem.pickup(floor, direction);
            }
            matcher = nextRequestInsideRegex.matcher(userOption);
            if (matcher.matches()) {
                int elevatorId = Integer.parseInt(matcher.group(1));
                int floor = Integer.parseInt(matcher.group(2));
                elevatorSystem.addRequestInsideElevator(elevatorId, floor);
            }
            matcher = nextStep.matcher(userOption);
            if(matcher.matches())
            {
                elevatorSystem.step();
            }
            matcher = actualStatus.matcher(userOption);
            if (matcher.matches())
            {
                System.out.println(elevatorSystem.status());
            }

        } catch (Exception e) {
            System.out.println("Invalid option");
        }
    }
}
