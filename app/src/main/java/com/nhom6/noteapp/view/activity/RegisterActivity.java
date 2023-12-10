package com.nhom6.noteapp.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivityRegisterBinding;
import com.nhom6.noteapp.model.DAO.UserDAO;
import com.nhom6.noteapp.model.DTO.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private ActivityRegisterBinding binding;
    private String username,name,password,repassword;
    private ArrayList<User> userArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(view->{
            userDAO = new UserDAO(this);
            userArrayList = userDAO.getAll();
            username = binding.editTextRegisterUsername.getText().toString();
            name = binding.editTextRegisterName.getText().toString();
            password = binding.editTextRegisterPass.getText().toString();
            repassword = binding.editTextRegisterRepass.getText().toString();
            //Kiểm tra mật khẩu
            String password_regex =
                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
            Pattern pattern = Pattern.compile(password_regex);
            Matcher matcher = pattern.matcher(password);
            if(username.isEmpty()){
                binding.editTextRegisterUsername.setError("Username cannot be blank");
            }else if (password.isEmpty()){
                binding.editTextRegisterPass.setError("Password cannot be blank");
            }else if (repassword.isEmpty()){
                binding.editTextRegisterRepass.setError("Comfirm password cannot be blank");
            } else if (name.isEmpty()) {
                binding.editTextRegisterName.setError("Name cannot be blank");
            } else if (!password.equals(repassword)) {
                binding.editTextRegisterRepass.setError("Confirmation password does not match");

            } else if (!matcher.matches()) {
                showDialog("Your password is too weak");
            } else if (!checkNewUserName()) {
                showDialog("Username available");
            } else {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                User user = new User(name, username, hashedPassword);
                try {
                    userDAO.insert(user);
                    showDialog("Registration successful, please log in to use the service");
                    finish();
                } catch (Exception e1) {
                    showDialog("Registration failed, there was an error connecting to the database");
                }
            }
        });
        binding.imgPreview.setOnClickListener(v->{
            finish();
        });
    }


    private boolean checkNewUserName(){
        for (User item:userArrayList) {
            if(username.equals(item.getUserName()))
              return false;
        }
        return true;
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