package com.codepath.easydo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.priority;
import static com.codepath.easydo.DetailsDialogFragment.dueDate;
import static com.codepath.easydo.DetailsDialogFragment.day;
import static com.codepath.easydo.DetailsDialogFragment.month;
import static com.codepath.easydo.DetailsDialogFragment.year;

/**
 * Created by John on 7/15/2017.
 */

public class ToDoAdapter extends ArrayAdapter<Items> {

    public ToDoAdapter(@NonNull Context context, @NonNull ArrayList<Items> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        Items item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        // Lookup view for data population
        TextView tvItem = (TextView) convertView.findViewById(R.id.tvItem);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        //TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);

        // Populate the data into the template view using the data object
        tvItem.setText(item.getTask());
        tvPriority.setText(item.getPriority());

//        Calendar c;
//        c = item.getDueDate();
//        tvDate.setText(Util.showDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));

        // Return the completed view to render on screen
        return convertView;
    }
}
