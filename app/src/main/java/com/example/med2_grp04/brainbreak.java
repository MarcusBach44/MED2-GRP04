package com.example.med2_grp04;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class brainbreak extends AppCompatActivity {
    private ImageButton btnBackArrow;
    private ImageButton btnTicTacToe;
    private ImageButton btnMineSweeper;
    private ImageButton btnWordle;
    private ImageButton btnSudoku;
    private ArrayList<String> activeSettings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brainbreak);
        btnBackArrow=findViewById(R.id.btnBackArrow);

        btnTicTacToe = findViewById(R.id.btnTicTacToe);
        btnMineSweeper = findViewById(R.id.btnMineSweeper);
        btnWordle = findViewById(R.id.btnWordle);
        btnSudoku = findViewById(R.id.btnSudoku);

        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
            }
        });
        btnTicTacToe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                toggleBrainBreak("TicTacToe", btnTicTacToe);
            }
        });
        btnMineSweeper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toggleBrainBreak("MineSweeper", btnMineSweeper);
            }
        });
        btnWordle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toggleBrainBreak("Wordle", btnWordle);
            }
        });
        btnSudoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBrainBreak("Sudoku", btnSudoku);
            }
        });
        loadSettings();
    }
    private void toggleBrainBreak(String name, ImageButton button) {
        if (!activeSettings.contains(name)) {
            activeSettings.add(name);
            button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.lightgrey)));

            button.setImageTintList(null);
            button.clearColorFilter();
        } else {
            activeSettings.remove(name);
            button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.grey)));

            button.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.argb(150, 100, 100, 100)));
        }
        saveSettings();
    }
    private void saveSettings() {
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("BrainBreakPrefs", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        String settingsString = String.join(",", activeSettings);
        editor.putString("active_games", settingsString);
        editor.apply();
    }
    private void loadSettings() {
        setButtonState(btnTicTacToe, false);
        setButtonState(btnMineSweeper, false);
        setButtonState(btnWordle, false);
        setButtonState(btnSudoku, false);

        android.content.SharedPreferences sharedPreferences = getSharedPreferences("BrainBreakPrefs", MODE_PRIVATE);
        String settingsString = sharedPreferences.getString("active_games", "");

        if (!settingsString.isEmpty()) {
            String[] games = settingsString.split(",");
            for (String game : games) {
                if (!activeSettings.contains(game)) {
                    activeSettings.add(game);
                    updateButtonUI(game);
                }
            }
        }
    }

    private void updateButtonUI(String name) {
        ImageButton btn = null;
        if (name.equals("TicTacToe")) btn = btnTicTacToe;
        else if (name.equals("MineSweeper")) btn = btnMineSweeper;
        else if (name.equals("Wordle")) btn = btnWordle;
        else if (name.equals("Sudoku")) btn = btnSudoku;

        if (btn != null) {
            btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.lightgrey)));
            btn.setImageTintList(null);
            btn.clearColorFilter();
        }
    }
    private void setButtonState(ImageButton btn, boolean active) {
        if (btn == null) return;
        if (active) {
            btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.lightgrey)));
            btn.setImageTintList(null);
            btn.clearColorFilter();
        } else {
            btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            btn.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.argb(150, 100, 100, 100)));
        }
    }
}