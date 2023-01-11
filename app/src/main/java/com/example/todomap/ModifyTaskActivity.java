package com.example.todomap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyTaskActivity extends Activity implements View.OnClickListener {


    private EditText titleEditText;
    private EditText typeEditText;
    private EditText timeEditText;
    private EditText addressEditText;
    private EditText descEditText;
    private EditText statusEditText;

    private Button updateBtn, deleteBtn;


    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.activity_modify_task);

        dbManager = new DBManager(this);
        dbManager.open();

        titleEditText = (EditText) findViewById(R.id.title_edittext);
        typeEditText = (EditText) findViewById(R.id.type_edittext);
        timeEditText = (EditText) findViewById(R.id.time_edittext);
        addressEditText = (EditText) findViewById(R.id.address_edittext);
        descEditText = (EditText) findViewById(R.id.desc_edittext);
        statusEditText = (EditText) findViewById(R.id.status_edittext);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");
        String time = intent.getStringExtra("time");
        String address = intent.getStringExtra("address");
        String desc = intent.getStringExtra("desc");
        String status = intent.getStringExtra("status");


        _id = Long.parseLong(id);

        titleEditText.setText(title);
        typeEditText.setText(type);
        timeEditText.setText(time);
        addressEditText.setText(address);
        descEditText.setText(desc);
        statusEditText.setText(status);


        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:

                String title = titleEditText.getText().toString();
                String type = typeEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String desc = descEditText.getText().toString();
                String status = statusEditText.getText().toString();

                dbManager.update(_id, title, type, time, address, desc, status);
                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), TaskViewActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
