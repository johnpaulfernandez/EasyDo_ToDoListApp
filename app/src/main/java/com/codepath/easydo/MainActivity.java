package com.codepath.easydo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.codepath.easydo.DetailsDialogFragment.c;
import static com.codepath.easydo.DetailsDialogFragment.dueDate;
import static com.codepath.easydo.EditItemActivity.ID_EDIT_ITEM;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_CODE = 50;

    ArrayList<Items> todoItems;
    ToDoAdapter aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    private int iItemIndex;

    DatabaseHelper databaseHelper;
    DetailsDialogFragment detailsDialogFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();

        // Connect the adapter to a ListView to convert the array items into View items
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);

        // Find the view from XML
        etEditText = (EditText)findViewById(R.id.etEditText);

        // Remove from the array list the item in this AdapterView that has been clicked and held
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Delete the particular record in database
                deleteRecord(position);

                todoItems.remove(position);

                // Notify the adapter that the ListView needs to be refreshed
                aToDoAdapter.notifyDataSetChanged();

                // Write to file the remaining items in the array list
                //writeItems();

                // Notify that the callback consumed the long click
                return true;
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Items item = aToDoAdapter.getItem(position);

//                // Launch Edit Item Activity
//                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
//                i.putExtra(ID_EDIT_ITEM, item);
//                startActivityForResult(i, REQUEST_CODE);

                FragmentManager fm = getSupportFragmentManager();
                detailsDialogFragment = DetailsDialogFragment.newInstance("Settings", item);
                detailsDialogFragment.show(fm, "fragment_details");

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

        Items item = new Items(etEditText.getText().toString());

        // Add the text to the adapter
        aToDoAdapter.add(item);
        item.text = etEditText.getText().toString();

        // Clear out the text
        etEditText.setText("");

        // Write to file the new item
        writeItemtoDatabase(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String strItem = data.getExtras().getString(ID_EDIT_ITEM);

            // Pass the context and use the singleton method
            databaseHelper = DatabaseHelper.getInstance(this);

            databaseHelper.updateItem(todoItems.get(iItemIndex).getText(), strItem);

            // Update the specific array item with the intent data
            todoItems.get(iItemIndex).setText(strItem);

            // Notify the adapter that the ListView needs to be refreshed
            aToDoAdapter.notifyDataSetChanged();

        }
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

        databaseHelper.deleteItem(todoItems.get(position).getText());
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

    public void onSave(View view) {
        detailsDialogFragment.onSave(view);
    }

}
