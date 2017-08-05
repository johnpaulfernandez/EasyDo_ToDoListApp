package com.codepath.easydo.activities;

import android.app.DatePickerDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.codepath.easydo.utils.DatabaseHelper;
import com.codepath.easydo.fragments.DetailsDialogFragment;
import com.codepath.easydo.models.Items;
import com.codepath.easydo.R;
import com.codepath.easydo.adapters.ToDoAdapter;
import com.codepath.easydo.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;

import static com.codepath.easydo.fragments.DetailsDialogFragment.dueDate;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DetailsDialogFragment.EditDetailsDialogListener {

    ArrayList<Items> todoItems;
    ToDoAdapter aToDoAdapter;
    ListView lvItems;
    private int iItemIndex;

    DatabaseHelper databaseHelper;
    DetailsDialogFragment detailsDialogFragment;
    private boolean newItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();

        // Connect the adapter to a ListView to convert the array items into View items
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorWhite));

        // Remove from the array list the item in this AdapterView that has been clicked and held
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Delete the particular record in database
                deleteRecord(position);

                todoItems.remove(position);

                // Notify the adapter that the ListView needs to be refreshed
                aToDoAdapter.notifyDataSetChanged();

                // Notify that the callback consumed the long click
                return true;
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Items item = new Items();
                item.setTask(todoItems.get(position).getTask());
                item.setPriority(todoItems.get(position).getPriority());
                item.setDueDate(todoItems.get(position).getDueDate());
                item.setStatus(todoItems.get(position).getStatus());

                newItem = false;
                addUpdateTaskDetails(item);

                // Save the index of updated item
                iItemIndex = position;

            }
        });
    }

    public void populateArrayItems() {

        // Populate the array items with the current content of the database
        readItemfromDatabase();

        // Create the adapter to convert the array to views
        aToDoAdapter = new ToDoAdapter(this, todoItems);
    }

    public void onAddItem(View view) {

        Items item = new Items();
        item.setTask("");
        item.setDueDate(Util.getDateTime());

        newItem = true;
        addUpdateTaskDetails(item);
    }

    private void writeItemtoDatabase(Items items) {

        // Pass the context and use the singleton method
        databaseHelper = DatabaseHelper.getInstance(this);

        // Add item to the database
        databaseHelper.addItem(items);
    }


    private void readItemfromDatabase(){
        // Pass the context and use the singleton method
        databaseHelper = DatabaseHelper.getInstance(this);

        // Get all posts from database
        ArrayList<Items> items = databaseHelper.getAllItems();

        todoItems = new ArrayList<>();

        todoItems.addAll(items);
    }

    private void deleteRecord(int position) {
        // Pass the context and use the singleton method
        databaseHelper = DatabaseHelper.getInstance(this);

        databaseHelper.deleteItem(todoItems.get(position).getTask());
    }

    private void addUpdateTaskDetails(Items item) {
        FragmentManager fm = getSupportFragmentManager();
        detailsDialogFragment = DetailsDialogFragment.newInstance("Settings", item);
        detailsDialogFragment.show(fm, "fragment_details");
    }

    // Listener is triggered on clicking SAVE button in DetailsDialogFragment
    // Access the data result passed by the DetailsDialogFragment
    @Override
    public void onFinishEditDialog(Items toDoItem) {

        try {
            if (newItem) {
                // Add the text to the adapter
                aToDoAdapter.add(toDoItem);

                // Write to file the new item
                writeItemtoDatabase(toDoItem);
            }
            else {
                // Pass the context and use the singleton method
                databaseHelper = DatabaseHelper.getInstance(this);

                // Extract the task name and update the database
                databaseHelper.updateItem(todoItems.get(iItemIndex).getTask(), toDoItem);

                // Update the specific array item with the intent data
                todoItems.get(iItemIndex).setTask(toDoItem.getTask());
                todoItems.get(iItemIndex).setPriority(toDoItem.getPriority());
                todoItems.get(iItemIndex).setStatus(toDoItem.getStatus());
                todoItems.get(iItemIndex).setDueDate(toDoItem.getDueDate());

                // Notify the adapter that the ListView needs to be refreshed
                aToDoAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        dueDate.set(Calendar.YEAR, year);
        dueDate.set(Calendar.MONTH, monthOfYear);
        dueDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        detailsDialogFragment.showDate(year, monthOfYear + 1, dayOfMonth);
    }
}
