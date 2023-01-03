package com.example.finalproject_customer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.finalproject_customer.adapters.viewpageradapter.vpAdapter;
import com.example.finalproject_customer.databinding.ActivityOnBoardingBinding;
import com.example.finalproject_customer.interfaces.Constants;
import com.example.finalproject_customer.interfaces.SendPosition;

public class OnBoarding extends AppCompatActivity {

    ActivityOnBoardingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPager viewPager = binding.viewPager;

        vpAdapter adapter = new vpAdapter(new SendPosition() {
            @Override
            public void clickButton(int position) {
                viewPager.setCurrentItem(position + 1);
                SharedPreferences preferences = getSharedPreferences(Constants.SHARED_KEY, 0);
                SharedPreferences.Editor editor = preferences.edit();

                if (position == 2) {
                    startActivity(new Intent(OnBoarding.this, LogIn.class));
                    editor.putBoolean(Constants.IS_FIRST_TIME, true);
                    editor.commit();
                }
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, false);
        viewPager.setNestedScrollingEnabled(false);
        viewPager.setHorizontalScrollBarEnabled(false);
    }
}