package org.ElevatorSystem;

import org.ElevatorSystem.Controllers.ConsoleController;
import org.ElevatorSystem.Controllers.ElevatorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Starting elevatorController");
        System.out.println("Provide set up input:\n(number of elevators) (min floor number) (max floor number)");
        Scanner scanner = new Scanner(System.in);
        int numberOfElevators;
        int lowestFloorNumber;
        int highestFloorNumber;
        while(true) {
            try {
                var userOption = scanner.nextLine();
                Pattern setUpInput = Pattern.compile("(\\d+) (-?\\d+) (-?\\d+)");
                Matcher matcher = setUpInput.matcher(userOption);
                if (matcher.matches()) {
                    numberOfElevators = Integer.parseInt(matcher.group(1));
                    lowestFloorNumber = Integer.parseInt(matcher.group(2));
                    highestFloorNumber = Integer.parseInt(matcher.group(3));
                    break;
                } else {
                    logger.error("Command is invalid: " + userOption);
                    System.out.println("Command is invalid. Please try again");
                }
            } catch (Exception e) {
                logger.error("Invalid input setup");
            }
        }
        ElevatorController elevatorController = new ElevatorController(numberOfElevators,
                lowestFloorNumber, highestFloorNumber);
        ConsoleController consoleController = new ConsoleController(elevatorController);
        System.out.println("""
                Select option:
                - take next step, type: step
                - get status of all elevators, type: status
                - pickup elevator, type: pickup (floor) (UP/DOWN)
                - add request inside elevator, type: add (elevator id) (floor)
                - to exit, type: exit\s
                """);
        while (true) {
            if(consoleController.executeNextCommand()) {
                break;
            }

        }
    }

}