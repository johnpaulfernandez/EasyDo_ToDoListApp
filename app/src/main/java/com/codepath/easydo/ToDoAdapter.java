package com.codepath.easydo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.priority;
import static com.codepath.easydo.DetailsDialogFragment.dueDate;
import static com.codepath.easydo.DetailsDialogFragment.day;
import static com.codepath.easydo.DetailsDialogFragment.month;
import static com.codepath.easydo.DetailsDialogFragment.year;
import static com.codepath.easydo.R.id.cbStatus;

/**
 * Created by John on 7/15/2017.
 */

public class ToDoAdapter extends ArrayAdapter<Items> {

    public List<Items> todoListItem;
    private Context mContext;

    // Pass the context and use the singleton method
    DatabaseHelper databaseHelper;

    CompoundButton.OnCheckedChangeListener cbListener;

    public ToDoAdapter(@NonNull Context context, @NonNull ArrayList<Items> objects) {
        super(context, 0, objects);
        this.todoListItem = objects;
        this.mContext = context;
        databaseHelper = DatabaseHelper.getInstance(mContext);
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
        //TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

        // Populate the data into the template view using the data object
        tvItem.setText(item.getTask());
        tvPriority.setText(item.getPriority());

        cbStatus.setTag(Integer.valueOf(position)); // set the tag so we can identify the correct row in the listener
        cbStatus.setChecked(item.getStatus().equals("Done"));
        cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                item.setStatus(isChecked ? "Done" : "To-Do");

                // Extract the task name and update the database
                databaseHelper.updateItem(todoListItem.get(position).getTask(), item);
            }
        }); // set the listener

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

//        Calendar c;
//        c = item.getDueDate();
//        tvDate.setText(Util.showDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));

        // Return the completed view to render on screen
        return convertView;
    }
}
