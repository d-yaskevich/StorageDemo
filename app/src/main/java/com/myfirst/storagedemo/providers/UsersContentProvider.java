package com.myfirst.storagedemo.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myfirst.storagedemo.database.DataContract;
import com.myfirst.storagedemo.database.MySQLiteOpenHelper;

public class UsersContentProvider extends ContentProvider {

    private static final String TAG = UsersContentProvider.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "com.myfirst.storagedemo.provider";
    public static final String PATH_USERS = DataContract.Users.TABLE_NAME;

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;

    private static final int USERS = 1;
    private static final int USER_ID = 2;

    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for table are shown.
         */

        /*
         * Sets the integer value for multiple rows to 1. Notice that no wildcard is used
         * in the path
         */
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_USERS, USERS);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used.
         */
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_USERS + "/#", USER_ID);
    }


    private MySQLiteOpenHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MySQLiteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        final int match = uriMatcher.match(uri);

        /*
         * Choose the table to query and a sort order based on the code returned for the incoming
         * URI.
         */
        switch (uriMatcher.match(uri)) {
            // If the incoming URI was for all of table
            case USERS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;
            // If the incoming URI was for a single row
            case USER_ID:
                /*
                 * Because this URI was for a single row, the _ID value part is
                 * present. Get the last path segment from the URI; this is the _ID value.
                 * Then, append the value to the WHERE clause for the query
                 */
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            default:
                // If the URI is not recognized, you should do some error handling here.
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
        SQLiteDatabase db = getDatabase(false);
        return db.query(DataContract.Users.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case USERS:
                return CONTENT_LIST_TYPE;
            case USER_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case USERS:
                SQLiteDatabase db = getDatabase(true);
                checkValues(values, false);
                long id = db.insert(DataContract.Users.TABLE_NAME, null, values);

                // If the ID is -1, then the insertion failed. Log an error and return null.
                if (id == -1) {
                    Log.e(TAG, "Failed to insert row for " + uri);
                    return null;
                }
                return ContentUris.withAppendedId(uri, id);
            case USER_ID:
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case USERS:
                break;
            case USER_ID:
                selection += "_ID = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        SQLiteDatabase db = getDatabase(true);
        return db.delete(DataContract.Users.TABLE_NAME, selection, selectionArgs);

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case USERS:
                break;
            case USER_ID:
                selection += "_ID = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        SQLiteDatabase db = getDatabase(true);

        checkValues(values, true);
        return db.update(DataContract.Users.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private SQLiteDatabase getDatabase(boolean write) {
        if (write) {
            return mDbHelper.getWritableDatabase();
        } else {
            return mDbHelper.getReadableDatabase();
        }
    }

    private void checkValues(ContentValues values, boolean update) {
        if (values == null || values.size() > 0) return;

        if (values.containsKey(DataContract.Users.COLUMN_USER_NAME)) {
            String name = values.getAsString(DataContract.Users.COLUMN_USER_NAME);
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("User requires a name");
            }
        } else if (!update) {
            throw new IllegalArgumentException("User requires a name");
        }

        if (values.containsKey(DataContract.Users.COLUMN_USER_EMAIL)) {
            String email = values.getAsString(DataContract.Users.COLUMN_USER_EMAIL);
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("User requires a email");
            }
        } else if (!update) {
            throw new IllegalArgumentException("User requires a email");
        }

    }


}
