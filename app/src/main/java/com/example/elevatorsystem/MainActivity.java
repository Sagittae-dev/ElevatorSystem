package com.example.elevatorsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elevatorsystem.core.Elevator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button addElevatorToList_Button;
    private NumberPicker floorsNumberPicker;
    private List<Elevator> elevators;
    private ElevatorsListAdapter elevatorsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setElevatorListView();
        setAddElevatorButton();
        setRemoveElevatorButton();
        setNumberPicker();
        setInstallElevatorSystemButton();
    }

    private void setElevatorListView() {
        elevators = new ArrayList<>();
        elevators.add(new Elevator(0, 10));
        elevatorsListAdapter = new ElevatorsListAdapter(this, elevators);
        ListView elevatorsListView = findViewById(R.id.elevators_ListView);
        elevatorsListView.setAdapter(elevatorsListAdapter);
    }

    private void setRemoveElevatorButton() {
        Button removeLastElevatorFromList_Button = findViewById(R.id.removeLastItem_Button);
        removeLastElevatorFromList_Button.setOnClickListener(view -> {
                    int elevatorsSize = elevators.size();
                    if (elevatorsSize > 1) {
                        elevators.remove(elevatorsSize - 1);
                        elevatorsListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "You can remove item only if more than 1 exist.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setAddElevatorButton() {
        addElevatorToList_Button = findViewById(R.id.addnNewElevator_Button);
        addElevatorToList_Button.setOnClickListener(view -> {
            if (elevators.size() < 15) {
                elevators.add(new Elevator(elevators.size(), floorsNumberPicker.getValue() - 1));
                elevatorsListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "You can add max 16 elevators.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setNumberPicker() {
        floorsNumberPicker = findViewById(R.id.amountOfElevators_NumberPicker);
        floorsNumberPicker.setMinValue(3);
        floorsNumberPicker.setMaxValue(30);
        floorsNumberPicker.setOnValueChangedListener((numberPicker, i, i1) ->
                addElevatorToList_Button.setText("add elevator \n with: " + i1 + " floor" + ((i1 != 1) ? "s" : "")));
    }

    private void setInstallElevatorSystemButton() {
        Button installElevatorSystem_Button = findViewById(R.id.installElevatorSystem_Button);
        installElevatorSystem_Button.setOnClickListener(view -> {
            Intent intent = new Intent(this, ElevatorsSystemActivity.class);
            intent.putExtra("LIST", (Serializable) elevators);
            startActivity(intent);
        });
    }
}