package com.nhom6.noteapp.view.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nhom6.noteapp.Constance;
import com.nhom6.noteapp.R;
import com.nhom6.noteapp.adapter.Categoryadpter;
import com.nhom6.noteapp.databinding.DialogAddCategoryBinding;
import com.nhom6.noteapp.databinding.FragmentCategoryBinding;
import com.nhom6.noteapp.model.DAO.CategoryDAO;
import com.nhom6.noteapp.model.DTO.Category;
import com.nhom6.noteapp.view.viewmodel.SharedViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

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
    private SharedViewModel sharedViewModel;

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

        binding.imgAddCategory.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogAddCategoryBinding bindingDialog = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout. dialog_add_category, null, false);
            dialog.setContentView(bindingDialog.getRoot());
            Window window = dialog.getWindow();
            if(window==null){
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowacc = window.getAttributes();
            windowacc.gravity = Gravity.NO_GRAVITY ;
            window.setAttributes(windowacc);


            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm-dd/MM/yyyy", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            bindingDialog.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bindingDialog.edtTitleCategory.getText().toString().trim().isEmpty()){
                        bindingDialog.edtTitleCategory.setError("Can not leave the title blank");
                    }if (bindingDialog.edtDesCategory.getText().toString().trim().isEmpty()){
                        bindingDialog.edtDesCategory.setError("Can not leave the description blank");
                    }else {
                        Category category1 = new Category();
                        category1.setName(bindingDialog.edtTitleCategory.getText().toString().trim());
                        category1.setDate(currentDateandTime);
                        category1.setDes(bindingDialog.edtDesCategory.getText().toString().trim());
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
            bindingDialog.btnCancel.setOnClickListener(v1 -> {
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

    private  void replaceFragment(Fragment fragment, Bundle data) {
        fragment.setArguments(data);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    @Override
    public void onClick(Category item) {
        Bundle data = new Bundle();
        data.putSerializable(Constance.KEY_CATEGORY,item);
        replaceFragment(new TaskFragment(),data);
//        sharedViewModel.setNameData(listCategory.get(position).getName());
    }

}