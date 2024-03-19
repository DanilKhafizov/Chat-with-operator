package com.galeev.operator_chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.galeev.operator_chat.adapters.QuestionAdapter;
import com.galeev.operator_chat.databinding.ActivityQuestionsBinding;
import com.galeev.operator_chat.models.Question;
import com.galeev.operator_chat.utilities.Constants;
import com.galeev.operator_chat.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private ActivityQuestionsBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        List<Question> mQuestionList = new ArrayList<>();
        QuestionAdapter mAdapter = new QuestionAdapter(this, mQuestionList);
        binding.listViewQuestions.setAdapter(mAdapter);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(Constants.KEY_COLLECTION_QUESTIONS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot preferenceManager : queryDocumentSnapshots) {
                        String firstName = preferenceManager.getString(Constants.KEY_NAME);
                        String questionTitle = preferenceManager.getString(Constants.KEY_QUESTION_TITLE);
                        String question = preferenceManager.getString(Constants.KEY_QUESTION);
                        String role = preferenceManager.getString(Constants.KEY_ROLE);
                        String timestamp = preferenceManager.getString(Constants.KEY_TIMESTAMP);
                        Question questionObject = new Question(role, timestamp, firstName, questionTitle, question);
                        mQuestionList.add(questionObject);
                    }
                    mAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(QuestionsActivity.this,
                        "Failed to load questions: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}