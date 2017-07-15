package com.codepath.easydo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.easydo.EditItemActivity.ID_EDIT_ITEM;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 50;

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    private int iItemIndex;

    DatabaseHelper databaseHelper;


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

                // Launch Edit Item Activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra(ID_EDIT_ITEM, todoItems.get(position));
                startActivityForResult(i, REQUEST_CODE);

                // Save the index of updated item
                iItemIndex = position;
            }
        });
    }

    public void populateArrayItems() {

        // Populate the array items with the current content of todo text file
        //readItems();

        // Populate the array items with the current content of the database
        readItemfromDatabase();

        // Initialize the adapter with simple Textview as layout for each of the array items
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);

    }

    public void onAddItem(View view) {

        Items item = new Items();

        // Add the text to the adapter
        aToDoAdapter.add(etEditText.getText().toString());
        item.text = etEditText.getText().toString();

        // Clear out the text
        etEditText.setText("");

        // Write to file all the array items
        //writeItems();

        // Write to file the new item
        writeItemtoDatabase(item);
    }

//
//    private void readItems () {
//
//        // Get a reference to a special directory that this application is able to read and write from
//        File filesDir = getFilesDir();
//        File file = new File(filesDir, "todoTxt");
//
//        // Read each line from the file to populate the array items
//        try {
//            todoItems = new ArrayList<>(FileUtils.readLines(file));
//
//        } catch (FileNotFoundException e) {
//
//            /* If the array list is not yet created, create initial file without data elements
//               to avoid using null adapter reference in setAdapter() */
//            if (todoItems == null) {
//                todoItems = new ArrayList<>();
//                writeItems();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void writeItems () {
//
//        // Get a reference to a special directory that this application is able to read and write from
//        File filesDir = getFilesDir();
//        File file = new File(filesDir, "todoTxt");
//
//        // Write to file the value of each item in todoItems
//        try {
//            FileUtils.writeLines(file, todoItems);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String strItem = data.getExtras().getString(ID_EDIT_ITEM);

            // Pass the context and use the singleton method
            databaseHelper = DatabaseHelper.getInstance(this);

            databaseHelper.updateItem(todoItems.get(iItemIndex), strItem);

            // Update the specific array item with the intent data
            todoItems.set(iItemIndex, strItem);

            // Notify the adapter that the ListView needs to be refreshed
            aToDoAdapter.notifyDataSetChanged();

            // Write to file the remaining items in the array list
            //writeItems();
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
        List<Items> items = databaseHelper.getAllItems();

        todoItems = new ArrayList<>();

        for (Items item : items) {
            todoItems.add(item.text);
        }
    }

    private void deleteRecord(int position) {
        // Pass the context and use the singleton method
        databaseHelper = DatabaseHelper.getInstance(this);

        databaseHelper.deleteItem(todoItems.get(position));
    }


}
