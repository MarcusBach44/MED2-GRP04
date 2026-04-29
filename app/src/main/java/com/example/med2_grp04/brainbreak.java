package com.example.med2_grp04;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class brainbreak extends AppCompatActivity {
    private Button btn;
    private boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brainbreak);

        btn = findViewById(R.id.btnTicTacToe);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPressed == false){
                    btn.setBackgroundColor(getResources().getColor(R.color.black));
                    isPressed = true;
                } else {
                    btn.setBackgroundColor(getResources().getColor(R.color.grey));
                    isPressed = false;
                }
            }
        });
    }
}
