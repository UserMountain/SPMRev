package com.example.spmrev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spmrev.MainActivity;
import com.example.spmrev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username, password;
    private Button forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.button_forgotPassword);

        Button button_Login = findViewById(R.id.button_Login);
        Button button_register = findViewById(R.id.button_register);

        button_Login.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString();
            String enteredPassword = password.getText().toString();
            signIn(enteredUsername, enteredPassword);
        });

        button_register.setOnClickListener(v -> {
            // Navigate to the RegisterActivity
            startActivity(new Intent(LoginPage.this, Register.class));
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, ForgotPasswordActivity.class));
            }
        });

    }

    private void signIn(String enteredUsername, String enteredPassword) {
        mAuth.signInWithEmailAndPassword(enteredUsername,enteredPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Toast.makeText(LoginPage.this, "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                    openSignInSuccessActivity(enteredUsername);
                    // Navigate to the next activity or perform other tasks
                } else { // If sign in fails, display a message to the user.
                    Toast.makeText(LoginPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void openSignInSuccessActivity(String enteredUsername) {

        Intent intentRegSuccess = new Intent(LoginPage.this, MainActivity.class);
        intentRegSuccess.putExtra("email", enteredUsername);
        startActivity(intentRegSuccess);

    }

}
