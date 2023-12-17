package com.nhom6.noteapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ItemTaskBinding;
import com.nhom6.noteapp.model.dao.TaskDAO;
import com.nhom6.noteapp.model.dto.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewholder> {

    private ArrayList<Task> listData;
    private Context mContext;
    private TaskDAO taskDAO;
    private TaskClick taskClick;

    public TaskAdapter(ArrayList<Task> list, Context mContext,TaskClick taskClick){
        this.listData = list;
        this.mContext = mContext;
        taskDAO = new TaskDAO(mContext);
        this.taskClick = taskClick;
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
        holder.itemView.setOnLongClickListener (v-> {
            taskClick.onLongClick(listData.get(position));
            return false;
        });
        holder.itemView.setOnClickListener( v-> {
            taskClick.onClick(listData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public  class TaskViewholder extends RecyclerView.ViewHolder  implements View.OnClickListener {

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

        @Override
        public void onClick(View v) {

        }
    }

    public interface TaskClick {
        void onLongClick(Task item);
        void onClick(Task item);
    }

}
