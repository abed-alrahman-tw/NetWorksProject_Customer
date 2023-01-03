package com.example.finalproject_customer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_customer.R;
import com.example.finalproject_customer.interfaces.Constants;


public class SplashScreen extends AppCompatActivity {
SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        preferences = getSharedPreferences(Constants.SHARED_KEY, 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (preferences.getBoolean(Constants.IS_FIRST_TIME, false))
                    startActivity(new Intent(SplashScreen.this, LogIn.class));
                else
                    startActivity(new Intent(SplashScreen.this, OnBoarding.class));

                finish();
            }
        }, 3000);

    }
}