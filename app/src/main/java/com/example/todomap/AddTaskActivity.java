package com.example.todomap;

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

import java.io.IOException;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {
    private Button searchLocBtn, addTodoBtn;

    private EditText titleEditText;
    private EditText typeEditText;
    private EditText timeEditText;
    private EditText addressEditText;
    private TextView latText;
    private TextView lonText;
    private EditText descEditText;
    private TextView statusText;

    private DBManager dbManager;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");

        setContentView(R.layout.activity_add_task);

        titleEditText = (EditText) findViewById(R.id.title_edittext);
        typeEditText = (EditText) findViewById(R.id.type_edittext);
        timeEditText = (EditText) findViewById(R.id.time_edittext);
        addressEditText = (EditText) findViewById(R.id.address_edittext);
        latText = (TextView) findViewById(R.id.lat_text);
        lonText = (TextView) findViewById(R.id.lon_text);
        descEditText = (EditText) findViewById(R.id.desc_edittext);
        statusText = (TextView) findViewById(R.id.status_text);

        statusText.setText(Integer.toString(0));

        addTodoBtn = (Button) findViewById(R.id.add_task_btn);
        searchLocBtn = (Button) findViewById(R.id.search_location_btn);

        geocoder = new Geocoder(this);
        dbManager = new DBManager(this);
        dbManager.open();

        Intent add_task_by_search = getIntent();
        String new_task_lat = add_task_by_search.getStringExtra("new_task_lat");
        String new_task_lon = add_task_by_search.getStringExtra("new_task_lon");
        String new_task_add = add_task_by_search.getStringExtra("new_task_add");
        latText.setText(new_task_lat);
        lonText.setText(new_task_lon);
        addressEditText.setText(new_task_add);

//        Intent add_task_on_map = getIntent();
//        String new_task_lat_1 = add_task_on_map.getStringExtra("new_task_lat_1");
//        String new_task_lon_1 = add_task_on_map.getStringExtra("new_task_lon_1");
//        latText.setText(new_task_lat_1);
//        lonText.setText(new_task_lon_1);

        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (latText.getText().toString() != null &
                        latText.getText().toString() != "Invalid" &
                        lonText.getText().toString() != null &
                        lonText.getText().toString() != "Invalid") {

                    final String title = titleEditText.getText().toString();
                    final String type = typeEditText.getText().toString();
                    final String time = timeEditText.getText().toString();
                    final String address = addressEditText.getText().toString();
                    final Double lat = Double.parseDouble(latText.getText().toString());
                    final Double lon = Double.parseDouble(lonText.getText().toString());
                    final String desc = descEditText.getText().toString();
                    final Integer status = Integer.parseInt(statusText.getText().toString());

                    Log.d("Insert", "Insert: lat: " + lat + ", lon: " + lon + ", status:" + status);
                    dbManager.insert(title, type, time, address, lat, lon, desc, status);

//                Fragment fragment = new TaskFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();

                    Intent main = new Intent(AddTaskActivity.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(main);
                } else {
                    Log.d("insert", "Insert failed! No valid address!");
                }

            }
        });

        searchLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    } else {
                        Log.d("address", "Invalid address! ");
                        String invalid = "Invalid";
                        latText.setText(invalid);
                        lonText.setText(invalid);
                    }

                } catch (IOException e) {
                    Log.d("address", "get location failed!");
                    String invalid = "Invalid";
                    latText.setText(invalid);
                    lonText.setText(invalid);
                    e.printStackTrace();
                }
            }
        });
    }
}
