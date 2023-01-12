package com.example.todomap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TaskViewActivity extends AppCompatActivity {

    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[]{
            DatabaseHelper._ID,
            DatabaseHelper.TITLE,
            DatabaseHelper.TYPE,
            DatabaseHelper.TIME,
            DatabaseHelper.ADDRESS,
            DatabaseHelper.DESC,
            DatabaseHelper.STATUS
    };

    final int[] to = new int[]{
            R.id.id,
            R.id.title,
            R.id.type,
            R.id.time,
            R.id.address,
            R.id.desc,
            R.id.status,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.task_list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.task_item, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView titleTextView = (TextView) view.findViewById(R.id.title);
                TextView typeTextView = (TextView) view.findViewById(R.id.type);
                TextView timeTextView = (TextView) view.findViewById(R.id.time);
                TextView addressTextView = (TextView) view.findViewById(R.id.address);
                TextView descTextView = (TextView) view.findViewById(R.id.desc);
                TextView statusTextView = (TextView) view.findViewById(R.id.status);

                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String type = typeTextView.getText().toString();
                String time = timeTextView.getText().toString();
                String address = addressTextView.getText().toString();
                String desc = descTextView.getText().toString();
                String status = statusTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyTaskActivity.class);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("type", type);
                modify_intent.putExtra("time", time);
                modify_intent.putExtra("address", address);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("status", status);

                startActivity(modify_intent);
            }
        });
    }

    // menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_view, menu);
        return true;
    }

    // menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddTaskActivity.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }

    // map button
    public void onClickStartMapsActivity(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}