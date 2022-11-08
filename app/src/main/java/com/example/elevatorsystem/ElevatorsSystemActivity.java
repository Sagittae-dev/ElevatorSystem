package com.example.elevatorsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elevatorsystem.core.Elevator;
import com.example.elevatorsystem.core.ElevatorHandler;
import com.example.elevatorsystem.core.ElevatorsSystem;
import com.example.elevatorsystem.core.PickupRequest;
import com.example.elevatorsystem.core.Request;
import com.example.elevatorsystem.core.WrongInputException;

import java.util.List;

public class ElevatorsSystemActivity extends AppCompatActivity {

    private static final String TAG = "ElevatorsSystem";
    private ElevatorsSystem elevatorsSystem;
    private TextView floorTextView;
    private NumberPicker numberPicker;
    private Elevator currentElevator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevator_system);

        List<Elevator> elevators = getElevatorsFromMainActivity();
        Log.i(TAG, elevators.toString());

        numberPicker = findViewById(R.id.floor_numberPicker);
        numberPicker.setMinValue(0);
        floorTextView = findViewById(R.id.floorInformation_TextView);

        setElevatorSystem(elevators);

        setElevatorsSpinnerList(elevators);

        setPickUpButton();

        setCloseDoorsButton();

    }

    private void setCloseDoorsButton() {
        Button closeDoorsButton = findViewById(R.id.closeDoors_Button);
        closeDoorsButton.setOnClickListener(b -> elevatorsSystem.closeDoors(currentElevator.getElevatorId()));
    }

    private void setPickUpButton() {
        Button pickUpButton = findViewById(R.id.pickup_Button);
        pickUpButton.setOnClickListener(view -> {
            Request request = new PickupRequest(numberPicker.getValue());
            try {
                int elevatorId = currentElevator.getElevatorId();
                elevatorsSystem.giveRequest(request, elevatorId);
            } catch (WrongInputException e) {
                Log.i("ElevatorsSystemActivity", "Problem with wrong input");
            }
        });
    }

    private void setElevatorSystem(List<Elevator> elevators) {
        elevatorsSystem = new ElevatorsSystem(this);
        elevatorsSystem.setAllHandlers(elevators);
    }

    private void setElevatorsSpinnerList(List<Elevator> elevators) {
        ElevatorsListAdapter elevatorsListAdapter = new ElevatorsListAdapter(this, elevators);
        Spinner elevators_Spinner = findViewById(R.id.elevatorsList_Spinner);
        elevators_Spinner.setAdapter(elevatorsListAdapter);

        elevators_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                currentElevator = elevators.get(position);
                int currentLevel = currentElevator.getCurrentLevel();
                ElevatorHandler currentElevatorHandler = elevatorsSystem.getElevatorHandlerById(position);
                currentElevatorHandler.setIsCurrentDisplay(true);
                currentElevatorHandler.setArrowInActivity(currentElevator.getElevatorMovement());
                elevatorsSystem.getAllElevatorHandlers().forEach(eh -> eh.setIsCurrentDisplay(eh.getId() == position));
                floorTextView.setText(String.valueOf(currentLevel));
                numberPicker.setMaxValue(currentElevator.getNumberOfFloors() - 1);
                numberPicker.setValue(currentLevel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    private List<Elevator> getElevatorsFromMainActivity() {
        Intent intent = getIntent();
        return (List<Elevator>) intent.getSerializableExtra("LIST");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        elevatorsSystem.stopAllElevators();
    }
}