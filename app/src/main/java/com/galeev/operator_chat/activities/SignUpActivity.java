package com.galeev.operator_chat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.galeev.operator_chat.R;
import com.galeev.operator_chat.databinding.ActivitySignUpBinding;
import com.galeev.operator_chat.utilities.Constants;
import com.galeev.operator_chat.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, R.layout.spinner_item_layout);
        adapter.setDropDownViewResource(R.layout.dropdown_item_layout);
        binding.inputRole.setAdapter(adapter);
        setListeners();
    }
    private void setListeners(){
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            if(isValidSignUpDetails()){
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void signUp(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        final String userEmail = binding.inputEmail.getText().toString();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            HashMap<String, Object> user = new HashMap<>();
                            user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
                            user.put(Constants.KEY_EMAIL, userEmail);
                            user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
                            if(encodedImage == null){
                                Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
                                String defaultEncodedImage = encodeImage(defaultBitmap);
                                preferenceManager.putString(Constants.KEY_IMAGE, defaultEncodedImage);
                                user.put(Constants.KEY_IMAGE, defaultEncodedImage);
                            }
                            else{
                                user.put(Constants.KEY_IMAGE, encodedImage);
                                preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                            }
                            user.put(Constants.KEY_ROLE, binding.inputRole.getSelectedItem().toString());
                            if (userEmail.equals("shamilgaleev@gmail.com")) {
                                user.put(Constants.KEY_ROLE, "Администратор");
                                preferenceManager.putString(Constants.KEY_ROLE, "Администратор");
                            }
                            if (userEmail.equals("bot@mail.ru")) {
                                user.put(Constants.KEY_ROLE, "BOT");
                                preferenceManager.putString(Constants.KEY_ROLE, "BOT");
                            }
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        loading(false);
                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                        preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                                        preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                                        preferenceManager.putString(Constants.KEY_ROLE, binding.inputRole.getSelectedItem().toString());
                                        openCorrectActivity();
                                    })
                                    .addOnFailureListener(exception -> {
                                        loading(false);
                                        showToast(exception.getMessage());
                                    });
                        } else {
                            loading(false);
                            showToast("Пользователь с такой почтой уже зарегистрирован.");
                        }
                    } else {
                        loading(false);
                        showToast("Ошибка: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }
    private void openCorrectActivity(){
        String role1 = preferenceManager.getString(Constants.KEY_ROLE);
        Intent intent;
        if(role1.equals("Клиент") || role1.equals("Работник")){
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        else{
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            encodedImage = encodeImage(bitmap);
                            binding.imageDefault.setVisibility(View.INVISIBLE);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private Boolean isValidSignUpDetails(){
        if(binding.inputRole.getSelectedItem().toString().trim().isEmpty()){
            showToast("Введите роль");
            return false;
        } else if (binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Введите имя");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Введите адрес электронной почты");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Введите корректный адрес электронной почты");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Введите пароль");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Подтвердите пароль");
            return false;
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Пароли не совпадают");
            return false;
        } else {
        return true;
        }
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}