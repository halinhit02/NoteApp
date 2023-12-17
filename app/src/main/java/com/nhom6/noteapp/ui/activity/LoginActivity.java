package com.nhom6.noteapp.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivityLoginBinding;
import com.nhom6.noteapp.model.dao.UserDAO;
import com.nhom6.noteapp.model.dto.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private User user;
    private ActivityLoginBinding binding;
    String userName, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setContentView(binding.getRoot());

        userDAO = new UserDAO(this);
        binding.btnLogin.setOnClickListener(view -> {
            userName = binding.editTextUsername.getText().toString().trim();
            password = binding.editTextPassword.getText().toString().trim();

            if (checkLogin(userName, password)) {
                LoginSuccess();
            }
        });
        binding.tvMoveRegister.setOnClickListener(view -> {
            Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent1);
        });
    }
    private boolean checkBlank(){
        boolean allSuccess = true;
        if (userName.isEmpty()) {
            binding.editTextUsername.setError("Username cannot be blank");
            allSuccess = false;
        }
        if (password.isEmpty()) {
            binding.editTextPassword.setError("Password cannot be blank");
            allSuccess = false;
        }
        return allSuccess;
    }
    private boolean checkLogin(String userName, String password) {
        if(checkBlank()){
            ArrayList<User> users = userDAO.getUserByUsername(userName);
            //check khác rỗng
            if(users.size() == 0) {
                showDialog("Username is not available");
                return false;
            }else {
                //check pass
                user = users.get(0);
                if (!BCrypt.checkpw(password, user.getPassword())) {
                    showDialog("Wrong password or username");
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private void LoginSuccess(){
        String name = user.getName();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("Name", name);
        startActivity(intent);
        finish();
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Close", (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}