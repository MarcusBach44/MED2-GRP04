package com.example.med2_grp04;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class InstigateGames {

    public static boolean isMinigameActive = false;

    private Context context;

    private WindowManager windowManager;

    private View popupView;

    private Handler handler = new Handler();

    public static String currentPackage = "";

    public InstigateGames(Context context) {

        this.context = context;

        windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void startPopupLoop() {

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                checkAndShowPopup();

                // repeating section
                handler.postDelayed(this, 30000);
            }

        }, 10000);
    }

    private void checkAndShowPopup() {

        if (isMinigameActive) {
            return;
        }

        if (popupView != null) {
            return;
        }

       // if (!ForegroundService.restrictedApps.contains(currentPackage)) {
         //   return;
       // }

        showMiniGamePopup();
    }

    private void showMiniGamePopup() {

        LayoutInflater inflater =
                LayoutInflater.from(context);

        popupView =
                inflater.inflate(R.layout.popup_minigame,
                        null);

        int layoutType;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            layoutType =
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        } else {

            layoutType =
                    WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(

                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,

                        layoutType,

                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,

                        PixelFormat.TRANSLUCENT
                );

        params.gravity = Gravity.BOTTOM | Gravity.END;

        params.x = 50;
        params.y = 300;

        Button playButton =
                popupView.findViewById(R.id.playButton);

        playButton.setOnClickListener(v -> {

            isMinigameActive = true;

            Intent intent =
                    new Intent(context,
                            TicTacToe.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

            removePopup();
        });

        windowManager.addView(popupView, params);

        // 5 secs rn
        handler.postDelayed(this::removePopup, 5000);
    }

    private void removePopup() {

        if (popupView != null) {

            try {

                windowManager.removeView(popupView);

            } catch (Exception ignored) {
            }

            popupView = null;
        }
    }
}