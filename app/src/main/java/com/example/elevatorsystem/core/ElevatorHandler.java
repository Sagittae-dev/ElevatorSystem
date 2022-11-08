package com.example.elevatorsystem.core;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elevatorsystem.ElevatorsSystemActivity;
import com.example.elevatorsystem.R;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ElevatorHandler {
    private static final String TAG = "ElevatorHandler";
    private final int id;
    private final Elevator elevator;
    private final TreeSet<Request> requestsSet = new TreeSet<>();
    private final Context context;
    private boolean[] buttons;
    private boolean elevatorIsEnabled;
    private Thread workThread;
    private boolean isCurrentDisplay;

    public ElevatorHandler(Context context, int id, Elevator elevator) {
        this.context = context;
        this.id = id;
        this.elevator = elevator;
        buttons = new boolean[elevator.getNumberOfFloors()];
    }

    public void setIsCurrentDisplay(boolean isCurrentDisplay) {
        this.isCurrentDisplay = isCurrentDisplay;
    }

    public void handleRequest(Request request) throws WrongInputException {
        int dest = request.getDestFloor();
        if (!destFloorIsBetweenCurrentAndPreviousDestFloor()) {
            buttons = addNewDestFloorToList(dest);
        } else {
            requestsSet.add(request);
        }
        if (!elevatorIsEnabled)
            runElevator();
        Log.i(TAG, requestsSet.stream().map(Request::getDestFloor).collect(Collectors.toSet()).toString());
    }

    private boolean destFloorIsBetweenCurrentAndPreviousDestFloor() {
        for (int i = 0; i < elevator.getNumberOfFloors() - 1; i++) {
            if (buttons[i])
                return true;
        }
        return false;
    }

    private boolean[] addNewDestFloorToList(int dest) throws WrongInputException {
        FloorInputValidator floorInputValidator = new FloorInputValidator();
        dest = floorInputValidator.getValidDestinationFloorNumber(dest, elevator);
        buttons[dest] = true;

        return buttons;
    }

    private synchronized void setWorkThread() {
        workThread = new Thread(() -> {
            while (elevatorIsEnabled) {
                ElevatorMovement elevatorMovement = ElevatorMovement.STATIONARY;
                try {
                    elevatorMovement = checkElevatorMovement();
                } catch (InterruptedException | WrongInputException e) {
                    e.printStackTrace();
                }
                int currentLevel = elevator.getCurrentLevel();

                System.out.println("Elevator ID: " + elevator.getElevatorId() + " Current level: " + currentLevel + " Elevator status: " + elevator.getElevatorMovement().toString());
                System.out.println(Arrays.toString(buttons));

                displayCurrentLevel(currentLevel);

                if (elevatorMovement == ElevatorMovement.GOINGUP && currentLevel != elevator.getNumberOfFloors() && !elevator.isDoorsAreOpened()) {
                    elevator.setCurrentLevel(currentLevel + 1);
                }
                if (elevatorMovement == ElevatorMovement.GOINGDOWN && currentLevel != 0 && !elevator.isDoorsAreOpened()) {
                    elevator.setCurrentLevel(currentLevel - 1);
                }
                if (buttons[currentLevel]) {
                    try {
                        openDoors(currentLevel);
                    } catch (WrongInputException e) {
                        Log.i(TAG, "Wrong input");
                    }
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void displayCurrentLevel(int currentLevel) {
        if (isCurrentDisplay) {
            ElevatorsSystemActivity elevatorsSystemActivity = (ElevatorsSystemActivity) context;
            elevatorsSystemActivity.runOnUiThread(() -> {
                TextView floorText = elevatorsSystemActivity.findViewById(R.id.floorInformation_TextView);
                floorText.setText(String.valueOf(currentLevel));
            });
        }
    }

    private ElevatorMovement checkElevatorMovement() throws InterruptedException, WrongInputException {
        ElevatorMovement elevatorMovement = elevator.getElevatorMovement();
        int currentLevel = elevator.getCurrentLevel();

        if (elevatorMovement == ElevatorMovement.GOINGUP) {
            for (int i = elevator.getNumberOfFloors() - 1; i > currentLevel; i--) {
                if (buttons[i]) return elevatorMovement;
            }
            elevator.setElevatorMovement(ElevatorMovement.STATIONARY);
            return ElevatorMovement.STATIONARY;
        }

        if (elevatorMovement == ElevatorMovement.GOINGDOWN) {
            for (int i = 0; i < currentLevel; i++) {
                if (buttons[i]) return elevatorMovement;
            }
            elevator.setElevatorMovement(ElevatorMovement.STATIONARY);
            return ElevatorMovement.STATIONARY;
        }

        if (elevatorMovement == ElevatorMovement.STATIONARY) {
            for (int i = 0; i < elevator.getNumberOfFloors(); i++) {
                if (buttons[i]) {
                    if (i > currentLevel) {
                        elevator.setElevatorMovement(ElevatorMovement.GOINGUP);
                        closeDoors();
                        setArrowInActivity(ElevatorMovement.GOINGUP);
                        return ElevatorMovement.GOINGUP;
                    }
                    if (i < currentLevel) {
                        elevator.setElevatorMovement(ElevatorMovement.GOINGDOWN);
                        closeDoors();
                        setArrowInActivity(ElevatorMovement.GOINGDOWN);
                        return ElevatorMovement.GOINGDOWN;
                    }
                }
            }
        }
        return ElevatorMovement.STATIONARY;
    }

    public void setArrowInActivity(ElevatorMovement elevatorMovement) {
        ElevatorsSystemActivity elevatorsSystemActivity = (ElevatorsSystemActivity) context;
        ImageView upArrow = elevatorsSystemActivity.findViewById(R.id.upArrow_imageView);
        ImageView downArrow = elevatorsSystemActivity.findViewById(R.id.downArrow_imageView);
        elevatorsSystemActivity.runOnUiThread(() -> {
            if (elevatorMovement == ElevatorMovement.GOINGUP && isCurrentDisplay) {
                upArrow.setVisibility(View.VISIBLE);
                downArrow.setVisibility(View.INVISIBLE);
            }
            if (elevatorMovement == ElevatorMovement.GOINGDOWN && isCurrentDisplay) {
                upArrow.setVisibility(View.INVISIBLE);
                downArrow.setVisibility(View.VISIBLE);
            }
            if (elevatorMovement == ElevatorMovement.STATIONARY || !isCurrentDisplay) {
                upArrow.setVisibility(View.INVISIBLE);
                downArrow.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void openDoors(int level) throws WrongInputException {
        elevatorIsEnabled = false;
        elevator.setDoorsAreOpened(true);
        buttons[level] = false;
        System.out.println("Doors are opened on: " + level + " floor.");
        setArrowInActivity(ElevatorMovement.STATIONARY);
    }

    private void runRequestIfScheduled() throws WrongInputException {
        if (!requestsSet.isEmpty()) {
            handleRequest(requestsSet.pollFirst());
        }
    }

    public void closeDoors() throws WrongInputException {
        if (elevator.isDoorsAreOpened()) {
            System.out.println("Doors are closed on: " + elevator.getCurrentLevel() + " floor.");
            elevator.setDoorsAreOpened(false);
            runRequestIfScheduled();
            startWork();
        }
    }

    private void startWork() {
        if (workThread.getState() == Thread.State.NEW) {
            workThread.start();
        }
    }

    public void stopElevator() {
        workThread.interrupt();
    }

    public void runElevator() {
        if (!elevatorIsEnabled) {
            this.elevatorIsEnabled = true;
            setWorkThread();
            startWork();
        }
    }

    public int getId() {
        return id;
    }
}