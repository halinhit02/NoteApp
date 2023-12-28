package com.nhom6.noteapp.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivityRegisterBinding;
import com.nhom6.noteapp.model.dao.UserDAO;
import com.nhom6.noteapp.model.dto.User;
import com.nhom6.noteapp.utils.SystemUtils;

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
        SystemUtils.setLocale(this);
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(view->{
            userDAO = new UserDAO(this);
            userArrayList = userDAO.getAll();
            getBinding();
            if(checkEditText()){
                if(checkLogic()){
                    register();
                }
            }
        });
        binding.imgPreview.setOnClickListener(v->{
            finish();
        });
    }

    private void getBinding(){
        username = binding.editTextRegisterUsername.getText().toString();
        name = binding.editTextRegisterName.getText().toString();
        password = binding.editTextRegisterPass.getText().toString();
        repassword = binding.editTextRegisterRepass.getText().toString();
    }
    private boolean checkNewUserName(){
        for (User item:userArrayList) {
            if(username.equals(item.getUserName()))
              return false;
        }
        return true;
    }

    private boolean checkEditText(){
        boolean allsuccess = true;
        if(username.isEmpty()){
            binding.editTextRegisterUsername.setError("Username cannot be blank");
            allsuccess = false;
        }
        if (password.isEmpty()){
            binding.editTextRegisterPass.setError("Password cannot be blank");
            allsuccess = false;
        }
        if (repassword.isEmpty()){
            binding.editTextRegisterRepass.setError("Comfirm password cannot be blank");
            allsuccess = false;
        }
        if (name.isEmpty()) {
            binding.editTextRegisterName.setError("Name cannot be blank");
            allsuccess = false;
        }
        if (!password.equals(repassword)) {
            binding.editTextRegisterRepass.setError("Confirmation password does not match");
            allsuccess = false;
        }
        return allsuccess;
    }

    private boolean checkLogic(){
        String password_regex =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(password_regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            showDialog("Your password is too weak");
            return false;
        } else if (!checkNewUserName()) {
            showDialog("Username available");
            return false;
        } else {
            return true;
        }
    }
    private void register(){
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(name, username, hashedPassword);
        try {
            userDAO.insert(user);
            showDialog2("Registration successful, please log in to use the service");
        } catch (Exception e1) {
            showDialog2("Registration failed, there was an error connecting to the database");
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

    private void showDialog2(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Close", (dialog, id) -> {
                    dialog.dismiss();
                    finish();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}