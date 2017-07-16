package com.codepath.easydo;

import java.io.Serializable;

/**
 * Created by John on 2/2/2017.
 */

public class Items implements Serializable{
    public String text;

    public Items() {
    }

    public Items(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
