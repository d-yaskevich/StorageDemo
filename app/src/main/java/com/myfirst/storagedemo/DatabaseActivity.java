package com.myfirst.storagedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myfirst.storagedemo.database.DataBaseContract.*;
import com.myfirst.storagedemo.database.MySQLiteOpenHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "result.txt";

    private EditText nameET;
    private EditText emailET;

    private TextView resultTV;

    private MySQLiteOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        nameET = findViewById(R.id.etName);
        emailET = findViewById(R.id.etEmail);

        Button addBtn = findViewById(R.id.btnAdd);
        Button readBtn = findViewById(R.id.btnRead);
        Button clearBtn = findViewById(R.id.btnClear);

        addBtn.setOnClickListener(this);
        readBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        resultTV = findViewById(R.id.tvResult);

        mDbHelper = new MySQLiteOpenHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                addDataToDB();
                break;
            case R.id.btnRead:
                readDataFromDB();
                break;
            case R.id.btnClear:
                clearDataFromDB();
                break;
        }
    }

    private void clearDataFromDB() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int deletedRows = db.delete(Users.TABLE_NAME, null, null); //*

        String result = "--- Deleted " + deletedRows + " row(s)\n";
        resultTV.append(result);
    }

    private void readDataFromDB() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                Users.TABLE_NAME,
                null, // array of columns to return
                null, // column for WHERE clause
                null, // values for WHERE clause
                null,
                null,
                null // sorting
        );

        if (cursor.moveToNext()) {
            do {
                int columnNameIndex = cursor.getColumnIndex(Users.COLUMN_USER_NAME);
                int columnEmailIndex = cursor.getColumnIndex(Users.COLUMN_USER_EMAIL);

                String name = cursor.getString(columnNameIndex);
                String email = cursor.getString(columnEmailIndex);

                String result = "User name: " + name + ", email: " + email + "\n";
                resultTV.append(result);
            } while (cursor.moveToNext());
        } else {
            resultTV.append("Empty db!\n");
        }

        cursor.close();
    }

    private void addDataToDB() {
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Users.COLUMN_USER_NAME, name);
        values.put(Users.COLUMN_USER_EMAIL, email);

        long newRowID = db.insert(Users.TABLE_NAME, null, values);

        String result = "--- Add new row ID " + newRowID + "\n";
        resultTV.append(result);

        nameET.setText("");
        emailET.setText("");
    }

    public void readFromFile(View view) {
        FileInputStream fileInputStream = null;
        try {

            fileInputStream = openFileInput(FILE_NAME);
            byte[] resultBytes = new byte[fileInputStream.available()];
            fileInputStream.read(resultBytes);

            String result = new String(resultBytes);
            resultTV.setText(result);

        } catch (FileNotFoundException e){
            Toast.makeText(this, "File is not exist!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error with read from file!", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error with close input stream!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void writeToFile(View view) {
        FileOutputStream fileOutputStream = null;
        try {

            String result = resultTV.getText().toString();
            byte[] resultBytes = result.getBytes();

            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fileOutputStream.write(resultBytes);

            resultTV.setText("");
        } catch (IOException e) {
            Toast.makeText(this, "Error with write to file!", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error with close output stream!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
