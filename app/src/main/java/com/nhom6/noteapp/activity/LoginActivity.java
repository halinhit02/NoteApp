package com.nhom6.noteapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivityLoginBinding;
import com.nhom6.noteapp.model.DAO.UserDAO;
import com.nhom6.noteapp.model.DTO.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private UserDAO userDAO;


    private ActivityLoginBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setContentView(binding.getRoot());

        userDAO = new UserDAO(this);
        binding.btnLogin.setOnClickListener(view->{
            String userName = binding.editTextUsername.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();

            if (checkLogin(userName,password,userDAO.getUserByUsername(userName))){
                User user = userDAO.getUserByUsername(userName);
                String name = user.getName();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("Name", name);
                startActivity(intent);
            }
        });
        binding.tvMoveRegister.setOnClickListener(view->{
            Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent1);
        });
    }

    private boolean checkLogin(String userName,String password,User user){

        if(userName.isEmpty()){
            binding.editTextUsername.setError("Username  cannot be blank");
            return false;
        } if( password.isEmpty()){
            binding.editTextUsername.setError("Password  cannot be blank");
            return false;
        }
        else if (user==null) {
            showDialog("Username does not exist");
            return false;
        } else {
            boolean isPasswordCorrect = BCrypt.checkpw(password, user.getPassword());
            if (!isPasswordCorrect) {
                showDialog("Wrong password");
                return false;
            } else {
               return true;
            }
        }
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