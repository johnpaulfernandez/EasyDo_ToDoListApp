package com.codepath.easydo;

import android.widget.Spinner;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by John on 2/2/2017.
 */

public class Items implements Serializable{
    private String task;
    private String priority;
    private String dueDate;

    public Items() {
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
