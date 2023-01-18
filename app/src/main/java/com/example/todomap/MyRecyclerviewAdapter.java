package com.example.todomap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerviewAdapter extends RecyclerView.Adapter<MyRecyclerviewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Task> taskArrayList;
    private OnItemClickListener listener;

    public MyRecyclerviewAdapter(Context context, ArrayList<Task> taskArrayList, OnItemClickListener listener) {
        this.context = context;
        this.taskArrayList = taskArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.task_item2, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerviewAdapter.MyViewHolder holder, int position) {

        Task task = taskArrayList.get(position);
        holder.titleText.setText(task.title);
        holder.typetext.setText(task.type);
        holder.descText.setText(task.description);
        holder.timeText.setText(task.time);
        holder.addressText.setText(task.address);

        holder.bind(task, listener);
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView titleText;
        TextView descText;
        TextView typetext;
        TextView timeText;
        TextView addressText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_textview);
            descText = itemView.findViewById(R.id.desc_textview);
            typetext = itemView.findViewById(R.id.type_textview);
            timeText = itemView.findViewById(R.id.time_textview);
            addressText = itemView.findViewById(R.id.address_textview);


        }
        public void bind(Task task, OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onClick(task);
                }
            });
        }
    }

}

