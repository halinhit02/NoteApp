package com.nhom6.noteapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ItemFullTaskBinding;
import com.nhom6.noteapp.databinding.ItemTaskBinding;
import com.nhom6.noteapp.model.dto.Task;

import java.util.ArrayList;

public class TaskSendAdapter extends RecyclerView.Adapter<TaskSendAdapter.TaskViewholder> {
    private final ArrayList<Task> listData;

    public TaskSendAdapter(ArrayList<Task> list){
        this.listData = list;
    }

    @NonNull
    @Override
    public TaskViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFullTaskBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_full_task, parent, false);
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

    public  class TaskViewholder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        private final ItemFullTaskBinding binding ;
        public TaskViewholder(@NonNull ItemFullTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Task task){
            if (task != null){
                binding.nameTask.setText(task.getTitle());
                binding.timeTask.setText(task.getTime() + " - " + task.getDate());
                binding.desTask.setText(task.getDes());
                binding.statusTask.setText(task.getDone() == 1 ? "Đã xong" : "Chưa hoàn thành");
                binding.spinnerScore.setText(task.getScore());
                binding.noteTask.setText(task.getNote());
            }

        }

        @Override
        public void onClick(View v) {

        }
    }
}
