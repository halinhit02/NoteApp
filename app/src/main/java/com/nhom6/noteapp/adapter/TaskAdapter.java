package com.nhom6.noteapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ItemTaskBinding;
import com.nhom6.noteapp.model.DTO.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewholder> {

    private ArrayList<Task> listData;
    private Context mContext;

    public TaskAdapter(ArrayList<Task> list, Context mContext){
        this.listData = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TaskViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_task, parent, false);
        return new TaskViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewholder holder, int position) {
        holder.bind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public  class TaskViewholder extends RecyclerView.ViewHolder {


        private ItemTaskBinding binding ;
        public TaskViewholder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Task task){
            if (task != null){
                binding.tvTitleTask.setText(task.getTitle());
                binding.tvTimeTask.setText(task.getTime() + " date " + task.getDate());
                binding.tvDesTask.setText(task.getDes());
                binding.tvPoinTask.setText(task.getScore());
            }

        }
    }


}
