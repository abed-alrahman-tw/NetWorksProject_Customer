package com.example.finalproject_customer.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_customer.databinding.ActivityFilterBinding;


public class FilterActivity extends AppCompatActivity {

    ActivityFilterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}