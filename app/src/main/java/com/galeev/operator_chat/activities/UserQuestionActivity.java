package com.galeev.operator_chat.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.galeev.operator_chat.databinding.ActivityUserQuestionBinding;
import com.galeev.operator_chat.utilities.Constants;
import com.galeev.operator_chat.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UserQuestionActivity extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private ActivityUserQuestionBinding binding;
    private Boolean ischeckCorrectInput = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        binding.btnSubmitQuestion.setOnClickListener(v -> saveQuestionToDatabase());
    }

    private void saveQuestionToDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        String name = preferenceManager.getString(Constants.KEY_NAME);
        String role = preferenceManager.getString(Constants.KEY_ROLE);
        showToast(userId);
        String questionTitle = binding.inputQuestionTitle.getText().toString();
        String question = binding.inputQuestion.getText().toString();
        checkCorrectInput(questionTitle, question);
        if (ischeckCorrectInput) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());


            HashMap<String, Object> questions = new HashMap<>();
            questions.put(Constants.KEY_QUESTION_TITLE, questionTitle);
            questions.put(Constants.KEY_QUESTION, question);
            questions.put(Constants.KEY_TIMESTAMP, currentTime);
            questions.put(Constants.KEY_NAME, name);
            questions.put(Constants.KEY_ROLE, role);

            db.collection(Constants.KEY_COLLECTION_QUESTIONS)
                    .add(questions)
                    .addOnSuccessListener(aVoid -> {
                        showToast("Вопрос создан");
                        finish();
                    })
                    .addOnFailureListener(exception -> showToast(exception.getMessage()));
        } else {
            showToast("Пожалуйста, заполните все поля");
        }
    }

    private void checkCorrectInput(String questionTitle, String question){
        if (TextUtils.isEmpty(questionTitle) || TextUtils.isEmpty(question)) {
            ischeckCorrectInput = false;
        }
        else{
            ischeckCorrectInput = true;
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}