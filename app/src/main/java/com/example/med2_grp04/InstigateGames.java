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
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Random;

public class InstigateGames {

    public static boolean isMinigameActive = false;

    private Context context;

    private WindowManager windowManager;

    private View popupView;

    private Handler handler = new Handler();

    private ArrayList<String> enabledGames =
            new ArrayList<>();

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
                handler.postDelayed(this, 10000);
            }

        }, 10000);
    }

    private void checkAndShowPopup() {

        if (isMinigameActive) {
            return;
        }

        if (popupView != null &&
                popupView.getVisibility() == View.VISIBLE) {
            return;
        }

        if (!ForegroundService.restrictedApps.contains(currentPackage)) {
            return;
        }

        refreshEnabledGames();

        if (enabledGames.isEmpty()) {
            return;
        }

        showMiniGamePopup();
    }

    private void showMiniGamePopup() {

        int layoutType;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        } else {

            layoutType = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(

                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,

                        layoutType,

                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,

                        PixelFormat.TRANSLUCENT
                );

        params.gravity = Gravity.BOTTOM | Gravity.END;

        params.x = 50;
        params.y = 300;

        LayoutInflater inflater = LayoutInflater.from(context);

        if (popupView == null) {

            popupView =
                    inflater.inflate(R.layout.popup_minigame, null);

            Button playButton = popupView.findViewById(R.id.playButton);

            playButton.setOnClickListener(v -> {

                isMinigameActive = true;

                Random random = new Random();

                String selectedGame = enabledGames.get(random.nextInt(enabledGames.size()));

                Intent intent;

                switch (selectedGame) {

                    case "TicTacToe":
                        intent = new Intent(context, TicTacToe.class);
                        break;

                    case "MineSweeper":
                        intent = new Intent(context, Minigame1.class);
                        break;

                    case "Wordle":
                        intent = new Intent(Intent.ACTION_VIEW,
                                android.net.Uri.parse("https://wordleunlimited.org/"));
                        break;

                    case "Sudoku":
                        intent = new Intent(Intent.ACTION_VIEW,
                                android.net.Uri.parse("https://sudoku.com/"));
                        break;
                    default:
                        return;
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

                if (selectedGame.equals("Wordle") ||
                        selectedGame.equals("Sudoku")) {

                    handler.postDelayed(() -> {
                        isMinigameActive = false;}, 120000);
                }

                removePopup();
            });

            windowManager.addView(popupView, params);
        }

        popupView.setVisibility(View.VISIBLE);

        // remove after 5 seconds
        handler.postDelayed(this::removePopup, 5000);
    }

    private void removePopup() {

        if (popupView != null) {

            try {

                popupView.setVisibility(View.GONE);

            } catch (Exception ignored) {
            }
        }
    }

    private void refreshEnabledGames() {

        enabledGames.clear();

        SharedPreferences prefs =
                context.getSharedPreferences(
                        "BrainBreakPrefs",
                        Context.MODE_PRIVATE);

        String settingsString =
                prefs.getString("active_games", "");

        if (settingsString.contains("TicTacToe")) {
            enabledGames.add("TicTacToe");
        }

        if (settingsString.contains("MineSweeper")) {
            enabledGames.add("MineSweeper");
        }

        if (settingsString.contains("Wordle")) {
            enabledGames.add("Wordle");
        }

        if (settingsString.contains("Sudoku")) {
            enabledGames.add("Sudoku");
        }

    }
}