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
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ForegroundService extends Service {
    private InkOverlayWindow inkOverlayWindow;
    static public ArrayList<String> restrictedApps = new ArrayList<String>();
    private InkyOverlayWindow InkyOverlay;
    private OverlayProcessor overlayProcessor;
    private InstigateGames instigateGameWindow;
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

        instigateGameWindow = new InstigateGames(this);
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
            Log.d("RECEIVED", "Package Received " +pkg);

            if (pkg == null || !MainActivity.isInkOverlayActive && !MainActivity.isInkyActive) { return; }

            if (isRestricted(pkg)){
                Log.d("RESTRICTED", "Show Overlay");

                //instigateGameWindow.Open();
                //Check Ink Overlay
                if (MainActivity.isInkOverlayActive){
                    OverlayManager.OpenInkOverlay();
                }
                //Check Inky
                if (MainActivity.isInkyActive) {
                    if (Settings_Options.isNightMode(ForegroundService.this)) {
                        overlayProcessor.InkyIsSleeping();
                    } else {
                        overlayProcessor.InkyIntroToIdle(12);
                    }
                }
                instigateGameWindow.startPopupLoop();
            } else {
                //Check if the previous app was restricted, so it does not remove overlay when it shows itself
                if (!isRestricted(DetectAppChanges.previousPackage)) {
                    Log.d("NOT RESTRICTED", "Hide Overlay");
                    instigateGameWindow.cancelPopupLoop();
                    instigateGameWindow.Close();
                    //Check Ink Overlay
                    if (MainActivity.isInkOverlayActive){
                        OverlayManager.CloseInkOverlay();
                    }
                    //Check Inky
                    if (MainActivity.isInkyActive){
                        overlayProcessor.InkyIsClose();
                    }
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

