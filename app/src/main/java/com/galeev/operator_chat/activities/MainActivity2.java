package com.galeev.operator_chat.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.galeev.operator_chat.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnOperator.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, SignInActivity.class)));

        binding.btnUser.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, UserQuestionActivity.class)));

        binding.btnExit.setOnClickListener(v -> finish());
    }
}