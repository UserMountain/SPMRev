package com.example.spmrev;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spmrev.MainActivity;
import com.example.spmrev.R;

public class LoginPage extends AppCompatActivity {

    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button button_Login = findViewById(R.id.button_Login);
        Button button_register = findViewById(R.id.button_register);

        button_Login.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString().trim();
            String enteredPassword = password.getText().toString().trim();
            signIn(enteredUsername, enteredPassword);
        });
        button_register.setOnClickListener(v -> {
            // Start the Register activity when the Register button is clicked
            Intent intent = new Intent(LoginPage.this, Register.class);
            startActivity(intent);
        });
    }

    private void signIn(String enteredUsername, String enteredPassword) {
        // Check if the entered username and password are valid
        if (enteredUsername.equals("mamu") && enteredPassword.equals("ain")) {
            // Credentials are valid, navigate to the MainActivity which holds the HomeFragment
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish the login activity to prevent going back on pressing back button
        } else {
            // Show an error message if credentials are invalid
            Toast.makeText(LoginPage.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

}
