package com.example.elevatorsystem.core;

import android.content.Context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ElevatorsSystemTest {
    private static final int ELEVATOR_HANDLER_ID = 0;
    private ElevatorsSystem elevatorsSystem;
    private ElevatorHandler handler;
    private Context contextMock;

    @BeforeEach
    void setUp() {
        elevatorsSystem = new ElevatorsSystem(contextMock);
        Elevator elevator = Mockito.mock(Elevator.class);
        contextMock = Mockito.mock(Context.class);
        handler = Mockito.mock(ElevatorHandler.class);
        elevatorsSystem.addElevatorToList(handler);
        Mockito.when(elevator.getElevatorId()).thenReturn(0);
        Mockito.when(handler.getId()).thenReturn(0);
    }

    @Test
    void closeDoorsTest() throws WrongInputException {
        elevatorsSystem.closeDoors(ELEVATOR_HANDLER_ID);
        Mockito.verify(handler, Mockito.times(1)).closeDoors();
    }

    @Test
    void removeHandlerFromList() throws WrongInputException {
        elevatorsSystem.removeHandlerFromList(ELEVATOR_HANDLER_ID);
        Assertions.assertEquals(0, elevatorsSystem.getAllElevatorHandlers().size());
    }

    @Test
    void removeHandlerFromList_WrongInput() {
        Exception exception = Assertions.assertThrows(WrongInputException.class, () -> elevatorsSystem.removeHandlerFromList(-1));
        Assertions.assertEquals("You are trying remove handler which no exist", exception.getMessage());
    }

    @Test
    void giveRequestTest() throws WrongInputException {
        Request request = new PickupRequest(1);
        elevatorsSystem.giveRequest(request, 0);
        Mockito.verify(handler, Mockito.times(1)).handleRequest(request);
    }

    @Test
    void giveRequestTest_WrongInput() {
        Request request = new PickupRequest(3);
        Exception exception = Assertions.assertThrows(WrongInputException.class, () -> elevatorsSystem.giveRequest(request, -1));
        Assertions.assertEquals("You are giving request for not existing elevator", exception.getMessage());
    }


}