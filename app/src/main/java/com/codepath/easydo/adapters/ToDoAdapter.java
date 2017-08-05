package com.codepath.easydo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.codepath.easydo.R;
import com.codepath.easydo.models.Items;

import java.util.ArrayList;

/**
 * Created by John on 7/15/2017.
 */

public class ToDoAdapter extends ArrayAdapter<Items> {

    public ToDoAdapter(@NonNull Context context, @NonNull ArrayList<Items> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        final Items item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        // Lookup view for data population
        TextView tvItem = (TextView) convertView.findViewById(R.id.tvItem);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        CheckBox cbStatus = (CheckBox) convertView.findViewById(R.id.cbStatus);

        // Populate the data into the template view using the data object
        tvItem.setText(item.getTask());
        tvPriority.setText(item.getPriority());

        cbStatus.setTag(position); // set the tag so we can identify the correct row in the listener
        cbStatus.setChecked(item.getStatus().equals("Done"));

        try {
            cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setStatus(isChecked ? "Done" : "To-Do");
                }
            }); // set the listener
        } catch (Exception e) {
            e.printStackTrace();
        }


        switch (item.getPriority()) {
            case "Low":
                tvPriority.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlueLow));
                break;
            case "Medium":
                tvPriority.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlueMedium));
                break;
            case "High":
                tvPriority.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlueHigh));
                break;
            default:
                tvPriority.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
