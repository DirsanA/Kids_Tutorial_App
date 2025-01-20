package com.kids;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterTeacher extends Fragment {

    private EditText firstName, lastName, email, password;
    private Button registerButton;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_teachers, container, false);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("teachers");

        // Initialize UI components
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.emailTeacher);
        password = view.findViewById(R.id.pwdTeacher);
        registerButton = view.findViewById(R.id.button);

        // Set click listener for register button
        registerButton.setOnClickListener(v -> registerTeacher());

        return view;
    }

    private void registerTeacher() {
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passText = password.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(fName)) {
            firstName.setError("First Name is required");
            return;
        }
        if (TextUtils.isEmpty(lName)) {
            lastName.setError("Last Name is required");
            return;
        }
        if (TextUtils.isEmpty(emailText)) {
            email.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(passText)) {
            password.setError("Password is required");
            return;
        }
        if (passText.length() < 6) {
            password.setError("Password must be at least 6 characters long");
            return;
        }

        // Create a unique ID for the teacher
        String teacherId = databaseReference.push().getKey();

        // Create a HashMap to store teacher data
        Map<String, Object> teacherData = new HashMap<>();
        teacherData.put("id", teacherId);
        teacherData.put("firstName", fName);
        teacherData.put("lastName", lName);
        teacherData.put("email", emailText);
        teacherData.put("password", passText);

        // Store data in Firebase Database
        assert teacherId != null;
        databaseReference.child(teacherId).setValue(teacherData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Teacher registered successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to register teacher: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearFields() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        password.setText("");
    }
}
