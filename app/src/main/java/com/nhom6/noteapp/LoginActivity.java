package com.nhom6.noteapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom6.noteapp.model.DAO.UserDAO;
import com.nhom6.noteapp.model.DTO.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText editText_username = findViewById(R.id.editText_username);
        EditText editText_password = findViewById(R.id.editText_password);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_move_register = findViewById(R.id.btn_move_register);
        userDAO = new UserDAO(this);
        btn_login.setOnClickListener(view->{
            String username = editText_username.getText().toString();
            String password = editText_password.getText().toString();
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

        btn_move_register.setOnClickListener(view->{
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