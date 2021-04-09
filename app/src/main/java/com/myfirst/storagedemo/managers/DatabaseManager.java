package com.myfirst.storagedemo.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myfirst.storagedemo.database.DataBaseContract;
import com.myfirst.storagedemo.database.MySQLiteOpenHelper;
import com.myfirst.storagedemo.models.User;

import java.util.ArrayList;

public class DatabaseManager {

    public static DatabaseManager instance;

    public static synchronized void init(Context context) {
        if (instance == null) instance = new DatabaseManager(context);
    }

    private final MySQLiteOpenHelper mDbHelper;

    private DatabaseManager(Context context) {
        mDbHelper = new MySQLiteOpenHelper(context);
    }

    private SQLiteDatabase getDatabase(boolean write) {
        if (write) {
            return mDbHelper.getWritableDatabase();
        } else {
            return mDbHelper.getReadableDatabase();
        }
    }

    public int clearData() {
        SQLiteDatabase db = getDatabase(true);

        return db.delete(DataBaseContract.Users.TABLE_NAME, null, null);
    }

    public ArrayList<User> readData() {
        SQLiteDatabase db = getDatabase(false);

        Cursor cursor = db.query(
                DataBaseContract.Users.TABLE_NAME,
                null, // array of columns to return
                null, // column for WHERE clause
                null, // values for WHERE clause
                null,
                null,
                null // sorting
        );

        ArrayList<User> users = new ArrayList<>();

        while (cursor.moveToNext()) {
            int columnNameIndex = cursor.getColumnIndex(DataBaseContract.Users.COLUMN_USER_NAME);
            int columnEmailIndex = cursor.getColumnIndex(DataBaseContract.Users.COLUMN_USER_EMAIL);

            String name = cursor.getString(columnNameIndex);
            String email = cursor.getString(columnEmailIndex);

            User user = new User(name, email);

            users.add(user);
        }

        cursor.close();

        return users;
    }

    public long addData(User user) {
        SQLiteDatabase db = getDatabase(true);

        ContentValues values = new ContentValues();
        values.put(DataBaseContract.Users.COLUMN_USER_NAME, user.getName());
        values.put(DataBaseContract.Users.COLUMN_USER_EMAIL, user.getEmail());

        return db.insert(DataBaseContract.Users.TABLE_NAME, null, values);
    }

}
