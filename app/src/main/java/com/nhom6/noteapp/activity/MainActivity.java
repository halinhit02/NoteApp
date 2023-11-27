package com.nhom6.noteapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivityMainBinding;
import com.nhom6.noteapp.model.DAO.CategoryDAO;
import com.nhom6.noteapp.model.DAO.TaskDAO;
import com.nhom6.noteapp.model.DAO.UserDAO;

public class MainActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private TaskDAO taskDAO;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDAO = new UserDAO(this);
        categoryDAO = new CategoryDAO(this);
        taskDAO = new TaskDAO(this);
    }
}