package com.example.med2_grp04;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Minigame1 extends AppCompatActivity {

    GridLayout gridLayout;

    private final int gridSize = 10;
    private final int mineCount = 15;

    private Tile[][] board;
    private Button[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame1);

        gridLayout = findViewById(R.id.gridLayout);
        Button test = new Button(this);
        test.setText("HELLO");
        gridLayout.addView(test);

        setupGame();
    }

    private void setupGame() {

        board = new Tile[gridSize][gridSize];
        buttons = new Button[gridSize][gridSize];

        for (int r = 0; r < gridSize; r++){
            for (int c = 0; c < gridSize; c++){
                board[r][c] = new Tile();
            }
        }

        placeMines();
        calculateNumbers();
        createBoardUI();

    }

    private void placeMines() {

        Random random = new Random();
        int placed = 0;

        while (placed < mineCount) {

            int r = random.nextInt(gridSize);
            int c = random.nextInt(gridSize);

            if (!board[r][c].isMine) {
                board[r][c].isMine = true;
                placed++;
            }
        }

    }

    private void calculateNumbers() {

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++){

                if (board[r][c].isMine) continue;

                int count = 0;

                for (int i = -1; i <= 1; i++){
                    for (int j = -1; j <= 1; j++){


                        int nr = r + i;
                        int nc = c + j;

                        if (nr >= 0 && nr < gridSize  &&
                                nc >= 0 && nc < gridSize &&
                                board[nr][nc].isMine){
                            count++;
                        }
                    }
                }

                board[r][c].neighborMines = count;
            }
        }
    }

    private void createBoardUI() {

        gridLayout.removeAllViews();

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++){

                int finalR = r;
                int finalC = c;

                Button button = new Button(this);
                button.setBackgroundColor(Color.LTGRAY);
                button.setPadding(0,0,0,0);

                GridLayout.LayoutParams params =
                        new GridLayout.LayoutParams(
                                GridLayout.spec(r, 1f),
                                GridLayout.spec(c, 1f)
                        );

                params.width = 0;
                params.height = 0;
                params.setMargins(2, 2, 2, 2);

                button.setLayoutParams(params);

                button.setOnClickListener(v -> revealTile(finalR, finalC));

                button.setOnLongClickListener(v -> {

                    if (!board[finalR][finalC].isRevealed) {
                        button.setText("⚑");
                    }

                    return true;
                });

                buttons[r][c] = button;
                gridLayout.addView(button);
            }
        }
    }

    private void revealTile(int r, int c) {

        Tile tile = board[r][c];

        if (tile.isRevealed) return;

        tile.isRevealed = true;

        Button button = buttons[r][c];

        if (tile.isMine) {

            button.setText("\uD83D\uDCA3");
            gameOver(false);
            return;
        }

        if (tile.neighborMines > 0) {
            button.setText(String.valueOf(tile.neighborMines));
            button.setBackgroundColor(Color.WHITE);
        } else {
            button.setBackgroundColor(Color.WHITE);

            for (int i = -1; i <= 1; i++){
                for (int j = -1; j <= 1; j++){

                    int nr = r + i;
                    int nc = c + j;

                    if (nr >= 0 && nr < gridSize &&
                            nc >= 0 && nc < gridSize) {

                        revealTile(nr, nc);
                    }
                }
            }
        }

        checkWin();
    }

    private void gameOver(boolean win){

        String message = win ? "You win!!" : "Game Over..";

        for (int r = 0; r < gridSize; r++){
            for (int c = 0; c < gridSize; c++){

                if (board[r][c].isMine) {
                    buttons[r][c].setText("\uD83D\uDCA3");
                }
            }
        }
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setTitle(message);

        builder.setMessage("What do you want to do?");

        builder.setCancelable(false);

        builder.setPositiveButton("Continue",
                (dialog, which) -> {

                    InstigateGames.isMinigameActive = false;

                    finish();
                });

        builder.setNegativeButton("Replay",
                (dialog, which) -> {

                    recreate();
                });

        builder.show();
    }

    private void checkWin() {

        for (int r = 0; r < gridSize; r++){
            for (int c = 0; c < gridSize; c++){

                if (!board[r][c].isMine &&
                        !board[r][c].isRevealed){
                    return;
                }
            }
        }

        gameOver(true);
    }
}