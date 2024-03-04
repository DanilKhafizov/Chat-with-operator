package com.galeev.operator_chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.galeev.operator_chat.databinding.ActivityMain2Binding;
import com.galeev.operator_chat.models.User;
import com.galeev.operator_chat.utilities.Constants;
import com.galeev.operator_chat.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        String role = preferenceManager.getString(Constants.KEY_ROLE);
        if(role.equals("Администратор")){
            binding.btnShowQuestions.setVisibility(View.VISIBLE);
            binding.btnShowQuestions.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, QuestionsActivity.class)));
        }
            binding.btnBot.setOnClickListener(v -> getUsersFromDatabase());
            binding.btnQuestion.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, UserQuestionActivity.class)));
            binding.btnExit.setOnClickListener(v -> signOut());
    }


    private void getUsersFromDatabase(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            String userEmail = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            String userRole = queryDocumentSnapshot.getString(Constants.KEY_ROLE);
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.role = queryDocumentSnapshot.getString(Constants.KEY_ROLE);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            if ("BOT".equals(userRole) && "bot@mail.ru".equals(userEmail)) {
                                users.add(user);
                                openChatWithBot(user);
                            }
                        }
                    }
                });
    }

    private void openChatWithBot(User selectedUser) {
       if ("BOT".equals(selectedUser.role)) {
            // Передача информации об операторе в активити чата
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, selectedUser);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut(){
        showToast("Выход из аккаунта...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Не удалось выйти"));
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }



}