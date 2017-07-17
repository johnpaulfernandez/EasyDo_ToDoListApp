package com.codepath.easydo;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import static com.codepath.easydo.DetailsDialogFragment.c;
import static com.codepath.easydo.DetailsDialogFragment.dueDate;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DetailsDialogFragment.EditDetailsDialogListener {

    private static final int REQUEST_CODE = 50;

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
                //item.setDueDate(todoItems.get(position).getDueDate());

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
        //item.setDueDate();

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
            databaseHelper.updateItem(todoItems.get(iItemIndex).getTask(), toDoItem.getTask());

            // Update the specific array item with the intent data
            todoItems.get(iItemIndex).setTask(toDoItem.getTask());

            // Notify the adapter that the ListView needs to be refreshed
            aToDoAdapter.notifyDataSetChanged();
        }
    }

    // Handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        dueDate = detailsDialogFragment.format.format(c.getTime());
        detailsDialogFragment.showDate(year, monthOfYear + 1, dayOfMonth);
    }
}
