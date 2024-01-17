package com.example.spmrev;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadActivity extends AppCompatActivity {

    private EditText edQuestion, edOp1, edOp2, edOp3, edOp4, edAnswer;
    private Button saveButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_layout);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");

        QuizData quizData = new QuizData();

        // Initialize UI elements
        edQuestion = findViewById(R.id.question);
        edOp1 = findViewById(R.id.ans_A);
        edOp2 = findViewById(R.id.ans_B);
        edOp3 = findViewById(R.id.ans_C);
        edOp4 = findViewById(R.id.ans_D);
        edAnswer = findViewById(R.id.correctAns);

        saveButton = findViewById(R.id.saveButton);

        // Set onClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = edQuestion.getText().toString().trim();
                String option1 = edOp1.getText().toString().trim();
                String option2 = edOp2.getText().toString().trim();
                String option3 = edOp3.getText().toString().trim();
                String option4 = edOp4.getText().toString().trim();
                String answer = edAnswer.getText().toString().trim();


                String uid = databaseReference.push().getKey();

                quizData.setQid(uid);
                quizData.setDataQuestion(question);
                quizData.setDataOption1(option1);
                quizData.setDataOption2(option2);
                quizData.setDataOption3(option3);
                quizData.setDataOption4(option4);
                quizData.setDataAnswer(answer);

                databaseReference.child(uid).setValue(quizData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UploadActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

}
