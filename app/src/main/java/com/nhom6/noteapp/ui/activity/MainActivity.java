package com.nhom6.noteapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivityMainBinding;
import com.nhom6.noteapp.extension.Constance;
import com.nhom6.noteapp.model.dto.User;
import com.nhom6.noteapp.ui.fragment.CategoryFragment;
import com.nhom6.noteapp.model.dao.CategoryDAO;
import com.nhom6.noteapp.model.dao.TaskDAO;
import com.nhom6.noteapp.model.dao.UserDAO;

public class MainActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private TaskDAO taskDAO;
    private ActivityMainBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        user = (User) getIntent().getSerializableExtra(Constance.KEY_USER);

        setContentView(binding.getRoot());
        userDAO = new UserDAO(this);
        categoryDAO = new CategoryDAO(this);
        taskDAO = new TaskDAO(this);


        addFragment();
    }

    private void addFragment() {
        Bundle data = new Bundle();
        data.putSerializable(Constance.KEY_USER,user);
        replaceFragment(new CategoryFragment(),data);
    }

    private void replaceFragment(Fragment fragment, Bundle data) {
        fragment.setArguments(data);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

    }

}