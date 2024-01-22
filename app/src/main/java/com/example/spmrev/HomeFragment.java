package com.example.spmrev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the TextView you want to make clickable
        CardView cardViewChapter1 = view.findViewById(R.id.cardViewChp1);
        CardView cardViewChapter2 = view.findViewById(R.id.cardViewChp2);
        CardView cardViewChapter3 = view.findViewById(R.id.cardViewChp3);
        CardView cardViewChapter4 = view.findViewById(R.id.cardViewChp4);
        CardView cardViewChapter5 = view.findViewById(R.id.cardViewChp5);
        CardView cardViewChapter6 = view.findViewById(R.id.cardViewChp6);

        // Set a click listener to the TextView
        cardViewChapter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuestionList.class);

                intent.putExtra("selectedChapter", "Chapter 1");

                startActivity(intent);
            }
        });

        cardViewChapter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadActivity with chapter information
                Intent intent = new Intent(getActivity(), QuestionList.class);
                intent.putExtra("selectedChapter", "Chapter 2"); // Replace with your chapter information
                startActivity(intent);
            }
        });

        cardViewChapter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadActivity with chapter information
                Intent intent = new Intent(getActivity(), QuestionList.class);
                intent.putExtra("selectedChapter", "Chapter 3"); // Replace with your chapter information
                startActivity(intent);
            }
        });

        cardViewChapter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadActivity with chapter information
                Intent intent = new Intent(getActivity(), QuestionList.class);
                intent.putExtra("selectedChapter", "Chapter 4"); // Replace with your chapter information
                startActivity(intent);
            }
        });

        cardViewChapter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadActivity with chapter information
                Intent intent = new Intent(getActivity(), QuestionList.class);
                intent.putExtra("selectedChapter", "Chapter 5"); // Replace with your chapter information
                startActivity(intent);
            }
        });

        cardViewChapter6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadActivity with chapter information
                Intent intent = new Intent(getActivity(), QuestionList.class);
                intent.putExtra("selectedChapter", "Chapter 6"); // Replace with your chapter information
                startActivity(intent);
            }
        });

        return view;
    }


}
