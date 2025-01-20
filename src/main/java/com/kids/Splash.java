package com.kids;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Splash extends AppCompatActivity {
    TextView login, getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Initialize views
        login = findViewById(R.id.button_login);
        getStarted = findViewById(R.id.button_get_started);

        // Handle "Login" button click
        login.setOnClickListener(v -> {
            Intent intent = new Intent(Splash.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the splash activity
        });

        // Handle "Get Started" button click
        getStarted.setOnClickListener(v -> {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the splash activity
        });
    }
}
