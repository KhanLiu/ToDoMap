package com.example.todomap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModifyTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button updateBtn, deleteBtn;

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

    private long _id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");
        setContentView(R.layout.activity_modify_task);

        Intent intent = getIntent();

        Integer _id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");
        String time = intent.getStringExtra("time");
        String address = intent.getStringExtra("address");
        String desc = intent.getStringExtra("desc");

//        _id = Long.parseLong(id);

        // Address
        addressEditText = (EditText) findViewById(R.id.address_edittext);

        //Title and Description
        titleEditText = (EditText) findViewById(R.id.title_edittext);
        descEditText = (EditText) findViewById(R.id.desc_edittext);
        titleEditText.setText(title);
        descEditText.setText(desc);

        // Date picker
        dateButton = findViewById(R.id.date_picker_btn);
//        dateString = getNowDate();
        dateButton.setText(dateString);

        // Time picker
        timeButton = findViewById(R.id.time_picker_btn);
//        timeString = getNowTime();
        timeButton.setText(timeString);

        // Type Spinner
        taskTypeSpinner = findViewById(R.id.task_type_spinner);
        taskTypeSpinner.setOnItemSelectedListener(this);

        String[] taskTypes = getResources().getStringArray(R.array.todo_types);
        ArrayAdapter typeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, taskTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskTypeSpinner.setAdapter(typeAdapter);
        taskTypeSpinner.setSelection(returnSpinnerPos(type.toString()));

        // Add task button
        updateBtn = (Button) findViewById(R.id.btn_update);

        // Geocoder
        geocoder = new Geocoder(this);
        dbManager = new DBManager(this);
        dbManager.open();

//        updateBtn.setOnClickListener(this);
//        deleteBtn.setOnClickListener(this);
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_update:
//
//                if (latText.getText().toString() != null &
//                        latText.getText().toString() != "Invalid" &
//                        lonText.getText().toString() != null &
//                        lonText.getText().toString() != "Invalid") {
//
//                    String title = titleEditText.getText().toString();
//                    String type = typeEditText.getText().toString();
//                    String time = timeEditText.getText().toString();
//                    String address = addressEditText.getText().toString();
//                    Double lat = Double.parseDouble(latText.getText().toString());
//                    Double lon = Double.parseDouble(lonText.getText().toString());
//                    String desc = descEditText.getText().toString();
////                    Integer status = Integer.parseInt(statusText.getText().toString());
//
//                    dbManager.update(_id, title, type, time, address, lat, lon, desc);
//                    this.returnHome();
//                } else {
//                    Log.d("insert", "Insert failed! No valid address!");
//                }
//                break;
//
//            case R.id.btn_delete:
//                dbManager.delete(_id);
//                this.returnHome();
//                break;
//
//            case R.id.search_location_btn:
//                try {
//                    String address = addressEditText.getText().toString();
//                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
//                    if (addresses.size() > 0) {
//                        Address address1 = addresses.get(0);
//                        Double lat = address1.getLatitude();
//                        Double lon = address1.getLongitude();
//                        latText.setText(Double.toString(lat));
//                        lonText.setText(Double.toString(lon));
//                        Log.d("address", "location1: " + lat + lon);
//                    }else{
//                        Log.d("address", "Invalid address! ");
//                        String invalid = "Invalid";
//                        latText.setText(invalid);
//                        lonText.setText(invalid);
//                    }
//
//                } catch (IOException e) {
//                    String invalid = "Invalid";
//                    latText.setText(invalid);
//                    lonText.setText(invalid);
//                    Log.d("address", "get location failed!" );
//                    e.printStackTrace();
//                }
//
//                break;
//        }
//    }

    public void returnHome() {

//        Fragment fragment = new TaskFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();

        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
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


//    // Now Date
//    private String getNowDate() {
//        Calendar cal = Calendar.getInstance();
//        year = cal.get(Calendar.YEAR);
//        month = cal.get(Calendar.MONTH);
//        day = cal.get(Calendar.DAY_OF_MONTH);
//        month = month + 1;
//        return getMonthFormat(month) + " " + day + " " + year;
//    }

//    // Now time
//    private String getNowTime() {
//        Calendar cal = Calendar.getInstance();
//        hour = cal.get(Calendar.HOUR_OF_DAY) + 1;
//        minute = cal.get(Calendar.MINUTE);
//        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
//    }


    // Type spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.task_type_spinner) {
            String valueFromSpinner = adapterView.getItemAtPosition(position).toString();
            typeString = new String(returnEmoji(valueFromSpinner));
//            typeEditText.setText(new String(returnEmoji(valueFromSpinner)));
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

    private int returnSpinnerPos(String emoji) {
        emoji = new String(emoji);
        Log.d("returnEmojiPos", "returnSpinnerPos: " + emoji);
        if (emoji.equals(new String(Character.toChars(0x1F4CB))))
            return 0;
        if (emoji.equals(new String(Character.toChars(0x1F4BC))))
            return 1;
        if (emoji.equals(new String(Character.toChars(0x1F4D6))))
            return 2;
        if (emoji.equals(new String(Character.toChars(0x1F388))))
            return 3;
        if (emoji.equals(new String(Character.toChars(0x1F30D))))
            return 4;
        if (emoji.equals(new String(Character.toChars(0x2714))))
            return 5;
        return 0;


    }


}
