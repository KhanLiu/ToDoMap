package com.example.todomap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private Button addTodoBtn;
    private EditText titleEditText;
    private EditText typeEditText;
    private EditText timeEditText;
    private EditText addressEditText;
    private EditText latEditText;
    private EditText lonEditText;
    private EditText descEditText;
    private EditText statusEditText;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");

        setContentView(R.layout.activity_add_task);

        titleEditText = (EditText) findViewById(R.id.title_edittext);
        typeEditText = (EditText) findViewById(R.id.type_edittext);
        timeEditText = (EditText) findViewById(R.id.time_edittext);
        addressEditText = (EditText) findViewById(R.id.address_edittext);
        latEditText = (EditText) findViewById(R.id.lat_edittext);
        lonEditText = (EditText) findViewById(R.id.lon_edittext);
        descEditText = (EditText) findViewById(R.id.desc_edittext);
        statusEditText = (EditText) findViewById(R.id.status_edittext);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                final String title = titleEditText.getText().toString();
                final String type = typeEditText.getText().toString();
                final String time = timeEditText.getText().toString();
                final String address = addressEditText.getText().toString();
                final String lat = latEditText.getText().toString();
                final String lon = lonEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                final String status = statusEditText.getText().toString();

                dbManager.insert(title, type, time, address, lat, lon, desc, status);

//                Fragment fragment = new TaskFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();

                Intent main = new Intent(AddTaskActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }
    }

}
