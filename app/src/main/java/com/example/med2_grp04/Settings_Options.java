package com.example.med2_grp04;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
public class Settings_Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_options);

        ImageButton backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(v -> finish());
    }
}


