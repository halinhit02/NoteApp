package com.nhom6.noteapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivityMainBinding;
import com.nhom6.noteapp.fragment.CategoryFragment;
import com.nhom6.noteapp.model.DAO.CategoryDAO;
import com.nhom6.noteapp.model.DAO.TaskDAO;
import com.nhom6.noteapp.model.DAO.UserDAO;

public class MainActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private TaskDAO taskDAO;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        userDAO = new UserDAO(this);
        categoryDAO = new CategoryDAO(this);
        taskDAO = new TaskDAO(this);
        replaceFragment(new CategoryFragment());
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

}