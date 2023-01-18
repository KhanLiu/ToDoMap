package com.example.todomap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    private Button searchLocBtn, addTodoBtn;


    // Date and Time
    private Button dateButton, timeButton;
    private String dateString, timeString;
    private int year, month, day, hour, minute;

    // Location
    private LocationManager locationManager;
    private Double lat, lon;
    private Geocoder geocoder;

    private EditText titleEditText;
    private EditText typeEditText;
    private EditText addressEditText;
    private TextView latText;
    private TextView lonText;
    private EditText descEditText;
    private TextView statusText;

    private DBManager dbManager;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");
        setContentView(R.layout.activity_add_task);

        titleEditText = (EditText) findViewById(R.id.title_edittext);
        typeEditText = (EditText) findViewById(R.id.type_edittext);
        addressEditText = (EditText) findViewById(R.id.address_edittext);
        latText = (TextView) findViewById(R.id.lat_text);
        lonText = (TextView) findViewById(R.id.lon_text);
        descEditText = (EditText) findViewById(R.id.desc_edittext);
        statusText = (TextView) findViewById(R.id.status_text);

        addTodoBtn = (Button) findViewById(R.id.add_task_btn);
        searchLocBtn = (Button) findViewById(R.id.search_location_btn);

        statusText.setText(Integer.toString(0));

        // Date picker
        dateButton = findViewById(R.id.date_picker_btn);
        dateString = getNowDate();
        dateButton.setText(dateString);

        // Time picker
        timeButton = findViewById(R.id.time_picker_btn);
        timeString = getNowTime();
        timeButton.setText(timeString);

        // Location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
            Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
            if (fineLocationGranted || coarseLocationGranted) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d("location1", "onLocationChanged: " + location.toString());

                lat = location.getLatitude();
                lon = location.getLongitude();

                latText.setText(lat+"");
                lonText.setText(lon+"");

                Geocoder geocoder = new Geocoder(AddTaskActivity.this, Locale.getDefault());

                try {
                    List<Address> address_list = geocoder.getFromLocation(lat, lon, 1);
                    if (address_list.size() != 0) {
                        String address_string = address_list.get(0).getAddressLine(0);
                        addressEditText.setText(address_string);
                    } else {
                        addressEditText.setText("no address found");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Location cannot be obtained due to missing permission.", Toast.LENGTH_LONG).show();
            }
        });
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        locationPermissionRequest.launch(PERMISSIONS);


        geocoder = new Geocoder(this);
        dbManager = new DBManager(this);
        dbManager.open();

        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (latText.getText().toString() != null &
                        latText.getText().toString() != "Invalid" &
                        lonText.getText().toString() != null &
                        lonText.getText().toString() != "Invalid") {

                    final String title = titleEditText.getText().toString();
                    final String type = typeEditText.getText().toString();
                    final String time = dateString + " " + timeString;
                    final String address = addressEditText.getText().toString();
                    final Double lat = Double.parseDouble(latText.getText().toString());
                    final Double lon = Double.parseDouble(lonText.getText().toString());
                    final String desc = descEditText.getText().toString();
                    final Integer status = Integer.parseInt(statusText.getText().toString());

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

    // Date
    public void openDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                dateString = getMonthFormat(month) + " " + day + " " + year;
                dateButton.setText(dateString);
            }
        };
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";
        // default
        return "JAN";
    }

    // Time
    public void openTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                timeButton.setText(timeString);
            }
        };
        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    // Now Date
    private String getNowDate() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        return getMonthFormat(month) + " " + day + " " + year;
    }

    // Now time
    private String getNowTime() {
        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        month = month + 1;
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }
}
