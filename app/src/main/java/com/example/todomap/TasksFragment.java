package com.example.todomap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private View view;
    private ArrayList<Task> taskArrayList;
    private DBManager dbManager;
    private Cursor cursor;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tasks, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataInitialize();

        recyclerView = (RecyclerView) view.findViewById(R.id.tasks_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayout.VERTICAL));
        MyRecyclerviewAdapter myRecyclerviewAdapter = new MyRecyclerviewAdapter(getContext(), taskArrayList, new OnItemClickListener() {
            @Override
            public void onClick(Task task) {

                Snackbar.make(view, "Item Clicked" + task.get_id(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                Toast.makeText(getContext(), "Item Clicked" + task.get_id(), Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(myRecyclerviewAdapter);
        myRecyclerviewAdapter.notifyDataSetChanged();


        // Folating buttion "add task"
        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent add_mem = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(add_mem);
            }
        });

    }

    private void dataInitialize() {

        taskArrayList = new ArrayList<>();
        Task task;

        dbManager = new DBManager(getActivity());
        dbManager.open();
        cursor = dbManager.fetch();
        int length = cursor.getCount();

        int _idIndex = cursor.getColumnIndex(DatabaseHelper._ID);
        int titleIndex = cursor.getColumnIndex(DatabaseHelper.TITLE);
        int typeIndex = cursor.getColumnIndex(DatabaseHelper.TYPE);
        int descIndex = cursor.getColumnIndex(DatabaseHelper.DESC);
        int timeIndex = cursor.getColumnIndex(DatabaseHelper.TIME);
        int addressIndex = cursor.getColumnIndex(DatabaseHelper.ADDRESS);
        int latIndex = cursor.getColumnIndex(DatabaseHelper.LAT);
        int lonIndex = cursor.getColumnIndex(DatabaseHelper.LON);
        int statusIndex = cursor.getColumnIndex(DatabaseHelper.STATUS);

        for (int i = 0; i < length; i++) {
            task = new Task(
                    Integer.parseInt(cursor.getString(_idIndex)),
                    cursor.getString(titleIndex),
                    cursor.getString(typeIndex),
                    cursor.getString(descIndex),
                    cursor.getString(timeIndex),
                    cursor.getString(addressIndex),
                    Double.parseDouble(cursor.getString(latIndex)),
                    Double.parseDouble(cursor.getString(lonIndex)),
                    Integer.parseInt(cursor.getString(statusIndex))
            );

            taskArrayList.add(task);
            cursor.moveToNext();
        }

    }
}