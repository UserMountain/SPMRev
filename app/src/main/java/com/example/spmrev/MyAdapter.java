package com.example.spmrev;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<QuizData> questionList;
    private Context context;
    private String selectedChapter;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public MyAdapter(Context context, List<QuizData> questionList, String selectedChapter) {
        this.context = context;
        this.questionList = questionList;
        this.selectedChapter = selectedChapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.number_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizData questionData = questionList.get(position);
        holder.questionTextView.setText("Question " + (position + 1));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use getAdapterPosition() to get the current adapter position
                int adapterPosition = holder.getAdapterPosition();
                // Ensure the position is valid
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Handle item click here
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("qid", questionList.get(adapterPosition).getQid());
                    intent.putExtra("selectedChapter", selectedChapter); // Pass the selected chapter
                    // Pass any necessary data to DetailActivity
                    context.startActivity(intent);
                }
            }
        });


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Set the selected position
                selectedPosition = holder.getAdapterPosition();
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void searchDataList(List<QuizData> searchList){
        questionList = searchList;

        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionText);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

}

