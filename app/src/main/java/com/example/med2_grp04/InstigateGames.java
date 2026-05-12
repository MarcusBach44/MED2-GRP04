package com.example.med2_grp04;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class InstigateGames {

    public static boolean isMinigameActive = false;
    private Context context;
    private View popupView;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private LayoutInflater layoutInflater;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d("TAG", "run: ");
            startPopupLoop();
        }
    };
    private ArrayList<String> enabledGames = new ArrayList<>();
    public static int popUpTimer = 180000;

    public InstigateGames(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_minigame,null);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.x = 50;
        params.y = 300;
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
    }

    public void Open(){
        try{
            if (popupView.getWindowToken()==null){
                if (popupView.getParent()==null){
                    windowManager.addView(popupView, params);
                }
            }
        }catch (Exception e){
            Log.d("Error Open", e.toString());
        }
    }

    public void Close(){
        try{
            ((WindowManager)context.getSystemService(WINDOW_SERVICE)).removeView(popupView);
            popupView.invalidate();
            ((ViewGroup)popupView.getParent()).removeAllViews();
        } catch (Exception e){
            Log.d("Error Close", e.toString());
        }
    }

    public void startPopupLoop() {
        cancelPopupLoop();
        handler.postDelayed(runnable,popUpTimer);
        handler.postDelayed(this::showMiniGamePopup,popUpTimer);
    }
    public void cancelPopupLoop(){
        handler.removeCallbacks(runnable);
    }

    private Intent ChooseMinigame(){
        Random random = new Random();
        String selectedGame = enabledGames.get(random.nextInt(enabledGames.size()));
        Intent intent;

        switch (selectedGame) {
            case "TicTacToe":
                intent = new Intent(
                        context,
                        TicTacToe.class);
                break;

            case "MineSweeper":
                intent = new Intent(
                        context,
                        Minigame1.class);
                break;

            case "Wordle":
                intent = new Intent(
                        Intent.ACTION_VIEW,
                        android.net.Uri.parse(
                                "https://wordleunlimited.org/"));
                break;

            case "Sudoku":
                intent = new Intent(
                        Intent.ACTION_VIEW,
                        android.net.Uri.parse(
                                "https://sudoku.com/"));
                break;

            default:
                intent = null;
        }
        if (intent != null){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }
    private void showMiniGamePopup() {
        refreshEnabledGames();
        if (enabledGames.isEmpty()) {
            return;
        }

        Open();
        popupView.findViewById(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMinigameActive = true;
                Intent intent = ChooseMinigame();
                if (intent == null){
                    return;
                }
                Close();
                OverlayManager.CloseInkOverlay();
                OverlayManager.InkyClose();
                handler.removeCallbacks(runnable);
                context.startActivity(ChooseMinigame());
            }
        });
        // remove after 10 seconds
        handler.postDelayed(this::Close, 10000);


    }
    private void refreshEnabledGames() {

        enabledGames.clear();

        SharedPreferences prefs = context.getSharedPreferences("BrainBreakPrefs", Context.MODE_PRIVATE);

        String settingsString = prefs.getString("active_games", "");

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