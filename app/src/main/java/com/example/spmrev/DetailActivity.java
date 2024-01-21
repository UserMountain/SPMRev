package com.example.spmrev;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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

    SearchView searchView;
    private MyAdapter myAdapter;
    TextView detailQuestion;
    private TextView questionNumberTextView;
    TextView answer;
    RadioGroup radioGroup;
    RadioButton opt1, opt2, opt3, opt4;
    private DatabaseReference databaseReference;
    FloatingActionButton deleteButton, editButton;
    private List<QuizData> questionList;
    private Button nextButton;
    private int currentQuestionIndex = 0;


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
        String questionKey = getIntent().getStringExtra("qid");
        String selectedChapter = getIntent().getStringExtra("selectedChapter");

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

                    // Update your UI with the retrieved data
                    detailQuestion.setText(theQuestion);
                    opt1.setText(theOption1);
                    opt2.setText(theOption2);
                    opt3.setText(theOption3);
                    opt4.setText(theOption4);
                    answer.setText(theAnswer);

                    preselectCorrectAnswer(theAnswer);
                } else {
                    Toast.makeText(DetailActivity.this, "Question data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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

        // Assuming you have declared your nextButton in your class
        nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if there are questions in the list
                if (!questionList.isEmpty()) {
                    // Increment the current question index
                    currentQuestionIndex++;

                    // Check if there are more questions
                    if (currentQuestionIndex < questionList.size()) {
                        // Retrieve data for the next question
                        QuizData nextQuestion = questionList.get(currentQuestionIndex);

                        // Update your UI with the data for the next question
                        detailQuestion.setText(nextQuestion.getDataQuestion());
                        opt1.setText(nextQuestion.getDataOption1());
                        opt2.setText(nextQuestion.getDataOption2());
                        opt3.setText(nextQuestion.getDataOption3());
                        opt4.setText(nextQuestion.getDataOption4());
                        answer.setText(nextQuestion.getDataAnswer());

                        preselectCorrectAnswer(nextQuestion.getDataAnswer());

                        questionNumberTextView.setText("Question " + (currentQuestionIndex + 1) + "/" + questionList.size());
                    } else {
                        // Handle the case where there are no more questions
                        Toast.makeText(DetailActivity.this, "No more questions", Toast.LENGTH_SHORT).show();

                        // Decrement the current question index to revert the increment
                        currentQuestionIndex--;
                    }
                } else {
                    // Handle the case where questionList is empty
                    Toast.makeText(DetailActivity.this, "Question list is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Initialize questionList
        questionList = new ArrayList<>();

        loadQuestionsFromFirebase();

        // Check if a specific question index was passed from the previous activity
        int selectedQuestionIndex = getIntent().getIntExtra("selectedQuestionIndex", -1);

        if (selectedQuestionIndex != -1 && selectedQuestionIndex < questionList.size()) {
            // Update currentQuestionIndex to the selected question index
            currentQuestionIndex = selectedQuestionIndex;

            // Display the selected question
            displayQuestionAtIndex(currentQuestionIndex);

            Log.d("DetailActivity", "Displaying selected question. Index: " + currentQuestionIndex);
        } else {
            // If no specific question index is provided, display the first question
            displayQuestionAtIndex(currentQuestionIndex);

            Log.d("DetailActivity", "Displaying selected question. Index: " + currentQuestionIndex);
        }

    }

    // Display a question at a specific index in the questionList
    private void displayQuestionAtIndex(int index) {
        if (index >= 0 && index < questionList.size()) {
            QuizData selectedQuestion = questionList.get(index);

            // Update your UI with the data for the selected question
            detailQuestion.setText(selectedQuestion.getDataQuestion());
            opt1.setText(selectedQuestion.getDataOption1());
            opt2.setText(selectedQuestion.getDataOption2());
            opt3.setText(selectedQuestion.getDataOption3());
            opt4.setText(selectedQuestion.getDataOption4());
            answer.setText(selectedQuestion.getDataAnswer());

            preselectCorrectAnswer(selectedQuestion.getDataAnswer());

            questionNumberTextView.setText("Question " + (index + 1) + "/" + questionList.size());
        }
    }

    private void loadQuestionsFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");

        String selectedChapter = getIntent().getStringExtra("selectedChapter");
        //String questionKey = getIntent().getStringExtra("qid");
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
                    // Display the first question
                    currentQuestionIndex = 0;
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

    private void retrieveQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            QuizData quizData = questionList.get(currentQuestionIndex);

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