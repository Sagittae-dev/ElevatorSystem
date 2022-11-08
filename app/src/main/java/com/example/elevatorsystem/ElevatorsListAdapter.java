package com.example.elevatorsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.elevatorsystem.core.Elevator;

import java.util.List;

public class ElevatorsListAdapter extends BaseAdapter implements ListAdapter {

    private final Context context;
    private final List<Elevator> elevators;

    public ElevatorsListAdapter(Context context, List<Elevator> handlers) {
        this.elevators = handlers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return elevators.size();
    }

    @Override
    public Object getItem(int i) {
        return elevators.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.elevator_list_item, null);
        TextView elevatorItem = view.findViewById(R.id.elevatorListItem_TextView);
        Elevator elevator = elevators.get(i);
        elevatorItem.setText("Elevator id: " + elevator.getElevatorId() + "\n with " + elevator.getNumberOfFloors() + " floors.");
        return view;
    }
}