package com.example.elevatorsystem.core;

public class PickupRequest implements Request {
    private int destFloor;
    private int priority;

    public PickupRequest(int destFloor) {
        this.destFloor = destFloor;
    }

    @Override
    public int getDestFloor() {
        return destFloor;
    }

    @Override
    public void setDestFloor(int destFloor) {
        this.destFloor = destFloor;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Request request) {
        return this.priority > request.getPriority() ? 0 : 1;
    }
}
