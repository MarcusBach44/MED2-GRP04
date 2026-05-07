package com.example.med2_grp04;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class TicTacToe extends AppCompatActivity {

    GridLayout gridLayout;

    private final int gridSize = 3;

    private Cell[][] board;
    private Button[][] buttons;

    private boolean playerTurn = true;

    private boolean gameEnded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!brainbreak.isGameEnabled("TicTacToe")){
            finish();
            return;
        }
        setContentView(R.layout.activity_tic_tac_toe);

        gridLayout = findViewById(R.id.gridLayout);

        setupGame();
    }

    private void setupGame() {

        board = new Cell[gridSize][gridSize];
        buttons = new Button[gridSize][gridSize];

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {

                board[r][c] = new Cell();
            }
        }

        createBoardUI();
    }

    private void createBoardUI() {

        gridLayout.removeAllViews();

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {

                int finalR = r;
                int finalC = c;

                Button button = new Button(this);

                button.setTextSize(32);

                GridLayout.LayoutParams params =
                        new GridLayout.LayoutParams(
                                GridLayout.spec(r, 1f),
                                GridLayout.spec(c, 1f)
                        );

                params.width = 0;
                params.height = 0;
                params.setMargins(5,5,5,5);

                button.setLayoutParams(params);

                button.setOnClickListener(v -> makeMove(finalR, finalC));

                buttons[r][c] = button;
                gridLayout.addView(button);
            }
        }
    }

    private void makeMove(int r, int c) {

        if (gameEnded) {
            return;
        }

        if (!board[r][c].value.equals("")) {
            return;
        }

        if (playerTurn) {

            board[r][c].value = "X";
            buttons[r][c].setText("X");

            playerTurn = false;

            checkWin();
           if (!gameEnded){
               checkDraw();
           }
            if (!gameEnded){
                botMove();
            }
        }
    }

    private void botMove() {

        if (gameEnded) {
            return;
        }

        ArrayList<int[]> emptyCells = new ArrayList<>();

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {

                if (board[r][c].value.equals("")) {

                    emptyCells.add(new int[]{r, c});
                }
            }
        }

        if (emptyCells.size() == 0) {
            return;
        }

        Random random = new Random();

        int[] move =
                emptyCells.get(random.nextInt(emptyCells.size()));

        int r = move[0];
        int c = move[1];

        board[r][c].value = "O";
        buttons[r][c].setText("O");

        playerTurn = true;

        checkWin();
        if (!gameEnded){
            checkDraw();
        }
    }



    private void checkWin() {

        // Rows
        for (int r = 0; r < gridSize; r++) {

            if (!board[r][0].value.equals("") &&
                    board[r][0].value.equals(board[r][1].value) &&
                    board[r][1].value.equals(board[r][2].value)) {

                gameOver(board[r][0].value);
                return;
            }
        }

        // Collums
        for (int c = 0; c < gridSize; c++) {

            if (!board[0][c].value.equals("") &&
                    board[0][c].value.equals(board[1][c].value) &&
                    board[1][c].value.equals(board[2][c].value)) {

                gameOver(board[0][c].value);
                return;
            }
        }

        // Diagonal (Top Left to Right)
        if (!board[0][0].value.equals("") &&
                board[0][0].value.equals(board[1][1].value) &&
                board[1][1].value.equals(board[2][2].value)) {

            gameOver(board[0][0].value);
            return;
        }

        // Diagonal (Top Right to Left)
        if (!board[0][2].value.equals("") &&
                board[0][2].value.equals(board[1][1].value) &&
                board[1][1].value.equals(board[2][0].value)) {

            gameOver(board[0][2].value);
        }
    }

    private void gameOver(String winner) {

        gameEnded = true;

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Game Over");

        builder.setMessage(winner + " wins!");

        builder.setCancelable(false);

        builder.setPositiveButton("Continue",
                (dialog, which) -> {

                    Intent intent =
                            new Intent(TicTacToe.this,
                                    MainActivity.class);

                    startActivity(intent);

                    finish();
                });


        builder.setNegativeButton("Replay",
                (dialog, which) -> {

                    recreate();
                });

        builder.show();
    }

    private void checkDraw() {

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {

                if (board[r][c].value.equals("")) {

                    return;
                }
            }
        }

        drawGame();
    }

    private void drawGame() {

        gameEnded = true;

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Draw");

        builder.setMessage("Nobody wins!");

        builder.setCancelable(false);

        builder.setPositiveButton("Continue",
                (dialog, which) -> {

                    Intent intent =
                            new Intent(TicTacToe.this,
                                    MainActivity.class);

                    startActivity(intent);

                    finish();
                });

        builder.setNegativeButton("Replay",
                (dialog, which) -> {

                    recreate();
                });

        builder.show();
    }
}

