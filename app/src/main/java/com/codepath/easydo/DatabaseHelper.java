package com.codepath.easydo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by John on 2/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static DatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "todolistDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "todoitems";

    // Items Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TASK = "task";
    private static final String KEY_ITEM_PRIORITY = "priority";
    private static final String KEY_ITEM_DUEDATE = "duedate";

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_TASK + " TEXT," +
                KEY_ITEM_PRIORITY + " TEXT," +
                KEY_ITEM_DUEDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }


    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Insert an item into the database
    public void addItem(Items item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TASK, item.getTask());
            values.put(KEY_ITEM_PRIORITY, item.getPriority());
            values.put(KEY_ITEM_DUEDATE, item.getDueDate());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add item to database");
        } finally {
            db.endTransaction();
        }
    }


    public ArrayList<Items> getAllItems() {
        ArrayList<Items> items = new ArrayList<>();

        // SELECT * FROM ITEMS
        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_ITEMS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Items newItem = new Items();
                    newItem.setTask(cursor.getString(cursor.getColumnIndex(KEY_ITEM_TASK)));
                    newItem.setPriority(cursor.getString(cursor.getColumnIndex(KEY_ITEM_PRIORITY)));
                    newItem.setDueDate(cursor.getString(cursor.getColumnIndex(KEY_ITEM_DUEDATE)));
                    items.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    public void deleteItem(String item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            db.delete(TABLE_ITEMS, KEY_ITEM_TASK + "=?", new String[]{String.valueOf(item)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete the record");
        } finally {
            db.endTransaction();
        }
    }

    // Update the item
    public int updateItem(String origItem, Items newItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_TASK, newItem.getTask());
        values.put(KEY_ITEM_PRIORITY, newItem.getPriority());
        values.put(KEY_ITEM_DUEDATE, newItem.getDueDate().toString());

        // Updating the item with the specified ID
        return db.update(TABLE_ITEMS, values, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(getid(origItem)) });
    }

    public String getid(String  item) throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println("ddbpos=" + item);
        long recc = 0;
        String rec = null;

        // item is a String so put it inside '"+____+"'
        // Example: WHERE task= 'Item 1'
        Cursor mCursor = db.rawQuery(
                "SELECT " + KEY_ITEM_ID + " FROM " + TABLE_ITEMS + " WHERE " + KEY_ITEM_TASK + "= '"+item+"'" , null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
            recc = mCursor.getLong(0);
            rec = String.valueOf(recc);
        }
        return rec;
    }
}
