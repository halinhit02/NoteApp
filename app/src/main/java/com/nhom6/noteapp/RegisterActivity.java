package com.nhom6.noteapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.nhom6.noteapp.model.DAO.UserDAO;
import com.nhom6.noteapp.model.DTO.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private UserDAO userDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText editText_username = findViewById(R.id.editText_register_username);
        EditText editText_name = findViewById(R.id.editText_register_name);
        EditText editText_password = findViewById(R.id.editText_register_pass);
        EditText editText_repassword = findViewById(R.id.editText_register_repass);
        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(view->{
            userDAO = new UserDAO(this);
            ArrayList<User> userArrayList = userDAO.getAll();
            String username = editText_username.getText().toString();
            String name = editText_name.getText().toString();
            String password = editText_password.getText().toString();
            String repassword = editText_repassword.getText().toString();

            //Kiểm tra tên đăng nhập
            boolean isNewUsername = true;
            for (User item:userArrayList) {
                if(username.equals(item.getUserName())){
                    isNewUsername = false;
                    break;
                }
            }

            //Kiểm tra mật khẩu
            String password_regex =
                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
            Pattern pattern = Pattern.compile(password_regex);
            Matcher matcher = pattern.matcher(password);
            if(username.isEmpty() || password.isEmpty() || repassword.isEmpty()){
                showDialog("Bạn phải nhập đầy đủ thông tin");
            } else if (!password.equals(repassword)) {
               showDialog("Mật khẩu xác nhận không trùng khớp");
            } else if (!matcher.matches()) {
                showDialog("Mật khẩu của bạn quá yếu");
            } else if (!isNewUsername) {
                showDialog("Tên đăng nhập đã tồn tại");
            } else {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                User user = new User(name, username, hashedPassword);
                try {
                    userDAO.insert(user);
                    showDialog("Đăng kí thành công, vui lòng đăng nhập để sử dụng dịch vụ");
                } catch (Exception e1) {
                    showDialog("Đăng kí thất bại, đã xảy ra lỗi khi kết nối đến cơ sở dữ liệu");
                }
            }
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