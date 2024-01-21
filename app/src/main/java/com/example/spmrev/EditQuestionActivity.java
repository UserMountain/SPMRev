package com.example.spmrev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditQuestionActivity extends AppCompatActivity {

    EditText editQuestion, editOpt1, editOpt2, editOpt3, editOpt4, editAnswer;
    DatabaseReference databaseReference;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        editQuestion = findViewById(R.id.editQuestion);
        editOpt1 = findViewById(R.id.editAnsA);
        editOpt2 = findViewById(R.id.editAnsB);
        editOpt3 = findViewById(R.id.editAnsC);
        editOpt4 = findViewById(R.id.editAnsD);
        editAnswer = findViewById(R.id.correctAns);

        saveButton = findViewById(R.id.save_EditQuestion);
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");

        String questionKey = getIntent().getStringExtra("qid");

        // Fetch existing data and populate EditText fields
        fetchQuestionData(questionKey);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save edited data to Firebase Realtime Database
                saveEditedQuestion(questionKey);
            }
        });
    }

    private void fetchQuestionData(String questionKey) {

        String selectedChapter = getIntent().getStringExtra("selectedChapter");
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

                    // Populate EditText fields with existing data
                    editQuestion.setText(theQuestion);
                    editOpt1.setText(theOption1);
                    editOpt2.setText(theOption2);
                    editOpt3.setText(theOption3);
                    editOpt4.setText(theOption4);
                    editAnswer.setText(theAnswer);
                } else {
                    // Handle the case where the data doesn't exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void saveEditedQuestion(String questionKey) {
        // Get edited data from EditText fields
        String editedQuestion = editQuestion.getText().toString();
        String editedOpt1 = editOpt1.getText().toString();
        String editedOpt2 = editOpt2.getText().toString();
        String editedOpt3 = editOpt3.getText().toString();
        String editedOpt4 = editOpt4.getText().toString();
        String editedAnswer = editAnswer.getText().toString();

        // Save edited data to Firebase Realtime Database
        String selectedChapter = getIntent().getStringExtra("selectedChapter");
        DatabaseReference chapterReference = databaseReference.child(selectedChapter);
        DatabaseReference questionRef = chapterReference.child(questionKey);

        questionRef.child("dataQuestion").setValue(editedQuestion);
        questionRef.child("dataOption1").setValue(editedOpt1);
        questionRef.child("dataOption2").setValue(editedOpt2);
        questionRef.child("dataOption3").setValue(editedOpt3);
        questionRef.child("dataOption4").setValue(editedOpt4);
        questionRef.child("dataAnswer").setValue(editedAnswer);

        // Display a success message or navigate back to DetailActivity
        Toast.makeText(this, "Question updated successfully", Toast.LENGTH_SHORT).show();
        finish(); // Finish the EditQuestionActivity
    }
}
