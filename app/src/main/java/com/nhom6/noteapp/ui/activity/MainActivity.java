package com.nhom6.noteapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivityMainBinding;
import com.nhom6.noteapp.extension.Constance;
import com.nhom6.noteapp.model.dto.User;
import com.nhom6.noteapp.services.NotificationService;
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
        checkNotificationPermission();
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

    void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.POST_NOTIFICATIONS)) {
            Toast.makeText(this, "Need notification permission.", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Need notification permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}