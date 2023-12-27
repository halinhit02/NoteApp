package com.nhom6.noteapp.utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.ui.activity.LoginActivity;

import java.util.Random;

public class NotificationUtils {

    static final String CHANNEL_ID = "mychannel";
    static final String name = "My Channel";
    static final String description = "This is an channel";

    // Hàm tạo một kênh thông báo
    private static void createNotificationChannel(Context context, String channelId, String name, String description) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        // Tạo một kênh thông báo với channel Id, name và importance đã khai báo.
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        // Gán mô tả cho kênh thông báo.
        channel.setDescription(description);
        // Khởi tạo một trình quản lí thông báo.
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        // Tạo một kênh thông báo đã được khởi tạo ở trên.
        notificationManager.createNotificationChannel(channel);
    }

    // Hàm tạo và hiển thị thông báo lên thanh trạng thái
    public static void showNotification(Context activity, String message) {
        // Khai báo intent để khi người dùng bấm vào thông báo sẽ trỏ tới.
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        // Khởi tạo một kênh thông báo
        createNotificationChannel(activity.getApplicationContext(), CHANNEL_ID, name, description);
        // Khởi tạo một builder cho thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                // Gán tiêu đề cho thông báo
                .setContentTitle(activity.getString(R.string.app_name))
                // Gán nội dung thông báo
                .setContentText(message)
                // Khai báo độ ưu tiên cho thông báo
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Gán intent, khi người dùng nhấn thông báo sẽ trỏ tới
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // Khởi tạo một quản lý thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        // Kiểm tra quyền thông báo đã được cho phép
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Hiển thị thông báo lên thanh trạng thái
        notificationManager.notify(new Random().nextInt(100), builder.build());
    }
}
