package com.nhom6.noteapp.fragment;

import static androidx.databinding.DataBindingUtil.getBinding;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.activity.MainActivity;
import com.nhom6.noteapp.adapter.Categoryadpter;
import com.nhom6.noteapp.databinding.FragmentCategoryBinding;
import com.nhom6.noteapp.model.DAO.CategoryDAO;
import com.nhom6.noteapp.model.DTO.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CategoryFragment extends Fragment implements Categoryadpter.CategoryClick{

    private  FragmentCategoryBinding binding;

    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_category, container, false);
        return binding.getRoot();
    }

    private CategoryDAO categoryDAO;
    private Categoryadpter categoryadpter;
    private ArrayList<Category> listCategory;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rcvCategory.setLayoutManager(linearLayoutManager);
        categoryDAO = new CategoryDAO(getContext());
        listCategory = categoryDAO.getAll();
        categoryadpter = new Categoryadpter(getContext(),listCategory,this);
        binding.searchCategory.clearFocus();
        binding.searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FinterList(newText);
                return true;
            }
        });

        binding.rcvCategory.setAdapter(categoryadpter);

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                int pos = viewHolder.getLayoutPosition();
//                int targetpos = target.getLayoutPosition();
//                Category category = listCategory.get(pos);
//                listCategory.remove(pos);
//                listCategory.add(targetpos,category);
//                categoryadpter.notifyItemMoved(pos,targetpos);
//                return true;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
//                builder.setTitle("Are you sure you want to delete ?");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        int check = categoryDAO.delete(listCategory.get(viewHolder.getLayoutPosition()).getId());
//                        switch (check){
//                            case  1 :
//                                listCategory.clear();
//                                listCategory.addAll(categoryDAO.getAll());
//                                categoryadpter.notifyDataSetChanged();
//                                Toast.makeText(getContext(),"Deleted successfully",Toast.LENGTH_SHORT).show();
//                                break;
//                            case 0 :
//                                Toast.makeText(getContext(),"Delete failed",Toast.LENGTH_SHORT).show();
//                                break;
//                            default:
//                                break;
//                        }
//
//                    }
//                });
//                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                builder.show();
//            }
//        });
//        itemTouchHelper.attachToRecyclerView(binding.rcvCategory);

        binding.imgAddCategory.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_add_category);
            Window window = dialog.getWindow();
            if(window==null){
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowacc = window.getAttributes();
            windowacc.gravity = Gravity.NO_GRAVITY ;
            window.setAttributes(windowacc);

            EditText edtTitle,edtDes;
            Button btnCancel,btnAdd;
            btnCancel = dialog.findViewById(R.id.btnCancel);
            btnAdd = dialog.findViewById(R.id.btnAdd);
            edtTitle = dialog.findViewById(R.id.edtTitleCategory);
            edtDes = dialog.findViewById(R.id.edtDesCategory);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm-dd/MM/yyyy", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtTitle.getText().toString().trim().isEmpty()){
                        edtTitle.setError("Can not leave the title blank");
                    }if (edtDes.getText().toString().trim().isEmpty()){
                        edtDes.setError("Can not leave the description blank");
                    }else {
                        Category category1 = new Category();
                        category1.setName(edtTitle.getText().toString().trim());
                        category1.setDate(currentDateandTime);
                        category1.setDes(edtDes.getText().toString().trim());
                        long res = categoryDAO.insert(category1);
                        if (res>0){
                            Toast.makeText(getContext(),"Added category successfully",Toast.LENGTH_SHORT).show();
                            listCategory.clear();
                            listCategory.addAll(categoryDAO.getAll());
                            categoryadpter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getContext(),"Add failure category",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }
            });
            btnCancel.setOnClickListener(v1 -> {
                dialog.cancel();
            });

            dialog.show();
        });



        binding.imgLogout.setOnClickListener(v -> {

            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_signout);
            Window window = dialog.getWindow();
            if(window==null){
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowacc = window.getAttributes();
            windowacc.gravity = Gravity.NO_GRAVITY ;
            window.setAttributes(windowacc);


            Button btnCancel,btnOke;
            btnCancel = dialog.findViewById(R.id.btnCancelSignout);
            btnOke = dialog.findViewById(R.id.btnOke);

            btnOke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            btnCancel.setOnClickListener(v1 -> {
                dialog.cancel();
            });

            dialog.show();
        });
    }

    private void FinterList(String text) {
        ArrayList<Category> filteredList=new ArrayList<>();

        for (Category category1: listCategory){
            if (category1.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(category1);
            }

        }
        if (filteredList.isEmpty()){
            Toast.makeText(this.getContext(), "no data", Toast.LENGTH_SHORT).show();
        }else {
            categoryadpter.setFilteredList(filteredList);
        }
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }
}