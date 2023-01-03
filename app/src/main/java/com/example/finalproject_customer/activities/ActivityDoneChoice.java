package com.example.finalproject_customer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.finalproject_customer.databinding.ActivityDoneOrderBinding;

public class ActivityDoneChoice extends AppCompatActivity {

    String workId, token, details, detailsAddress, phone, lat, Long;
    SharedPreferences preferences;
    ActivityDoneOrderBinding binding;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoneOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences("shared", 0);
        requestQueue = Volley.newRequestQueue(this);

        getDataFromShred();
        binding.btnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ActivityDoneChoice.this, AvailableJobs.class));
            }
        });
    }


    void getDataFromShred() {

        details = preferences.getString("details", "there is no details");

        Toast.makeText(this, "" + workId, Toast.LENGTH_SHORT).show();
        token = preferences.getString("token", "");
        phone = preferences.getString("phone", "");
        detailsAddress = preferences.getString("moreDetails", "");
    }
}