package com.example.spmrev;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    TextView detailQuestion;
    TextView answer;
    RadioGroup radioGroup;
    RadioButton opt1, opt2, opt3, opt4;
    private DatabaseReference databaseReference;
    FloatingActionButton deleteButton, editButton;

    private String selectedChapter;
    private String questionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");
        questionKey = getIntent().getStringExtra("qid");
        selectedChapter = getIntent().getStringExtra("selectedChapter");

        detailQuestion = findViewById(R.id.question);
        opt1 = findViewById(R.id.ansA);
        opt2 = findViewById(R.id.ansB);
        opt3 = findViewById(R.id.ansC);
        opt4 = findViewById(R.id.ansD);
        answer = findViewById(R.id.answer);

        DatabaseReference chapterReference = databaseReference.child(selectedChapter);
        DatabaseReference questionReference = chapterReference.child(questionKey);
        questionReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

                    // Update UI with the retrieved data
                    detailQuestion.setText(theQuestion);
                    opt1.setText(theOption1);
                    opt2.setText(theOption2);
                    opt3.setText(theOption3);
                    opt4.setText(theOption4);
                    answer.setText(theAnswer);

                    preselectCorrectAnswer(theAnswer);

                } else {
                    // Handle the case where the data doesn't exist
                    Toast.makeText(DetailActivity.this, "Question data not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Toast.makeText(DetailActivity.this, "Failed to retrieve question data", Toast.LENGTH_SHORT).show();
            }
        });

        editButton = findViewById(R.id.fabEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open EditQuestionActivity for editing the question
                Intent intent = new Intent(DetailActivity.this, EditQuestionActivity.class);
                intent.putExtra("qid", getIntent().getStringExtra("qid"));
                intent.putExtra("selectedChapter", selectedChapter);
                startActivity(intent);
            }
        });

        deleteButton = findViewById(R.id.fabDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prompt user for confirmation before deleting
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this question?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User confirmed, delete the question
                        deleteQuestion();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, do nothing
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void deleteQuestion() {

        String selectedChapter = getIntent().getStringExtra("selectedChapter");

        // Remove the question from Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");
        DatabaseReference chapterReference = databaseReference.child(selectedChapter);
        String questionKey = getIntent().getStringExtra("qid");
        chapterReference.child(questionKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Display a success message or navigate back to the previous activity
                Toast.makeText(DetailActivity.this, "Question deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Finish the DetailActivity
            }
        });
    }

    private void preselectCorrectAnswer(String correctAnswer) {
        // Preselect the correct answer RadioButton
        if (correctAnswer.equals(opt1.getText().toString())) {
            opt1.setChecked(true);
        } else if (correctAnswer.equals(opt2.getText().toString())) {
            opt2.setChecked(true);
        } else if (correctAnswer.equals(opt3.getText().toString())) {
            opt3.setChecked(true);
        } else if (correctAnswer.equals(opt4.getText().toString())) {
            opt4.setChecked(true);
        }
    }

}