package com.nhom6.noteapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
            String username = binding.editTextUsername.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();
            List<User> userList = userDAO.getUserByUsername(username);
            if(username.isEmpty() || password.isEmpty()){
                showDialog("Bạn phải nhập đầy đủ thông tin");
            } else if (userList.size()==0) {
                showDialog("Tên đăng nhập không tồn tại");
            } else {
                User user = userList.get(0);
                boolean isPasswordCorrect = BCrypt.checkpw(password, user.getPassword());
                if(!isPasswordCorrect) {
                   showDialog("Sai mật khẩu");
                }
                else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });

        binding.tvMoveRegister.setOnClickListener(view->{
            Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent1);
        });
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Đóng", (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}