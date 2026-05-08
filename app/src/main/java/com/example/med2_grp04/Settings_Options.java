package com.example.med2_grp04;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class Settings_Options extends AppCompatActivity {

    private Button btnSleepTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_options);


        ImageButton backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(v -> finish());


        btnSleepTime = findViewById(R.id.btnSleepTime);


        updateButtonText();


        btnSleepTime.setOnClickListener(v -> openTimePicker());
    }


    public static boolean isNightMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("SleepPrefs", Context.MODE_PRIVATE);
        int sleepHour = prefs.getInt("sleep_hour", 22);
        int sleepMinute = prefs.getInt("sleep_minute", 0);

        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        int currentTime = currentHour * 60 + currentMinute;
        int sleepTime = sleepHour * 60 + sleepMinute;
        int wakeTime = 7 * 60;


        return (currentTime >= sleepTime || currentTime < wakeTime);
    }

    private void openTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    saveSleepTime(hourOfDay, minuteOfHour);
                    updateButtonText();

                    if (isNightMode(this)) {
                        Toast.makeText(this, "Night mode er nu aktiv", Toast.LENGTH_SHORT).show();
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private void saveSleepTime(int hour, int minute) {
        SharedPreferences prefs = getSharedPreferences("SleepPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("sleep_hour", hour);
        editor.putInt("sleep_minute", minute);
        editor.apply();
    }

    private void updateButtonText() {
        SharedPreferences prefs = getSharedPreferences("SleepPrefs", MODE_PRIVATE);
        int hour = prefs.getInt("sleep_hour", 22);
        int minute = prefs.getInt("sleep_minute", 0);

        String timeFormatted = String.format("%02d:%02d", hour, minute);
        btnSleepTime.setText(timeFormatted);
    }
}