package com.myfirst.storagedemo.database;

import android.provider.BaseColumns;

public class DataBaseContract {

    public DataBaseContract() {
    }

    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USER_NAME = "name";
        public static final String COLUMN_USER_EMAIL = "email";
    }

}
