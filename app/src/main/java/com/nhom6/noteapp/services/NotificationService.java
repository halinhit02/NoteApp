package com.nhom6.noteapp.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.extension.Format;
import com.nhom6.noteapp.model.dao.TaskDAO;
import com.nhom6.noteapp.model.dto.Task;
import com.nhom6.noteapp.ui.activity.LoginActivity;
import com.nhom6.noteapp.utils.NotificationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class NotificationService extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private TaskDAO taskDAO;
    ZoneOffset zone = OffsetDateTime.now().getOffset();

    // Tạo class xử lí dịch vụ
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            // Tạo vòng lặp cho service chạy vô hạn
            while (true) {
                try {
                    // Lấy danh sách công việc trong database
                    ArrayList<Task> taskList = taskDAO.getAll();
                    // Duyệt danh sách công việc
                    taskList.forEach((task) -> {
                        // Lấy thời gian, ngày hạn của công việc
                        LocalDateTime localDateTime = Format.formatDateTimeToDate(task.getTime(), task.getDate());
                        // Tính toán thời gian còn lại của công việc
                        long epochSecond = localDateTime.toEpochSecond(zone);
                        long currentEpochSecond = LocalDateTime.now().toEpochSecond(zone);
                        long leftTime = epochSecond - currentEpochSecond;
                        // Nếu hết thời gian và công việc chưa hoàn thành thì thông báo.
                        // Nếu thời gian còn lại nhỏ hơn 1 tiếng thì thông báo.
                        if (leftTime == 0 && task.getDone() == 0) {
                            // Đẩy thông báo công việc đã hết hạn.
                            NotificationUtils.showNotification(getApplicationContext(),
                                    "Công việc " + task.getTitle() + " đã hết hạn."
                            );
                        } else if (leftTime <= 60 * 60 && task.getDone() == 0 && task.getNotified() != 1) {
                            // Gán giá trị cho công việc đã được thông báo.
                            task.setNotified(1);
                            // Lưu công việc vừa sửa.
                            taskDAO.update(task);
                            // Đẩy thông báo công việc sắp hết hạn.
                            NotificationUtils.showNotification(getApplicationContext(),
                                    "Công việc " + task.getTitle() + " sắp hết hạn, hãy hoàn thành nó ngay!"
                            );
                        }
                    });
                    // Dừng thread trong 1s
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            // Dừng chạy Service
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo đối tượng lấy dữ liệu công việc trong database
        taskDAO = new TaskDAO(getApplicationContext());
        Log.d("service creating", "service starting");
        // Tạo một luồng mới để chạy dịch vụ
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        // Khởi tạo lớp xử lý dịch vụ
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        // Gửi message vào trong service
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
