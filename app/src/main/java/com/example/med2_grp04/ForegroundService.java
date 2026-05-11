package com.example.med2_grp04;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForegroundService extends Service {
    private InkOverlayWindow inkOverlayWindow;
    static public ArrayList<String> restrictedApps = new ArrayList<String>();
    private InkyOverlayWindow InkyOverlay;
    private OverlayProcessor overlayProcessor;
    public ForegroundService() {
    }
    @Nullable @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter("APP_CHANGED");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiver, filter, RECEIVER_EXPORTED);
            Log.d("RECEIVER", "Open");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            StartMyOwnForeground();
        }else{
            startForeground(1, new Notification());
        }
        inkOverlayWindow = new InkOverlayWindow(this);
        OverlayManager.updateInkOverlayWindow(inkOverlayWindow);

        overlayProcessor = new OverlayProcessor();


        InkyOverlay = new InkyOverlayWindow(this);
        OverlayManager.updateOverlayInkyWindow(InkyOverlay);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pkg = intent.getStringExtra("package");

            InstigateGames.currentPackage = pkg;

            Log.d("RECEIVED", "Package Received " +pkg);

            if (pkg == null || !MainActivity.isOverlayActive) {
                return;
            }
            if (isRestricted(pkg)){
                System.out.println("Running IsResticted in ForegroundService");
                Log.d("RESTRICTED", "Show Overlay");
                if(Settings_Options.isNightMode(ForegroundService.this) == true){
                    OverlayManager.InkySleepingAnimation();
                } else {
                OverlayManager.InkyIntroAnimation();
                overlayProcessor.InkyIntroToIdle(12);
                }
                System.out.println("Completing IsResticted in ForegroundService");
            } else {
                if (restrictedApps.contains(DetectAppChanges.previousApp)){
                    OverlayManager.OpenInkOverlay();
                }else {
                    Log.d("NOT RESTRICTED", "Hide Overlay");
                    System.out.println("Running IsNotResticted in ForegroundService");
                    Log.d("NOT RESTRICTED", "Hide Overlay");
                    OverlayManager.CloseInkOverlay();
                    OverlayManager.InkyClose();
                    System.out.println("Completing IsNotResticted in ForegroundService");
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isRestricted(String pkg){
        for (String app : restrictedApps) {
            if (app.equals(pkg)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void StartMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_MIN);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Service running")
                .setContentText("Displaying over other apps")

                // this is important, otherwise the notification will show the way
                // you want i.e. it will show some default notification
                .setSmallIcon(R.drawable.ic_launcher_foreground)

                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
}

