package com.example.med2_grp04;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {
    public static boolean isOverlayActive = false;


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
         findViewById(R.id.btnBrainBreak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, brainbreak.class);
                startActivity(intent);
            }
        });

        CheckOverlayPermission();
        CheckAccessibilityPermission();
        StartService();

        try {
            OverlayManager.inkOverlayGif = new GifDrawable(getResources(), R.drawable.ink_screen_overlay);
            OverlayManager.inkOverlayGif.reset();

            OverlayManager.inkOverlayReverseGif = new GifDrawable(getResources(), R.drawable.ink_screen_overlay_reverse);
            OverlayManager.inkOverlayReverseGif.seekToFrame(47);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        findViewById(R.id.btnSwitch).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isOverlayActive = !isOverlayActive;
                if (isOverlayActive){
                    OverlayManager.OpenInkOverlay();
                } else{
                    OverlayManager.CloseInkOverlay();
                }
            }
        });
        findViewById(R.id.btnSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change();
            }
        });
    }


    public void Change(){
        Intent intent = new Intent(this, SettingsRestrictedApps.class);
        intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
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