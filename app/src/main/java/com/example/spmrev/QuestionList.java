package com.example.spmrev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<QuizData> questionList;
    private String selectedChapter;

    private Button addButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_quiz1);

        String selectedChapter = getIntent().getStringExtra("selectedChapter");
        // Retrieve data from Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload").child(selectedChapter);

        recyclerView = findViewById(R.id.recyclerView);
        questionList = new ArrayList<>();
        adapter = new MyAdapter(this, questionList, selectedChapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        retrieveQuestions(selectedChapter);

        addButton = findViewById(R.id.addQuestionButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadActivity
                startActivity(new Intent(QuestionList.this, UploadActivity.class).putExtra("selectedChapter", selectedChapter));
            }
        });

    }

    private void retrieveQuestions(String selectedChapter) {
        // Assume you have a node in the Realtime Database for each chapter
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");

        DatabaseReference chapterReference = databaseReference.child(selectedChapter);

        // Add a ValueEventListener to fetch questions for the selected chapter
        chapterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    QuizData questionData = snapshot.getValue(QuizData.class);
                    questionList.add(questionData);
                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(QuestionList.this, "Failed to retrieve questions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
