package com.example.spmrev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {

    TextView detailQuestion;
    TextView opt1, opt2, opt3, opt4, answer;

    FloatingActionButton deleteButton, editButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailQuestion = findViewById(R.id.question);
        opt1 = findViewById(R.id.ansA);
        opt2 = findViewById(R.id.ansB);
        opt3 = findViewById(R.id.ansC);
        opt4 = findViewById(R.id.ansD);
        answer = findViewById(R.id.answer);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");
        String questionKey = getIntent().getStringExtra("qid");

        databaseReference.child(questionKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve data from the snapshot
                    String theQuestion = dataSnapshot.child("dataQuestion").getValue(String.class);
                    String theOption1 = dataSnapshot.child("dataOption1").getValue(String.class);
                    String theOption2 = dataSnapshot.child("dataOption2").getValue(String.class);
                    String theOption3 = dataSnapshot.child("dataOption3").getValue(String.class);
                    String theOption4 = dataSnapshot.child("dataOption4").getValue(String.class);
                    String theAnswer = dataSnapshot.child("dataAnswer").getValue(String.class);

                    // Update your UI with the retrieved data
                    detailQuestion.setText(theQuestion);
                    opt1.setText(theOption1);
                    opt2.setText(theOption2);
                    opt3.setText(theOption3);
                    opt4.setText(theOption4);
                    answer.setText(theAnswer);
                } else {
                    // Handle the case where the data doesn't exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        editButton = findViewById(R.id.fabDelete);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open EditQuestionActivity for editing the question
                Intent intent = new Intent(DetailActivity.this, EditQuestionActivity.class);
                intent.putExtra("qid", getIntent().getStringExtra("qid"));
                startActivity(intent);
            }
        });

    }
}