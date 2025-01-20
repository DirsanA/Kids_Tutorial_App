package com.kids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    RadioGroup radioGroupLoginAs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        radioGroupLoginAs = findViewById(R.id.radioGroupLoginAs);

        // Find the views by their IDs
        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.login);

        // Set up click listeners for the buttons
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.login) {
            loginUser();
        }
    }
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Get the selected user type from the RadioGroup
        int selectedId = radioGroupLoginAs.getCheckedRadioButtonId();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if no RadioButton is selected
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a login type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check credentials (this example uses a hardcoded password "123")
        if ("123".equals(password)) {
            // Navigate based on the selected RadioButton
            Intent intent;
            if (selectedId == R.id.radioEvaluator) {
                intent = new Intent(this, MainActivityEvaluater.class);
            } else if (selectedId == R.id.radioTeacher) {
                intent = new Intent(this, MainActivityTeacher.class);
            } else {
                Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
            startActivity(intent); // Start the respective activity
            finish(); // Close LoginActivity
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToForgotPassword() {
        Toast.makeText(this, "Forgot Password functionality is disabled.", Toast.LENGTH_SHORT).show();
    }

    private void navigateToRegister() {
        Toast.makeText(this, "Register functionality is disabled.", Toast.LENGTH_SHORT).show();
    }
}