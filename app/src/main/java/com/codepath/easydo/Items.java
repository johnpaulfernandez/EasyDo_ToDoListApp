package com.codepath.easydo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by John on 2/2/2017.
 */

public class Items implements Serializable{
    public String task;
    public Date dueDate;

    public Items() {
    }

    public Items(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
