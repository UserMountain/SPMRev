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
    private TextView questionNumberTextView;
    TextView answer;
    RadioGroup radioGroup;
    RadioButton opt1, opt2, opt3, opt4;
    private DatabaseReference databaseReference;
    FloatingActionButton deleteButton, editButton;
    private List<QuizData> questionList;
    private Button nextButton;
    private int currentQuestionIndex;
    private String selectedChapter;
    private String questionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");
        questionKey = getIntent().getStringExtra("qid");
        selectedChapter = getIntent().getStringExtra("selectedChapter");

        loadQuestionsFromFirebase(questionKey);

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

    private void loadQuestionsFromFirebase(String questionKey) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");

        String selectedChapter = getIntent().getStringExtra("selectedChapter");

        DatabaseReference chapterReference = databaseReference.child(selectedChapter);
        chapterReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    QuizData questionData = snapshot.getValue(QuizData.class);
                    questionList.add(questionData);
                }

                if (!questionList.isEmpty()) {
                    retrieveQuestion();
                }
                else{
                    Toast.makeText(DetailActivity.this, "No questions found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void retrieveQuestion() {
        if (!questionList.isEmpty() && currentQuestionIndex < questionList.size()) {
            QuizData quizData = questionList.get(currentQuestionIndex);

            questionNumberTextView = findViewById(R.id.questionNumber);
            detailQuestion = findViewById(R.id.question);
            radioGroup = findViewById(R.id.radioGroup);

            opt1 = findViewById(R.id.ansA);
            opt2 = findViewById(R.id.ansB);
            opt3 = findViewById(R.id.ansC);
            opt4 = findViewById(R.id.ansD);
            answer = findViewById(R.id.answer);

            // Update your UI with the retrieved data
            detailQuestion.setText(quizData.getDataQuestion());
            opt1.setText(quizData.getDataOption1());
            opt2.setText(quizData.getDataOption2());
            opt3.setText(quizData.getDataOption3());
            opt4.setText(quizData.getDataOption4());
            answer.setText(quizData.getDataAnswer());

            preselectCorrectAnswer(quizData.getDataAnswer());

            questionNumberTextView.setText("Question " + (currentQuestionIndex + 1) + "/" + questionList.size());
        } else {
            // Handle the case where there are no more questions
            Toast.makeText(DetailActivity.this, "No more questions", Toast.LENGTH_SHORT).show();
        }

        nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

    }

    public void nextQuestion(){
        currentQuestionIndex++;
        retrieveQuestion();
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