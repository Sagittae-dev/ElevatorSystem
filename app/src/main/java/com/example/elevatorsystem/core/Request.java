package com.example.elevatorsystem.core;

public interface Request extends Comparable<Request> {
    int getDestFloor();

    void setDestFloor(int destFloor);

    int getPriority();

    void setPriority(int priority);
}
