package com.galeev.operator_chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.galeev.operator_chat.R;


public class FragmentQuestion extends Fragment  {

    public FragmentQuestion() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), QuestionsActivity.class);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Метод для создания нового экземпляра фрагмента с передачей данных о вопросе
    public static FragmentQuestion newInstance(String questionTitle, String questionDetails) {
        FragmentQuestion fragment = new FragmentQuestion();
        Bundle args = new Bundle();
        args.putString("questionTitle", questionTitle);
        args.putString("questionDetails", questionDetails);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question, container, false);

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);

        TextView textViewDetails = view.findViewById(R.id.textViewDetails);

        Bundle args = getArguments();
        if (args != null) {
            String questionTitle = args.getString("questionTitle");
            String questionDetails = args.getString("questionDetails");

            textViewTitle.setText(questionTitle);
            textViewDetails.setText(questionDetails);
        }

        // Возвращаем вид фрагмента
        return view;
    }



}