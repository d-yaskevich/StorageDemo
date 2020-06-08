package com.myfirst.storagedemo.database;

class UsersDataBaseHelper {

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + DataBaseContract.Users.TABLE_NAME + " (" +
            DataBaseContract.Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DataBaseContract.Users.COLUMN_USER_NAME + " TEXT," +
            DataBaseContract.Users.COLUMN_USER_EMAIL + " TEXT" +
            ")";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXIST " + DataBaseContract.Users.TABLE_NAME;

}
