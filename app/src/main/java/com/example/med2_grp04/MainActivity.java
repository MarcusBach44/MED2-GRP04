package com.example.med2_grp04;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.IOException;
import java.lang.ref.WeakReference;
import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {
    public static boolean isOverlayActive = false;
    private static WeakReference<Window> overlay;
    public static GifDrawable inkOverlayGif;
    public static GifDrawable inkOverlayReverseGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        CheckOverlayPermission();
        CheckAccessibilityPermission();
        StartService();

        try {
            inkOverlayGif = new GifDrawable(getResources(), R.drawable.ink_screen_overlay);
            inkOverlayReverseGif = new GifDrawable(getResources(), R.drawable.ink_screen_overlay_reverse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //ImageView im = (ImageView) overlay.get().mView.findViewById(R.id.ink_overlay);
        //im.setImageDrawable(inkOverlayGif);
        inkOverlayGif.reset();
        inkOverlayReverseGif.seekToFrame(47);

        findViewById(R.id.DisableButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isOverlayActive = !isOverlayActive;
                if (isOverlayActive){
                    OpenOverlay();
                } else{
                    CloseOverlay();
                }
            }
        });
    }

    public static void OpenOverlay(){
        inkOverlayReverseGif.pause();
        ImageView im = (ImageView) overlay.get().mView.findViewById(R.id.ink_overlay);
        im.setImageDrawable(inkOverlayGif);
        inkOverlayGif.seekToFrame((inkOverlayReverseGif.getCurrentFrameIndex()-48)*-1);
        inkOverlayGif.setSpeed(0.05f);
        inkOverlayGif.start();
        overlay.get().Open();
    }
    public static void CloseOverlay(){
        inkOverlayGif.pause();
        ImageView im = (ImageView) overlay.get().mView.findViewById(R.id.ink_overlay);
        im.setImageDrawable(inkOverlayReverseGif);
        inkOverlayReverseGif.seekToFrame((inkOverlayGif.getCurrentFrameIndex()-48)*-1);
        inkOverlayReverseGif.setSpeed(0.05f);
        inkOverlayReverseGif.start();
        overlay.get().Close();
    }

    public static void updateOverlayWindow(Window window){
        overlay = new WeakReference<>(window);
    }
    public void CheckOverlayPermission(){
        if (!Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        }
    }

    public void CheckAccessibilityPermission(){
        if (isAccessibilityEnabled(this)){
            return;
        }
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    public boolean isAccessibilityEnabled(Context context) {
        String service = context.getPackageName() + "/" + DetectAppChanges.class.getCanonicalName();

        int enabled = 0;
        try {
            enabled = Settings.Secure.getInt(
                    context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED
            );
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }

        if (enabled == 1) {
            String settingValue = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );

            if (settingValue != null && settingValue.contains(service)) {
                return true;
            }
        }
        return false;
    }

    public void StartService(){
        if (Settings.canDrawOverlays(this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, ForegroundService.class));
            } else {
                startService(new Intent(this, ForegroundService.class));
            }
        }else{
            startService(new Intent(this, ForegroundService.class));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Toast.makeText(this, "FUCK", Toast.LENGTH_SHORT).show();
        return super.onSupportNavigateUp();
    }
}