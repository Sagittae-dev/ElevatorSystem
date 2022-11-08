package com.example.elevatorsystem.core;

import java.io.Serializable;

public class Elevator implements Serializable {

    private int elevatorId;
    private int numberOfFloors;
    private ElevatorMovement elevatorMovement = ElevatorMovement.STATIONARY;
    private int currentLevel;
    private boolean doorsAreOpened;

    public Elevator(int elevatorId, int numberOfFloors) {
        this.elevatorId = elevatorId;
        this.numberOfFloors = numberOfFloors;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public ElevatorMovement getElevatorMovement() {
        return elevatorMovement;
    }

    public void setElevatorMovement(ElevatorMovement elevatorMovement) {
        this.elevatorMovement = elevatorMovement;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public boolean isDoorsAreOpened() {
        return doorsAreOpened;
    }

    public void setDoorsAreOpened(boolean doorsAreOpened) {
        this.doorsAreOpened = doorsAreOpened;
    }
}
