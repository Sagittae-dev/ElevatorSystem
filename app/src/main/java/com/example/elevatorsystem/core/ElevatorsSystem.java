package com.example.elevatorsystem.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ElevatorsSystem {
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

    public void removeElevatorFromList(int id) {
        elevatorHandlers.remove(id);
    }

    public void giveRequest(Request request, int elevatorId) throws WrongInputException {
        elevatorHandlers.get(elevatorId).handleRequest(request);
    }

    public void runElevatorHandler(int elevatorHandlerId) {
        elevatorHandlers.get(elevatorHandlerId).runElevator();
    }

    public void stopElevatorHandler(int elevatorHandlerId) {
        elevatorHandlers.get(elevatorHandlerId).stopElevator();
    }

    public void closeDoors(int elevatorHandlerId) {
        try {
            elevatorHandlers.get(elevatorHandlerId).closeDoors();
        } catch (WrongInputException e) {
            e.printStackTrace();
        }
    }

    public void stopAllElevators() {
        elevatorHandlers.forEach(ElevatorHandler::stopElevator);
    }

    public void setAllHandlers(List<Elevator> elevators) {
        elevators.forEach(e -> {
            ElevatorHandler elevatorHandler = new ElevatorHandler(context, e.getElevatorId(), e);
            elevatorHandlers.add(elevatorHandler);
        });
    }
}