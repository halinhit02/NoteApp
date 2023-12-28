package com.nhom6.noteapp.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ActivitySendBinding;
import com.nhom6.noteapp.model.dao.TaskDAO;
import com.nhom6.noteapp.model.dto.Task;
import com.nhom6.noteapp.ui.adapter.TaskSendAdapter;
import com.nhom6.noteapp.utils.SystemUtils;

import java.util.ArrayList;

public class SendActivity extends AppCompatActivity {

    private ActivitySendBinding binding;
    private ArrayList<Task> listTask;
    private TaskSendAdapter taskAdapter;
    private String body = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khai báo binding để ánh xạ tới layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);
        SystemUtils.setLocale(this);
        setContentView(binding.getRoot());

        // Lấy dữ liệu danh mục gồm id và tên gửi từ màn hình trước
        Bundle bundle = getIntent().getExtras();
        String categoryName = bundle.getString("categoryName");
        int categoryId = bundle.getInt("categoryId");
        binding.tvTitle.setText(categoryName);

        // khai báo đối tượng để lấy dữ liệu công việc trong database
        TaskDAO taskDAO = new TaskDAO(this);
        // Lấy danh sách công việc dùng đối tượng vừa tạo.
        listTask = taskDAO.getAllByCategory(String.valueOf(categoryId));
        // Khai báo adapter cho recyclerView công việc
        taskAdapter = new TaskSendAdapter(listTask);
        binding.rcvTaskSend.setLayoutManager(new LinearLayoutManager(this));
        // Gán adapter cho view
        binding.rcvTaskSend.setAdapter(taskAdapter);

        // Bắt sự kiện nhấn nút back
        binding.imgBack.setOnClickListener(v -> {
            finish();
        });
        // Băt sự kiện nhấn nút gửi
        binding.imgSend.setOnClickListener(v -> {
            // Lọc ra số lượng công việc chưa hoàn thành
            long inProgressTaskCount = listTask.stream().filter(task -> task.getDone() != 1).count();
            // Kiểm tra nếu còn công việc chưa hoàn thành thì cảnh báo gửi báo cáo công việc.
            if (inProgressTaskCount > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Thông báo")
                        .setMessage("Bạn chưa hoàn thành tất cả công việc, vẫn muốn tiếp tục?")
                        .setNegativeButton("Huỷ", (dialogInterface, i) -> {
                            // Tắt dialog, không gửi Email
                            dialogInterface.dismiss();
                        })
                        .setPositiveButton("Tiếp tục", (dialogInterface, i) -> {
                            // Gọi hàm gửi Email báo cáo công việc.
                            sendEmail();
                        });
                builder.show();
            } else {
                // Nếu tất cả công việc đã hoàn thành thì gọi hàm sendEmail
                sendEmail();
            }
        });
    }

    // Gửi Email kèm nội dung đã có
    void sendEmail() {
        // Duyệt danh sách công việc tạo nội dung Email.
        listTask.forEach(task -> {
            if (!body.isEmpty()) {
                body += "\n";
            }
            String status = task.getDone() == 1 ? "Đã xong" : "Chưa hoàn thành";
            body += task.getTitle() + ", " + task.getTime() + " - " + task.getDate() + ", " + status + ", " + task.getScore() + " điểm, " + task.getNote();
        });
        // Khai báo intent gọi tới ứng dụng gửi Email
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        // Gán nội dung subject của Email
        i.putExtra(Intent.EXTRA_SUBJECT, "Báo cáo danh sách kết quả công việc.");
        // Gán nội dung của Email
        i.putExtra(Intent.EXTRA_TEXT, body);
        // Mở ứng dụng gửi Email trong thiết bị
        try {
            startActivity(Intent.createChooser(i, "Gửi kết quả qua email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Không có ứng dụng gửi Email nào.", Toast.LENGTH_SHORT).show();
        }
    }
}