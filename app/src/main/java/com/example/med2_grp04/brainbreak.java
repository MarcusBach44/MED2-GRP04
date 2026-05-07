package com.example.med2_grp04;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class brainbreak extends AppCompatActivity {
    private ImageButton btnTicTacToe;
    private ImageButton btnMineSweeper;
    private ImageButton btnWordle;
    private ImageButton btnSudoku;
    private ArrayList<String> activeSettings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brainbreak);


        btnTicTacToe = findViewById(R.id.btnTicTacToe);
        btnMineSweeper = findViewById(R.id.btnMineSweeper);
        btnWordle = findViewById(R.id.btnWordle);
        btnSudoku = findViewById(R.id.btnSudoku);

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
    }
    private void toggleBrainBreak(String name, ImageButton button) {
        if (!activeSettings.contains(name)) {
            activeSettings.add(name);
            button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.black)));
        } else {
            activeSettings.remove(name);
            button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        }
    }
}