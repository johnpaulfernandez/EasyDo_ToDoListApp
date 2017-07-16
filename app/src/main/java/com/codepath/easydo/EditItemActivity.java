package com.codepath.easydo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    public final static String ID_EDIT_ITEM = "edit_item";

    private EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Find the view from XML
        etEditItem = (EditText) findViewById(R.id.etEditItem);

        Items item = (Items) getIntent().getSerializableExtra(ID_EDIT_ITEM);

        // Get the passed variable from Intent
        etEditItem.setText(item.getText());
        etEditItem.setSelection(item.getText().length());
    }


    public void onSaveItem(View view) {

        Intent   data;

        // Prepare data intent
        data = new Intent();

        // Pass the edited item back as a result
        data.putExtra(ID_EDIT_ITEM, etEditItem.getText().toString());

        // Return the data and close the activity;
        setResult(RESULT_OK, data);
        finish();
    }

}
