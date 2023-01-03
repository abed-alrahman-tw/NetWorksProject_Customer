package com.example.finalproject_customer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_customer.databinding.ActivityChoiceCardBinding;
import com.example.finalproject_customer.interfaces.Constants;


public class ActivityChoiceCard extends AppCompatActivity {


    ActivityChoiceCardBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ActivityResultLauncher launcher;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChoiceCardBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


        intent = new Intent(ActivityChoiceCard.this, ActivityCardInfo.class);
        preferences = getSharedPreferences(Constants.SHARED_KEY, 0);
        editor = preferences.edit();

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);
            }
        });
        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), AvailableJobs.class);
                startActivity(intent);
            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {


                        if (result.getResultCode() == RESULT_OK) {

                            Intent in = result.getData();
                            Uri picture = in.getData();
                            intent.putExtra("pic", picture.toString());

                            binding.imageView.setImageURI(picture);

                        } else if (result.getResultCode() == 0) {
                            Toast.makeText(ActivityChoiceCard.this,
                                    "UNDONE", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!binding.etMoreDetails.getText().toString().isEmpty()) {

                    String details = binding.etMoreDetails.getText().toString();
                    editor.putString(Constants.DETAILS, details);
                    editor.apply();
                    startActivity(intent);

                } else Toast.makeText(ActivityChoiceCard.this, "Please enter details",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}