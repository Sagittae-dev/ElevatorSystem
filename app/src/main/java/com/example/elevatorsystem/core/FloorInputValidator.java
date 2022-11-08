package com.example.elevatorsystem.core;

public class FloorInputValidator {

    public int getValidDestinationFloorNumber(int input, Elevator elevator) throws WrongInputException {
        if (input >= elevator.getNumberOfFloors() || input < 0) {
            throw new WrongInputException("There is no floor with number: " + input + ". Please choose another one");
        }
        int currentLevel = elevator.getCurrentLevel();
        if (input == currentLevel) {
            throw new WrongInputException("You are on this: ( " + currentLevel + " ) level. Please choose another one");
        }
        return input;
    }
}
