package com.myfirst.storagedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText nameET;
    private SharedPreferences sPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameET = findViewById(R.id.etName);

        Button okBtn = findViewById(R.id.btnOk);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sPrefs = getPreferences(MODE_PRIVATE);
//        or
//        sPrefs = getSharedPreferences("file_name", MODE_PRIVATE);

//        nameET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void saveData() {
        String name = nameET.getText().toString();
        if (!name.isEmpty()) {
            sPrefs.edit()
                    .putString("name_key", name)
                    .apply();
        }
    }

    private void loadData() {
        String name = sPrefs.getString("name_key", "");
        nameET.setText(name);
    }
}
