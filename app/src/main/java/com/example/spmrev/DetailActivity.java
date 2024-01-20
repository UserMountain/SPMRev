package com.example.spmrev;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView detailQuestion;
    private TextView questionNumberTextView;
    TextView answer;
    RadioGroup radioGroup;
    RadioButton opt1, opt2, opt3, opt4;
    private DatabaseReference databaseReference;
    FloatingActionButton deleteButton, editButton;
    private List<QuizData> questionList;
    private MyAdapter adapter;

    int totalQuestions;
    int currentQuestionIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        questionNumberTextView = findViewById(R.id.questionNumber);
        detailQuestion = findViewById(R.id.question);
        radioGroup = findViewById(R.id.radioGroup);

        opt1 = findViewById(R.id.ansA);
        opt2 = findViewById(R.id.ansB);
        opt3 = findViewById(R.id.ansC);
        opt4 = findViewById(R.id.ansD);
        answer = findViewById(R.id.answer);

        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");

        String selectedChapter = getIntent().getStringExtra("selectedChapter");
        String questionKey = getIntent().getStringExtra("qid");

        adapter = new MyAdapter(this, questionList, selectedChapter);

        DatabaseReference chapterReference = databaseReference.child(selectedChapter);
        chapterReference.child(questionKey).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    totalQuestions = (int) dataSnapshot.getChildrenCount(); // Count of all questions
                    String qidValue = dataSnapshot.child("qid").getValue(String.class);

                    try {
                        currentQuestionIndex = Integer.parseInt(qidValue);
                    } catch (NumberFormatException e) {
                        // Handle the case where the value is not a valid integer
                        e.printStackTrace(); // Log the exception for debugging
                        // You may want to set a default value or handle it based on your requirements
                        currentQuestionIndex = 0; // Set a default value or handle it accordingly
                    }

                    // Update your UI with the retrieved data
                    detailQuestion.setText(theQuestion);
                    opt1.setText(theOption1);
                    opt2.setText(theOption2);
                    opt3.setText(theOption3);
                    opt4.setText(theOption4);
                    answer.setText(theAnswer);


                    preselectCorrectAnswer(theAnswer);

                    questionNumberTextView.setText("Question " + (currentQuestionIndex + 1) + "/" + totalQuestions);

                } else {
                    // Handle the case where the data doesn't exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
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