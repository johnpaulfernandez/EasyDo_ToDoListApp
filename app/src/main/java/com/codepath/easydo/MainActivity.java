package com.codepath.easydo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.codepath.easydo.EditItemActivity.ID_EDIT_ITEM;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 50;

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    private int iEditItemIndex;

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
                todoItems.remove(position);

                // Notify the adapter that the ListView needs to be refreshed
                aToDoAdapter.notifyDataSetChanged();

                // Write to file the remaining items in the array list
                writeItems();

                // Notify that the callback consumed the long click
                return true;
            }
        });

        //
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Launch Edit Item Activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra(ID_EDIT_ITEM, todoItems.get(position));
                startActivityForResult(i, REQUEST_CODE);

                // Save the index of updated item
                iEditItemIndex = position;
            }
        });
    }

    public void populateArrayItems() {

        // Populate the array items with the current content of todo text file
        readItems();

        // Initialize the adapter with simple Textview as layout for each of the array items
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    public void onAddItem(View view) {

        // Add the text to the adapter
        aToDoAdapter.add(etEditText.getText().toString());

        // Clear out the text
        etEditText.setText("");

        // Write to file all the array items
        writeItems();
    }


    private void readItems () {

        // Get a reference to a special directory that this application is able to read and write from
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todoTxt");

        // Read each line from the file to populate the array items
        try {
            todoItems = new ArrayList<>(FileUtils.readLines(file));

        } catch (FileNotFoundException e) {

            /* If the array list is not yet created, create initial file without data elements
               to avoid using null adapter reference in setAdapter() */
            if (todoItems == null) {
                todoItems = new ArrayList<>();
                writeItems();
            }
        } catch (IOException e) {

        }
    }

    private void writeItems () {

        // Get a reference to a special directory that this application is able to read and write from
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todoTxt");

        // Write to file the value of each item in todoItems
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String strItem = data.getExtras().getString(ID_EDIT_ITEM);

            // Update the specific array item with the intent data
            todoItems.set(iEditItemIndex, strItem);

            // Notify the adapter that the ListView needs to be refreshed
            aToDoAdapter.notifyDataSetChanged();

            // Write to file the remaining items in the array list
            writeItems();
        }
    }
}
