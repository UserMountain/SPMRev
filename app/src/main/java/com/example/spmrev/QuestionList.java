package com.example.spmrev;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<QuizData> questionList;
    private String selectedChapter;
    private SearchView searchView;
    private Button addButton, takeButton;
    private String selectedQuestionKey;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_quiz1);

        selectedChapter = getIntent().getStringExtra("selectedChapter");
        // Retrieve data from Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload").child(selectedChapter);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        recyclerView = findViewById(R.id.recyclerView);
        questionList = new ArrayList<>();
        adapter = new MyAdapter(this, questionList, selectedChapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        retrieveQuestions(selectedChapter);

        TextView chapterTitle = findViewById(R.id.chapterTitle);
        chapterTitle.setText(selectedChapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });


        addButton = findViewById(R.id.addQuestionButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadActivity
                startActivity(new Intent(QuestionList.this, UploadActivity.class).putExtra("selectedChapter", selectedChapter));
            }
        });

        takeButton = findViewById(R.id.takeQuiz);
        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadActivity
                startActivity(new Intent(QuestionList.this, FullQuestionActivity.class).putExtra("selectedChapter", selectedChapter));
            }
        });

        registerForContextMenu(recyclerView);

    }

    public void searchList(String text){
        List<QuizData> searchList = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            QuizData quizData = questionList.get(i);
            if (quizData.getDataQuestion().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(quizData);
            }
        }

        for (int i = 0; i < searchList.size(); i++) {
            // Find the original position of the question in the questionList
            int originalPosition = questionList.indexOf(searchList.get(i));
            // Set the question number based on the original position
            searchList.get(i).setQuestionNumber(originalPosition + 1);
        }

        adapter.searchDataList(searchList);
        adapter.notifyDataSetChanged();
    }

    private void retrieveQuestions(String selectedChapter) {
        // Assume you have a node in the Realtime Database for each chapter
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Quiz1_Upload");

        DatabaseReference chapterReference = databaseReference.child(selectedChapter);

        // Order the questions by their keys
        Query orderedQuery = chapterReference.orderByKey();
        // Add a ValueEventListener to fetch questions for the selected chapter
        orderedQuery.addValueEventListener(new ValueEventListener() {
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

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int position = adapter.getSelectedPosition();

        // Check if the position is valid
        if (position != RecyclerView.NO_POSITION) {
            // Retrieve the selected question
            QuizData selectedQuestion = questionList.get(position);

            selectedQuestionKey = selectedQuestion.getQid();

            // Add menu items programmatically
            MenuItem editItem = menu.add(Menu.NONE, R.id.menu_edit, Menu.NONE, "Edit");
            MenuItem deleteItem = menu.add(Menu.NONE, R.id.menu_delete, Menu.NONE, "Delete");

            // Set on click listeners for programmatically added items
            editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(QuestionList.this, EditQuestionActivity.class);
                    intent.putExtra("qid", selectedQuestion.getQid());
                    intent.putExtra("selectedChapter", selectedChapter);
                    startActivity(intent);
                    return true;
                }
            });

            deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    showDeleteConfirmationDialog();
                    return true;
                }
            });
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

        chapterReference.child(selectedQuestionKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Display a success message or navigate back to the previous activity
                Toast.makeText(QuestionList.this, "Question deleted successfully", Toast.LENGTH_SHORT).show();
                retrieveQuestions(selectedChapter);
                finish(); // Finish the DetailActivity
            }
        });
    }

}
