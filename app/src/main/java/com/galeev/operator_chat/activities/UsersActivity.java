package com.galeev.operator_chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.galeev.operator_chat.adapters.UsersAdapter;
import com.galeev.operator_chat.databinding.ActivityUsersBinding;
import com.galeev.operator_chat.listeners.UserListener;
import com.galeev.operator_chat.models.User;
import com.galeev.operator_chat.utilities.Constants;
import com.galeev.operator_chat.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }
private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
}
    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    String currentUserRole = preferenceManager.getString(Constants.KEY_ROLE); // Получаем роль текущего пользователя
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            String userId = queryDocumentSnapshot.getId();
                            String userRole = queryDocumentSnapshot.getString(Constants.KEY_ROLE);
                            if (currentUserId.equals(userId)) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            // Проверяем роль текущего пользователя и добавляем пользователя в список
                            if ("Администратор".equals(currentUserRole)) {
                                // Если текущий пользователь администратор, отображаем всех пользователей, кроме текущего
                                users.add(user);
                            } else if ("Администратор".equals(userRole)) {
                                // Если текущий пользователь не администратор, отображаем только аккаунт администратора
                                users.clear(); // Очищаем список от других пользователей
                                users.add(user);
                                break; // Прерываем цикл после добавления аккаунта администратора
                            } else {
                                // Если ни текущий пользователь, ни текущий пользователь из списка не администраторы, отображаем всех пользователей
                                users.add(user);
                            }
                        }
                        if (users.size() > 0) {
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter(usersAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }
    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "Нет доступного пользователя"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}