package com.developer_rahul.agrideal;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developer_rahul.agrideal.ExtraClasses.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etName, etPhone, etEmail;
    private Button btnRegister;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Bind views
        etUsername = findViewById(R.id.et_enterusername);
        etPassword = findViewById(R.id.et_enterpswd);
        etName = findViewById(R.id.et_entername);
        etPhone = findViewById(R.id.et_enter_phone);
        etEmail = findViewById(R.id.et_enteremail);
        btnRegister = findViewById(R.id.btn_reg);

        btnRegister.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        // Get input values
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hash the password for storage
        String hashedPassword = hashPassword(password);

        // Save user data to Firebase Realtime Database
        User newUser = new User(username, hashedPassword, name, phone, email);

        // Store user data under 'users' node in Firebase
        mDatabase.child("Farmer").child(username).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        finish(); // Close registration activity or redirect to login
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Function to hash the password
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.encodeToString(hash, Base64.DEFAULT); // Store in Base64 encoded format
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
