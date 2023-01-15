package com.example.todomap;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class ModifyTaskActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText titleEditText;
    private EditText typeEditText;
    private EditText timeEditText;
    private EditText addressEditText;
    private TextView latText;
    private TextView lonText;
    private EditText descEditText;
    private TextView statusText;

    private Button searchLocBtn, updateBtn, deleteBtn;


    private long _id;

    private DBManager dbManager;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.activity_modify_task);

        geocoder = new Geocoder(this);
        dbManager = new DBManager(this);
        dbManager.open();

        titleEditText = (EditText) findViewById(R.id.title_edittext);
        typeEditText = (EditText) findViewById(R.id.type_edittext);
        timeEditText = (EditText) findViewById(R.id.time_edittext);
        addressEditText = (EditText) findViewById(R.id.address_edittext);
        latText = (TextView) findViewById(R.id.lat_text);
        lonText = (TextView) findViewById(R.id.lon_text);
        descEditText = (EditText) findViewById(R.id.desc_edittext);
        statusText = (TextView) findViewById(R.id.status_text);

        searchLocBtn = (Button) findViewById(R.id.search_location_btn);
        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");
        String time = intent.getStringExtra("time");
        String address = intent.getStringExtra("address");
        Double lat = intent.getDoubleExtra("latitude", 0.00);
        Double lon = intent.getDoubleExtra("longitude", 0.00);
        String desc = intent.getStringExtra("desc");
        Integer status = intent.getIntExtra("status", 0);


        _id = Long.parseLong(id);

        titleEditText.setText(title);
        typeEditText.setText(type);
        timeEditText.setText(time);
        addressEditText.setText(address);
        latText.setText(Double.toString(lat));
        lonText.setText(Double.toString(lon));
        descEditText.setText(desc);
        statusText.setText(Integer.toString(status));

        searchLocBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:

                if (latText.getText().toString() != null &
                        latText.getText().toString() != "Invalid" &
                        lonText.getText().toString() != null &
                        lonText.getText().toString() != "Invalid") {

                    String title = titleEditText.getText().toString();
                    String type = typeEditText.getText().toString();
                    String time = timeEditText.getText().toString();
                    String address = addressEditText.getText().toString();
                    Double lat = Double.parseDouble(latText.getText().toString());
                    Double lon = Double.parseDouble(lonText.getText().toString());
                    String desc = descEditText.getText().toString();
                    Integer status = Integer.parseInt(statusText.getText().toString());

                    dbManager.update(_id, title, type, time, address, lat, lon, desc, status);
                    this.returnHome();
                } else {
                    Log.d("insert", "Insert failed! No valid address!");
                }
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;

            case R.id.search_location_btn:
                try {
                    String address = addressEditText.getText().toString();
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    if (addresses.size() > 0) {
                        Address address1 = addresses.get(0);
                        Double lat = address1.getLatitude();
                        Double lon = address1.getLongitude();
                        latText.setText(Double.toString(lat));
                        lonText.setText(Double.toString(lon));
                        Log.d("address", "location1: " + lat + lon);
                    }else{
                        Log.d("address", "Invalid address! ");
                        String invalid = "Invalid";
                        latText.setText(invalid);
                        lonText.setText(invalid);
                    }

                } catch (IOException e) {
                    String invalid = "Invalid";
                    latText.setText(invalid);
                    lonText.setText(invalid);
                    Log.d("address", "get location failed!" );
                    e.printStackTrace();
                }

                break;
        }
    }

    public void returnHome() {

//        Fragment fragment = new TaskFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();

        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
