package com.example.cosc326_l5;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

public class MyService extends Service {

    // Create as separate class below
    private final IBinder binder = new LocalBinder();
    private ArrayList<String> channels = new ArrayList<String>();
    int nCount = 0;

    public class LocalBinder extends Binder {
        MyService getService() {
//          Return current instance of class
            return MyService.this;
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // When other participants UNBIND from the service
    @Override
    public boolean onUnbind(Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        for (int x = 0; x < channels.size(); x++) {
            notificationManager.deleteNotificationChannel(channels.get(x));
        }
        channels = new ArrayList<String>();
        return true;
    }

    public void createChannel(String id, String name, String description, int importance) {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        channels.add(id);
    }

    public void createNotification(String channel, String message, String title, PendingIntent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (intent != null) {
            builder.setContentIntent(intent);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_DENIED) {
            return;
        }
//        Had to add POST_NOTIFICATIONS permission in manifest.xml
        notificationManager.notify(nCount, builder.build());
        nCount++;
    }

    public void deleteLast() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        nCount--;
        notificationManager.cancel(nCount);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        createChannel("startserv", "StartService Channel", "Channel for notifications created by started services.", 4);
        for(int x = 0; x < 100; x++) {
            createNotification("startserv", "Hello World! I'm a started service!", "Message From Started Service", null);
        }
        return START_NOT_STICKY;
    }
}