package com.nhom6.noteapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.model.DTO.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewholder> {

    private ArrayList<Task> listData;
    private Context mContext;

    public TaskAdapter(ArrayList<Task> list, Context mContext){
        this.listData = list;
        this.mContext = mContext
    }

    @NonNull
    @Override
    public TaskViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =((Activity)mContext).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_task,parent,false);
        return new TaskViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public  class TaskViewholder extends RecyclerView.ViewHolder {
        public TaskViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
