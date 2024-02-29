package com.galeev.operator_chat.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.galeev.operator_chat.R;
import com.galeev.operator_chat.databinding.ActivityUserQuestionBinding;
import com.galeev.operator_chat.utilities.Constants;
import com.galeev.operator_chat.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UserQuestionActivity extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private EditText editTextFirstName, editTextLastName, editTextQuestionTitle, editTextQuestion;
    private Spinner spinnerRole;
    private ActivityUserQuestionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRole.setAdapter(adapter);
        binding.btnSubmitQuestion.setOnClickListener(v -> saveQuestionToDatabase());
    }

    private void saveQuestionToDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        String firstName = binding.editTextFirstName.getText().toString();
        String lastName = binding.editTextLastName.getText().toString();
        String role = binding.spinnerRole.getSelectedItem().toString();
        String questionTitle = binding.editTextQuestionTitle.getText().toString();
        String question = binding.editTextQuestion.getText().toString();
        checkCorrectInput(firstName, lastName, role, questionTitle, question);
        HashMap<String, Object> questionData = new HashMap<>();
        questionData.put(Constants.KEY_NAME, firstName);
        questionData.put(Constants.KEY_LASTNAME, lastName);
        questionData.put(Constants.KEY_ROLE, role);
        questionData.put(Constants.KEY_QUESTION_TITLE, questionTitle);
        questionData.put(Constants.KEY_QUESTION, question);
        questionData.put(Constants.KEY_TIMESTAMP, currentTime);
        db.collection(Constants.KEY_COLLECTION_QUESTIONS)
                .add(questionData)
                .addOnSuccessListener(documentReference -> {
                    //preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    //preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_NAME, firstName);
                    preferenceManager.putString(Constants.KEY_LASTNAME, lastName);
                    preferenceManager.putString(Constants.KEY_QUESTION_TITLE, questionTitle);
                    preferenceManager.putString(Constants.KEY_QUESTION, question);
                    preferenceManager.putString(Constants.KEY_TIMESTAMP, currentTime);
                   showToast("Вопрос создан");
                    finish();  })
                .addOnFailureListener(exception -> showToast(exception.getMessage()));
    }

    private void checkCorrectInput(String name, String lastname, String role, String questionTitle, String question){
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(role)
                || TextUtils.isEmpty(questionTitle) || TextUtils.isEmpty(question)) {
            showToast("Пожалуйста, заполните все поля");
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}