package com.example.med2_grp04;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    public ForegroundService() {
    }
    @Nullable @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Window window = new Window(this);
        window.Open();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}

