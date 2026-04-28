package com.example.med2_grp04;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    private Window window;
    private String[] restrictedApps = {
            "com.example.med2_grp04",
            "com.reddit.frontpage",
            "com.google.android.youtube",
            "com.instagram.android",
            "com.zhiliaoapp.musically"
    };
    public ForegroundService() {
    }
    @Nullable @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Implemented");
    }

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
        window = new Window(this);
        MainActivity.updateOverlayWindow(window);
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

            if (pkg == null || !MainActivity.isOverlayActive) {
                return;
            }

            if (isRestricted(pkg)){
                Log.d("RESTRICTED", "Show Overlay");
                MainActivity.OpenOverlay();
            } else {
                Log.d("NOT RESTRICTED", "Hide Overlay");
                MainActivity.CloseOverlay();
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

