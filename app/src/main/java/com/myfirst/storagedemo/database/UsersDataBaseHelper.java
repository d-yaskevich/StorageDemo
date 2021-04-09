package com.myfirst.storagedemo.database;

class UsersDataBaseHelper {

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + DataContract.Users.TABLE_NAME + " (" +
            DataContract.Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DataContract.Users.COLUMN_USER_NAME + " TEXT," +
            DataContract.Users.COLUMN_USER_EMAIL + " TEXT" +
            ")";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXIST " + DataContract.Users.TABLE_NAME;

}
