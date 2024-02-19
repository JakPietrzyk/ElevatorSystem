package org.ElevatorSystem;

import org.ElevatorSystem.Constants.ElevatorSettings;
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
        logger.info("Starting elevatorController");
        System.out.println("Provide set up input:\n(number of elevators) (min floor number) (max floor number)");
        Scanner scanner = new Scanner(System.in);
        int numberOfElevators = 2;
        int lowestFloorNumber = ElevatorSettings.LOWEST_FLOOR_NUMBER;
        int highestFloorNumber = ElevatorSettings.HIGHEST_FLOOR_NUMBER;
        try {
            var userOption = scanner.nextLine();
            Pattern setUpInput = Pattern.compile("(\\d+) (\\d+) (\\d+)");
            Matcher matcher = setUpInput.matcher(userOption);
            if (matcher.matches()) {
                numberOfElevators = Integer.parseInt(matcher.group(1));
                lowestFloorNumber = Integer.parseInt(matcher.group(2));
                highestFloorNumber = Integer.parseInt(matcher.group(3));
            }
        } catch (Exception e) {
            logger.error("Invalid setUpInput");
        }
        ElevatorController elevatorController = new ElevatorController(numberOfElevators,
                lowestFloorNumber, highestFloorNumber);
        ConsoleController consoleController = new ConsoleController(elevatorController);
        while (true) {
            System.out.println("""
                    Select option:
                    - take next step, type: step
                    - get status of all elevators, type: status
                    - pickup elevator, type: pickup (floor) (UP/DOWN)
                    - add request inside elevator, type: add (elevator id) (floor)\s
                    """);
            consoleController.executeNextCommand();
        }
    }

}