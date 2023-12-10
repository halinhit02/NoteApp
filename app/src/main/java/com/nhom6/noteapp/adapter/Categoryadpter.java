package com.nhom6.noteapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.model.DAO.CategoryDAO;
import com.nhom6.noteapp.model.DTO.Category;

import java.util.ArrayList;

public class Categoryadpter extends RecyclerView.Adapter<Categoryadpter.CategoryViewholder> {


    private ArrayList<Category> listData;
    private Context context;
    private CategoryDAO categoryDAO;
    private CategoryClick categoryClick;

    public Categoryadpter(Context context,ArrayList<Category> list,CategoryClick categoryClick){
        this.context= context;
        this.listData = list;
        categoryDAO = new CategoryDAO(context);
        this.categoryClick = categoryClick;
    }

    public void setFilteredList(ArrayList<Category> filteredList){
        this.listData=filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itemcategory,parent,false);
        return new CategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {
        holder.bind(listData.get(position));
        holder.itemView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Are you sure you want to delete ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int check = categoryDAO.delete(listData.get(holder.getLayoutPosition()).getId());
                        switch (check){
                            case  1 :
                                listData.clear();
                                listData.addAll(categoryDAO.getAll());
                                notifyDataSetChanged();
                                Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show();
                                break;
                            case 0 :
                                Toast.makeText(context,"Delete failed",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            return false;
        });
        holder.itemView.setOnClickListener(v -> {
            holder.onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class   CategoryViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvTitleCategory,tvTime,tvDes;
    LinearLayout category;
        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            tvTitleCategory = itemView.findViewById(R.id.tvTitleCategory);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDes = itemView.findViewById(R.id.tvDes);
            category = itemView.findViewById(R.id.category);
            itemView.setOnClickListener(this);
        }
        public void bind(Category category){
            tvTitleCategory.setText(category.getName());
            tvTime.setText(category.getDate());
            tvDes.setText(category.getDes());
        }

        @Override
        public void onClick(View v) {
            categoryClick.onClick(v,getLayoutPosition());
        }
    }
    public interface CategoryClick {
        void onClick(View view, int position);

    }


}
