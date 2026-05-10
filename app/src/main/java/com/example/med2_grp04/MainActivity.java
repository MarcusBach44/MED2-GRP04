package com.example.med2_grp04;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    //private static WeakReference<Window> overlay;
    Button ToSettings_options;
    OverlayProcessor overlayProcessor = new OverlayProcessor();

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
        ToSettings_options = findViewById(R.id.ToSettings_options);

        ToSettings_options.setOnClickListener(v1 -> {

            startActivity(new Intent(
                    MainActivity.this,
                    Settings_Options.class));

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
            System.out.println("Running onCreate in main");
            OverlayManager.inkOverlayGif = new GifDrawable(getResources(), R.drawable.ink_screen_overlay);
            OverlayManager.inkOverlayGif.reset();

            OverlayManager.inkOverlayReverseGif = new GifDrawable(getResources(), R.drawable.ink_screen_overlay_reverse);
            OverlayManager.inkOverlayReverseGif.reset();
            OverlayManager.inkOverlayReverseGif.seekToFrame(47);

            OverlayManager.inkyOverlayIdleGif = new GifDrawable(getResources(), R.drawable.inky_idle);
            OverlayManager.inkyOverlayIdleGif.reset();

            OverlayManager.inkyOverlaySleepingGif = new GifDrawable(getResources(), R.drawable.inky_sleeping);
            OverlayManager.inkyOverlaySleepingGif.reset();

            OverlayManager.inkyOverlayFrustratedGif = new GifDrawable(getResources(), R.drawable.inky_angry);
            OverlayManager.inkyOverlayFrustratedGif.reset();

            OverlayManager.inkyOverlayIntroGif = new GifDrawable(getResources(), R.drawable.inky_walkingintro);
            OverlayManager.inkyOverlayIntroGif.reset();


            System.out.println("Completing onCreate in main");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        findViewById(R.id.btnSwitch).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("Running onClick in MainActivity");
                isOverlayActive = !isOverlayActive;
                if (isOverlayActive){
                    OverlayManager.OpenInkOverlay();
                    OverlayManager.CloseInkOverlay();
                    OverlayManager.InkyIntroAnimation();
                    overlayProcessor.InkyIntroToIdle(1);

                    System.out.println("Completing onClick if statement in MainActivity");
                } else{
                    OverlayManager.CloseInkOverlay();
                    OverlayManager.InkyClose();
                    System.out.println("Completing onClick else statement in MainActivity");
                }

            }

        });
        findViewById(R.id.btnSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change();
            }
        });

        InstigateGames games =
                new InstigateGames(MainActivity.this);

        games.startPopupLoop();
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