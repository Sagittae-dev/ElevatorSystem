package com.example.elevatorsystem.core;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ElevatorsSystem {
    private static final String TAG = "ElevatorSystem";
    private final List<ElevatorHandler> elevatorHandlers = new ArrayList<>();
    private final Context context;

    public ElevatorsSystem(Context context) {
        this.context = context;
    }

    public ElevatorHandler getElevatorHandlerById(int id) {
        return elevatorHandlers.get(id);
    }

    public List<ElevatorHandler> getAllElevatorHandlers() {
        return elevatorHandlers;
    }

    public void addElevatorToList(ElevatorHandler elevatorHandler) {
        elevatorHandlers.add(elevatorHandler);
    }

    public void removeHandlerFromList(int id) throws WrongInputException {
        boolean elevatorHandlerExist = simpleValidationForElevatorHandlerRequest(id);
        if (elevatorHandlerExist) {
            elevatorHandlers.remove(id);
        } else {
            throw new WrongInputException("You are trying remove handler which no exist");
        }
    }

    private boolean simpleValidationForElevatorHandlerRequest(int id) {
        return elevatorHandlers.stream().anyMatch(eh -> eh.getId() == id);
    }

    public void giveRequest(Request request, int elevatorId) throws WrongInputException {
        if (simpleValidationForElevatorHandlerRequest(elevatorId)) {
            elevatorHandlers.get(elevatorId).handleRequest(request);
        } else {
            throw new WrongInputException("You are giving request for not existing elevator");
        }
    }

    public void closeDoors(int elevatorHandlerId) {
        try {
            elevatorHandlers.get(elevatorHandlerId).closeDoors();
        } catch (WrongInputException e) {
            Log.i(TAG, e.getMessage());
        }
    }

    public void stopAllElevators() {
        elevatorHandlers.forEach(ElevatorHandler::stopElevator);
    }

    public void setAllHandlers(List<Elevator> elevators) {
        elevators.forEach(e -> {

            ElevatorHandler elevatorHandler = createNewElevator(context, e);
            elevatorHandlers.add(elevatorHandler);
        });
    }

    private ElevatorHandler createNewElevator(Context context, Elevator elevator) {

        simpleValidationForElevatorHandlerRequest(elevator.getElevatorId());

        return new ElevatorHandler(context, elevator);
    }
}