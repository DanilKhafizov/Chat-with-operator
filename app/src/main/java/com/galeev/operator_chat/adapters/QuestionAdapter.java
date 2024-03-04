package com.galeev.operator_chat.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.galeev.operator_chat.R;
import com.galeev.operator_chat.activities.FragmentQuestion;
import com.galeev.operator_chat.models.Question;


import java.util.List;

public class QuestionAdapter extends ArrayAdapter<Question>  {

    private Context mContext;
    private List<Question> mQuestions;

    public QuestionAdapter(Context context, List<Question> questions) {
        super(context, 0, questions);
        mContext = context;
        mQuestions = questions;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.question_item, parent, false);
        }
        Question currentQuestion = mQuestions.get(position);
        TextView textViewRole = listItem.findViewById(R.id.textViewRole);
        String role = "Вопрос от " + currentQuestion.getRole() + "а";
        textViewRole.setText(role);
        TextView textViewTimestamp = listItem.findViewById(R.id.textViewTimestamp);
        textViewTimestamp.setText(currentQuestion.getTimestamp());

        TextView textViewUsername = listItem.findViewById(R.id.textViewUsername);
        String username = currentQuestion.getFirstName();
        textViewUsername.setText(username);

        TextView textViewQuestionTitle = listItem.findViewById(R.id.textViewQuestionTitle);
        textViewQuestionTitle.setText(currentQuestion.getQuestionTitle());

        TextView textViewQuestion = listItem.findViewById(R.id.textViewQuestion);
        textViewQuestion.setText(currentQuestion.getQuestion());

        // Ограничение текста и установка многоточия при необходимости
        textViewQuestion.setMaxLines(2); // Например, максимальное количество строк
        textViewQuestion.setEllipsize(TextUtils.TruncateAt.END); // Обрезка в конце текста
        textViewQuestion.setText(currentQuestion.getQuestion());

        listItem.setOnClickListener(v -> {

            FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
            Question selectedQuestion = mQuestions.get(position);
            FragmentQuestion questionFragment = FragmentQuestion.newInstance(selectedQuestion.getQuestionTitle(), selectedQuestion.getQuestion());

            transaction.replace(R.id.fragment_container, questionFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });
        return listItem;
    }
}