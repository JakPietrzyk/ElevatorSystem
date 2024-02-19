# ElevatorSystem
The Elevator System is a Java-based application designed to manage and control multiple elevators within a building. It provides functionality for handling elevator requests, updating elevator status, and managing elevator queues efficiently.
## Installation
```bash
git clone https://github.com/JakPietrzyk/ElevatorSystem.git
```
```bash
mvn clean install
```
```bash
java -jar elevator-system.jar
```
## Algorithm Overview
* Upon receiving a request, the elevator determines its direction of travel.
* The elevator adds to its queue only those requests that are in the current direction.
* If there are two or more available elevators, the one closest to the new request will respond. If the distance is the same, the first available elevator will be selected.
* If a pickup request is received and all elevators are busy and won't pass through the requested floor, the request is added to the waiting queue.
* The elevator checks the waiting queue status upon finishing the current queue. If there's an entry, the oldest request in the waiting list is processed, and all waiting requests are processed in the new direction of the elevator.
* If the elevator is traveling in a certain direction and a request appears on its way, the destination floor is changed to a closer one.
* Requests from inside the elevator are assigned to that specific elevator.
## Components Overview

### ConsoleController class
The ConsoleController class provides functionality for interacting with the ElevatorSystem through the console.
It allows users to input commands for controlling elevators and monitoring their status.
#### Usage
* "pickup <floor\> <direction\>" to request picking up passengers from a specific floor in the specified direction (e.g., "pickup 5 UP").
* "add <elevatorId\> <floor\>" to add a request inside a specific elevator to stop at a certain floor (e.g., "add 1 8").
* "step" to advance the elevator system by one step.
* "status" to check the current status of the elevators.
### ElevatorController class
Represents the controller for managing elevators in a building. 
It implements the ElevatorSystem interface.
### ElevatorManager class 
Manages multiple elevators and their operations. 
It implements the Manager interface to provide elevator management functionalities.
#### Chosen data structures:
* LinkedHashSet: Used to store waiting requests. It preserves the insertion order and ensures no duplicates are present.
* ArrayList: Utilized to store elevator instances for easy access and management.
### The ElevatorQueueManager class 
Represents a queue manager for elevator requests.
It implements the QueueManager interface to provide methods for adding, getting, and peeking tasks.
#### Chosen data structures:
* PriorityQueue: Employed to store upward and downward requests. This choice is made because it allows for easy retrieval of the smallest or largest floor in the queue.
### StandardElevator class
Represents a standard elevator implementation.
It implements the Elevator interface to provide elevator functionalities.
#### Chosen data structures:
* Enum ElevatorDirection: Utilized to represent the direction of elevator movement, offering clarity and simplicity in managing direction-related operations.