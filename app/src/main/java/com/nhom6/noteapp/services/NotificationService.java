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

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            while (true) {
                try {
                    ArrayList<Task> taskList = taskDAO.getAll();
                    taskList.stream().filter((task -> task.getNotified() != 1)).forEach((task) -> {
                        String[] timeStr = task.getTime().split(":");
                        LocalTime localTime = LocalTime.of(Integer.parseInt(timeStr[0]), Integer.parseInt(timeStr[1]));
                        String[] date = task.getDate().split("/");
                        LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]), localTime.getHour(), localTime.getMinute());
                        long epochSecond = localDateTime.toEpochSecond(zone);
                        long currentEpochSecond = LocalDateTime.now().toEpochSecond(zone);
                        long minus = epochSecond - currentEpochSecond;
                        if (minus <= 60 * 60 * 24 && minus >= 0 && task.getDone() == 0) {
                            task.setNotified(1);
                            taskDAO.update(task);
                            NotificationUtils.showNotification(getApplicationContext(),
                                    "Công việc " + task.getTitle() + " sắp hết hạn, hãy hoàn thành nó ngay!"
                            );
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        taskDAO = new TaskDAO(getApplicationContext());
        Log.d("service creating", "service starting");
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
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
