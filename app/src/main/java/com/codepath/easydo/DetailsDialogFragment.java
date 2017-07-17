package com.codepath.easydo;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.codepath.easydo.R.id.spPriority;

/**
 * Created by John on 7/16/2017.
 */

public class DetailsDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{

    public static final String ID_DATE = "10";
    public static final String ID_SORT_ORDER = "11";
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    public Spinner priority;
    private EditText etTaskName;
    public static int priorityIndex;
    public static int year;
    public static int month;
    public static int day;
    // store the values selected into a Calendar instance
    static final Calendar c = Calendar.getInstance();
    public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static String dueDate;
    private Items toDoItem;
    private Button btnSave;

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
        //return inflater.inflate(R.layout.fragment_filter, container);
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_details, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTaskName = (EditText) view.findViewById(R.id.etTaskName);
        priority = (Spinner) view.findViewById(spPriority);
        dateView = (TextView) view.findViewById(R.id.tvDueDate);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        // Get the passed variable from Intent
        etTaskName.setText(toDoItem.getTask());
        etTaskName.setSelection(toDoItem.getTask().length());

        priority.setSelection(priorityIndex);


        calendar = Calendar.getInstance();

        if (year == 0 && month == 0 && day == 0) {
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

                priorityIndex = priority.getSelectedItemPosition();

                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                toDoItem.setTask(etTaskName.getText().toString());

                // Return input task back to activity through the implemented listener
                EditDetailsDialogListener listener = (EditDetailsDialogListener) getActivity();
                listener.onFinishEditDialog(toDoItem);
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (EditorInfo.IME_ACTION_SEND == actionId) {
            // Return input task back to activity through the implemented listener
              EditDetailsDialogListener listener = (EditDetailsDialogListener) getActivity();
              listener.onFinishEditDialog(toDoItem);
              // Close the dialog and return back to the parent activity
              dismiss();
              return true;
        }
        return false;
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
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
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

//    public void onSave(View view) {
//
//        priorityIndex = priority.getSelectedItemPosition();
//
//        this.year = c.get(Calendar.YEAR);
//        this.month = c.get(Calendar.MONTH);
//        this.day = c.get(Calendar.DAY_OF_MONTH);;
//
//        // Close the dialog and return back to the parent activity
//        dismiss();
//    }
}
