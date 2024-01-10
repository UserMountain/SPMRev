package com.example.spmrev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button back_login = findViewById(R.id.back_login);
        back_login.setOnClickListener(v -> {
            // back to login
            Intent intent = new Intent(Register.this, LoginPage.class);
            startActivity(intent);
        });
    }
}