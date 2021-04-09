package com.myfirst.storagedemo.managers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.myfirst.storagedemo.database.DataContract;
import com.myfirst.storagedemo.models.User;

import java.util.ArrayList;

import static com.myfirst.storagedemo.providers.UsersContentProvider.CONTENT_AUTHORITY;
import static com.myfirst.storagedemo.providers.UsersContentProvider.PATH_USERS;

public class ContentManager {

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);

    public static ContentManager instance;

    public static synchronized void init(Context context) {
        if (instance == null) instance = new ContentManager(context);
    }

    private final ContentResolver contentResolver;

    private ContentManager(Context context) {
        contentResolver = context.getContentResolver();
    }


    public int clearData() {
        return contentResolver.delete(
                CONTENT_URI,
                null,
                null
        );
    }

    public ArrayList<User> readData() {
        Cursor cursor = contentResolver.query(
                CONTENT_URI,
                null, // array of columns to return
                null, // column for WHERE clause
                null, // values for WHERE clause
                null // sorting
        );

        ArrayList<User> users = new ArrayList<>();


        while (cursor != null && cursor.moveToNext()) {
            int columnNameIndex = cursor.getColumnIndex(DataContract.Users.COLUMN_USER_NAME);
            int columnEmailIndex = cursor.getColumnIndex(DataContract.Users.COLUMN_USER_EMAIL);

            String name = cursor.getString(columnNameIndex);
            String email = cursor.getString(columnEmailIndex);

            User user = new User(name, email);

            users.add(user);
        }

        if (cursor != null) {
            cursor.close();
        }

        return users;
    }

    public long addData(User user) {
        ContentValues values = new ContentValues();
        values.put(DataContract.Users.COLUMN_USER_NAME, user.getName());
        values.put(DataContract.Users.COLUMN_USER_EMAIL, user.getEmail());

        Uri uri = contentResolver.insert(
                CONTENT_URI,
                values
        );

        if (uri == null) return -1;
        else return Long.parseLong(uri.getLastPathSegment());
    }

}
