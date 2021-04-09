package com.myfirst.storagedemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myfirst.storagedemo.managers.ContentManager;
import com.myfirst.storagedemo.models.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "result.txt";

    private EditText nameET;
    private EditText emailET;

    private TextView resultTV;

    private final ContentManager contentManager = ContentManager.instance;

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
        int deletedRows = contentManager.clearData();

        String result = "--- Deleted " + deletedRows + " row(s)\n";
        resultTV.append(result);
    }

    private void readDataFromDB() {
        ArrayList<User> users = contentManager.readData();

        if (users.isEmpty()) {
            resultTV.append("Empty db!\n");
            return;
        }

        for (User user : users) {
            String result = "User name: " + user.getName() + ", email: " + user.getEmail() + "\n";
            resultTV.append(result);
        }
    }

    private void addDataToDB() {
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        User user = new User(name, email);

        long id = contentManager.addData(user);

        String result = "--- Add new row ID " + id + "\n";
        resultTV.append(result);

        nameET.setText("");
        emailET.setText("");
    }

    public void readFromFile(View view) {
        try (FileInputStream fileInputStream = openFileInput(FILE_NAME)) {

            byte[] resultBytes = new byte[fileInputStream.available()];
            fileInputStream.read(resultBytes);

            String result = new String(resultBytes);
            resultTV.setText(result);

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File is not exist!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error with read from file!", Toast.LENGTH_SHORT).show();
        }
    }

    public void writeToFile(View view) {

        try (FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE)) {

            String result = resultTV.getText().toString();
            byte[] resultBytes = result.getBytes();

            fileOutputStream.write(resultBytes);

            resultTV.setText("");
        } catch (IOException e) {
            Toast.makeText(this, "Error with write to file!", Toast.LENGTH_SHORT).show();
        }
    }
}
