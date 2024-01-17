package com.example.spmrev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText usernameUser, emailUser, idNumberUser, passwordUser, phoneUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameUser = findViewById(R.id.reg_username);
        emailUser = findViewById(R.id.reg_email);
        idNumberUser = findViewById(R.id.reg_id);
        passwordUser = findViewById(R.id.reg_password);
        phoneUser = findViewById(R.id.reg_phone);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        Button registerButton = findViewById(R.id.buttonRegisterConfirm);
        registerButton.setOnClickListener(v -> registerUser());

        Button backLoginButton = findViewById(R.id.back_login);
        backLoginButton.setOnClickListener(v -> {
            // back to login
            Intent intent = new Intent(Register.this, LoginPage.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String username = usernameUser.getText().toString().trim();
        String email = emailUser.getText().toString().trim();
        String idNumber = idNumberUser.getText().toString().trim();
        String password = passwordUser.getText().toString().trim();
        String phone = phoneUser.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(idNumber) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user in Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {
                            // Save user information to Realtime Database
                            saveUserInfoToDatabase(currentUser.getUid(), username, email, idNumber, password, phone);
                        }
                    } else {
                        // Registration failed
                        if (task.getException() instanceof FirebaseAuthException) {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            Toast.makeText(Register.this, "Registration failed: " + errorCode, Toast.LENGTH_SHORT).show();
                            Log.e("Registration Error", errorCode);
                        } else {
                            Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserInfoToDatabase(String userId, String username, String email, String idNumber, String password, String phone) {
        // Create a user map to store user information
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("email", email);
        userMap.put("idNumber", idNumber);
        userMap.put("password", password);
        userMap.put("phone", phone);

        // Save the user information to Realtime Database
        databaseReference.child(userId).setValue(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User information saved successfully
                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        // You can add additional logic here, such as redirecting to a different activity
                    } else {
                        // Error saving user information
                        Toast.makeText(Register.this, "Error saving user information", Toast.LENGTH_SHORT).show();
                        Log.e("Database Error", task.getException().getMessage());
                    }
                });
    }
}
