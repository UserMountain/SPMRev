package com.example.spmrev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView viewUserName;
    private TextView viewID;
    private TextView viewPassword;
    private TextView viewEmail;
    private TextView viewNumber;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // You can add more initialization code here if needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        viewUserName = view.findViewById(R.id.ViewUsername);
        viewID = view.findViewById(R.id.ViewId);
        viewPassword = view.findViewById(R.id.ViewPassword);
        viewEmail = view.findViewById(R.id.ViewEmail);
        viewNumber = view.findViewById(R.id.ViewNumber);
        Button buttonLogout = view.findViewById(R.id.button_Logout);

        // Set OnClickListener for the logout button
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginPage when the logout button is clicked
                Intent intent = new Intent(getActivity(), LoginPage.class);
                startActivity(intent);
                getActivity().finish(); // Finish the current activity
            }
        });

        // Fetch and display user data from the Realtime Database
        fetchUserData();

        return view;
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the user's node in the Realtime Database
            DatabaseReference userRef = mDatabase.child(userId);

            // Read data from the user's node
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

                        // Update UI with user data
                        viewUserName.setText(userName);
                        viewID.setText(userID);
                        viewPassword.setText(userPassword);
                        viewEmail.setText(userEmail);
                        viewNumber.setText(userNumber);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
    }
}
