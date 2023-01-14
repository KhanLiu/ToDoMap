package com.example.todomap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TaskFragment extends Fragment {

    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    View view;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {


        dbManager = new DBManager(getActivity());
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) view.findViewById(R.id.task_list_view);
        listView.setEmptyView(view.findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.task_item, cursor, from, to, 0);
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

                Intent modify_intent = new Intent(getActivity().getApplicationContext(), ModifyTaskActivity.class);
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task_view, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    // menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(getActivity(), AddTaskActivity.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task, container, false);
        return view;
    }


}