package com.example.med2_grp04;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class InstigateGames {

    private Context context;

    public InstigateGames(Context context) {
        this.context = context;
    }

    public void startPopupTimer(){
        new Handler().postDelayed(() -> {

            showMiniGamePopup();
        }, 10000);
    }

    private void showMiniGamePopup() {

        LayoutInflater inflater =
                LayoutInflater.from(context);

        View popupView =
                inflater.inflate(R.layout.popup_minigame,
                        null);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);

        builder.setView(popupView);

        AlertDialog dialog = builder.create();

        Button playButton =
                popupView.findViewById(R.id.playButton);

        playButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(context,
                            TicTacToe.class);

            context.startActivity(intent);

            dialog.dismiss();
        });

        dialog.show();

        new Handler().postDelayed(() -> {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }, 5000);
    }


}
