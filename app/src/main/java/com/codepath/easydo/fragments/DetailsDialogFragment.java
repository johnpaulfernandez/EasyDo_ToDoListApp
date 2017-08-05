package com.codepath.easydo.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.codepath.easydo.models.Items;
import com.codepath.easydo.R;
import com.codepath.easydo.utils.Util;

import java.util.Calendar;

import static android.media.CamcorderProfile.get;
import static com.codepath.easydo.R.id.spPriority;
import static com.codepath.easydo.R.id.spStatus;

/**
 * Created by John on 7/16/2017.
 */

public class DetailsDialogFragment extends DialogFragment{

    private Calendar calendar;
    private TextView dateView;
    private Spinner priority;
    private Spinner status;
    private EditText etTaskName;
    public static int year;
    public static int month;
    public static int day;
    // store the values selected into a Calendar instance
    public static final Calendar dueDate = Calendar.getInstance();
    private Items toDoItem;
    private Button btnSave;
    private ImageButton ibCancel;

    public FragmentManager fm;
    public DatePickerFragment newFragment;

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditDetailsDialogListener {
        void onFinishEditDialog(Items toDoItem);
    }


    public DetailsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DetailsDialogFragment newInstance(String title, Items item) {
        DetailsDialogFragment frag = new DetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("item", item);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toDoItem = (Items) getArguments().getSerializable("item");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_details, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTaskName = (EditText) view.findViewById(R.id.etTaskName);
        priority = (Spinner) view.findViewById(spPriority);
        status = (Spinner) view.findViewById(spStatus);
        dateView = (TextView) view.findViewById(R.id.tvDueDate);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        ibCancel = (ImageButton) view.findViewById(R.id.ibCancel);

        // Get the passed variable from Intent
        etTaskName.setText(toDoItem.getTask());
        etTaskName.setSelection(toDoItem.getTask().length());

        setSpinnerToValue(priority, toDoItem.getPriority(), R.array.priority_array);
        setSpinnerToValue(status, toDoItem.getStatus(), R.array.status_array);

        if (year == 0 && month == 0 && day == 0) {
            calendar = Calendar.getInstance();

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        showDate(year, month + 1, day);

        newFragment = new DatePickerFragment();
        fm = getFragmentManager();

        showDatePickerDialog();

        // Setup a callback when the the SAVE button is pressed on keyboard
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toDoItem.setTask(etTaskName.getText().toString());
                toDoItem.setPriority(priority.getSelectedItem().toString());
                toDoItem.setStatus(status.getSelectedItem().toString());

                // Return input task back to activity through the implemented listener
                EditDetailsDialogListener listener = (EditDetailsDialogListener) getActivity();
                listener.onFinishEditDialog(toDoItem);
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 100% of the screen width
        window.setLayout((int) (size.x), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    public void showDate(int year, int month, int day) {
        dateView.setText(Util.showDate(year, month, day));
    }


    // Attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // SETS the target fragment for use later when sending results
                newFragment.setTargetFragment(DetailsDialogFragment.this, 300);
                newFragment.show(fm, "datePicker");
            }
        });
    }

    public void setSpinnerToValue(Spinner spinner, String value, int arrayID) {

        int index = 0;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), arrayID, R.layout.spinner_item1);
        spinner.setAdapter(adapter);

        SpinnerAdapter spinnerAdapter = spinner.getAdapter();
        for (int i = 0; i < spinnerAdapter.getCount(); i++) {
            if (spinnerAdapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }
}
