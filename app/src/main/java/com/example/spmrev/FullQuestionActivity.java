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

public class FullQuestionActivity extends AppCompatActivity {

    TextView detailQuestion;
    private TextView questionNumberTextView;
    TextView answer;
    RadioGroup radioGroup;
    RadioButton opt1, opt2, opt3, opt4;
    private DatabaseReference databaseReference;
    FloatingActionButton deleteButton, editButton;
    private List<QuizData> questionList;
    private Button nextTButton, prevButton;
    private int currentQuestionIndex;
    private String selectedChapter;
    private String questionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_question);

        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");
        questionKey = getIntent().getStringExtra("qid");
        selectedChapter = getIntent().getStringExtra("selectedChapter");
        TextView chapterTitle = findViewById(R.id.chapterTitleAll);
        chapterTitle.setText(selectedChapter);

        loadQuestionsFromFirebase(questionKey);

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
                    Toast.makeText(FullQuestionActivity.this, "No questions found", Toast.LENGTH_SHORT).show();
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

            questionNumberTextView = findViewById(R.id.seeQuestionNumber);
            detailQuestion = findViewById(R.id.seeQuestion);
            radioGroup = findViewById(R.id.radioGroup);

            opt1 = findViewById(R.id.seeAnsA);
            opt2 = findViewById(R.id.seeAnsB);
            opt3 = findViewById(R.id.seeAnsC);
            opt4 = findViewById(R.id.seeansD);
            answer = findViewById(R.id.seeanswer);

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
            Toast.makeText(FullQuestionActivity.this, "No more questions", Toast.LENGTH_SHORT).show();
        }

        nextTButton = findViewById(R.id.button_nextQ);
        nextTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        prevButton = findViewById(R.id.button_previousQ);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });

    }

    public void previousQuestion() {
        if (currentQuestionIndex > 0) {
            // Move to the previous question
            currentQuestionIndex--;
            retrieveQuestion();
        } else {
            // Handle the case where there are no previous questions
            Toast.makeText(FullQuestionActivity.this, "No previous questions", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextQuestion(){
        currentQuestionIndex++;
        retrieveQuestion();
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