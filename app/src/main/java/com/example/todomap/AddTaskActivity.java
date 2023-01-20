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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button addTodoBtn;

    // Date and Time
    private Button dateButton, timeButton;
    private String dateString, timeString;
    private int year, month, day, hour, minute;

    // Location
    private EditText addressEditText;
    private Double lat, lon;
    private Geocoder geocoder;

    // type
    private Spinner taskTypeSpinner;
    private String typeString;

    // Title
    private EditText titleEditText;

    // Description
    private EditText descEditText;

    // sqlite datebase
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");
        setContentView(R.layout.activity_add_task);

        // Address
        addressEditText = (EditText) findViewById(R.id.address_edittext);

        //Title and Description
        titleEditText = (EditText) findViewById(R.id.title_edittext);
        descEditText = (EditText) findViewById(R.id.desc_edittext);

        // Date picker
        dateButton = findViewById(R.id.date_picker_btn);
        dateString = getNowDate();
        dateButton.setText(dateString);

        // Time picker
        timeButton = findViewById(R.id.time_picker_btn);
        timeString = getNowTime();
        timeButton.setText(timeString);

        // Type Spinner
        taskTypeSpinner = findViewById(R.id.task_type_spinner);
        taskTypeSpinner.setOnItemSelectedListener(this);

        String[] taskTypes = getResources().getStringArray(R.array.todo_types);
        ArrayAdapter typeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, taskTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskTypeSpinner.setAdapter(typeAdapter);

        // Add task button
        addTodoBtn = (Button) findViewById(R.id.add_task_btn);

        // Geocoder
        geocoder = new Geocoder(this);
        dbManager = new DBManager(this);
        dbManager.open();

        // Add task by map fragment
        Intent add_task_by_search = getIntent();
        Double new_task_lat = add_task_by_search.getDoubleExtra("new_task_lat", 0);
        Double new_task_lon = add_task_by_search.getDoubleExtra("new_task_lon", 0);
        String new_task_add = add_task_by_search.getStringExtra("new_task_add");
        lat = new_task_lat;
        lon = new_task_lon;
        addressEditText.setText(new_task_add);

        // Add task button onClick
        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String title = titleEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                final String address = addressEditText.getText().toString();

                final String type = typeString;
                final String time = dateString + ", " + timeString;

                if (title.equals("")) {
                    Snackbar.make(v, "Title is required!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (address.equals("")) {
                    Snackbar.make(v, "Address is required!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    //Geocoding
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(address, 1);
                        if (addresses.size() > 0) {
                            Address address1 = addresses.get(0);
                            lat = address1.getLatitude();
                            lon = address1.getLongitude();
//                            latText.setText(Double.toString(lat));
//                            lonText.setText(Double.toString(lon));
                            Log.d("address", "location1: " + lat + lon);

                            // insert a new task
                            dbManager.insert(title, type, time, address, lat, lon, desc);

                            // return to main activity
                            Intent main = new Intent(AddTaskActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(main);

                        } else {
                            Snackbar.make(v, "Invalid address!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            Log.d("address", "Invalid address! ");
//                            latText.setText("0");
//                            lonText.setText("0");
                        }
                    } catch (IOException e) {
                        Snackbar.make(v, "Invalid geocoder!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Log.d("address", "get location failed!");
//                        latText.setText("0");
//                        lonText.setText("0");
                        e.printStackTrace();
                    }

                }
            }
        });
//
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
        hour = cal.get(Calendar.HOUR_OF_DAY) + 1;
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
        hour = cal.get(Calendar.HOUR_OF_DAY) + 1;
        minute = cal.get(Calendar.MINUTE);
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }


    // Type spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.task_type_spinner) {
            String valueFromSpinner = adapterView.getItemAtPosition(position).toString();
            typeString = new String (returnEmoji(valueFromSpinner));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private char[] returnEmoji(String task_class) {
        Log.d("returnEmoji", "returnEmoji: " + task_class);
        if (task_class.equals("All"))
            return Character.toChars(0x1F4CB);
        if (task_class.equals("Work"))
            return Character.toChars(0x1F4BC);
        if (task_class.equals("Study"))
            return Character.toChars(0x1F4D6);
        if (task_class.equals("Life"))
            return Character.toChars(0x1F388);
        if (task_class.equals("Other"))
            return Character.toChars(0x1F30D);
        if (task_class.equals("Done"))
            return Character.toChars(0x2714);
        return Character.toChars(0x1F4CB);
    }

}
