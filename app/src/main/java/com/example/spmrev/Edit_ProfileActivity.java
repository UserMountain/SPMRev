package com.example.spmrev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_ProfileActivity extends AppCompatActivity {

    private EditText editUserName;
    private EditText editID;
    private EditText editPassword;
    private EditText editEmail;
    private EditText editNumber;
    private Button buttonSave;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        editUserName = findViewById(R.id.UpdateUsername);
        editID = findViewById(R.id.UpdateId);
        editPassword = findViewById(R.id.UpdatePassword);
        editEmail = findViewById(R.id.UpdateEmail);
        editNumber = findViewById(R.id.UpdateNumber);
        buttonSave = findViewById(R.id.save_EditAcc);

        // Retrieve existing user data
        fetchUserData();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the updated data to Firebase Realtime Database
                saveUserData();
            }
        });
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = mDatabase.child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user data
                        String userName = dataSnapshot.child("username").getValue(String.class);
                        String userID = dataSnapshot.child("idNumber").getValue(String.class);
                        String userPassword = dataSnapshot.child("password").getValue(String.class);
                        String userEmail = dataSnapshot.child("email").getValue(String.class);
                        String userNumber = dataSnapshot.child("phone").getValue(String.class);

                        // Update UI with existing user data
                        editUserName.setText(userName);
                        editID.setText(userID);
                        editPassword.setText(userPassword);
                        editEmail.setText(userEmail);
                        editNumber.setText(userNumber);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
    }

    private void saveUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = mDatabase.child(userId);

            // Get updated data from EditText fields
            String updatedUserName = editUserName.getText().toString();
            String updatedID = editID.getText().toString();
            String updatedPassword = editPassword.getText().toString();
            String updatedEmail = editEmail.getText().toString();
            String updatedNumber = editNumber.getText().toString();

            // Update the data in Firebase Realtime Database
            userRef.child("username").setValue(updatedUserName);
            userRef.child("idNumber").setValue(updatedID);
            userRef.child("password").setValue(updatedPassword);
            userRef.child("email").setValue(updatedEmail);
            userRef.child("phone").setValue(updatedNumber);

            // Display a success message or navigate back to the profile fragment
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Finish the EditProfileActivity
        }
    }
}
