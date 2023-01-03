package com.example.finalproject_customer.activities;

import static com.android.volley.Request.Method.POST;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject_customer.databinding.ActivityLogInBinding;
import com.example.finalproject_customer.interfaces.Constants;

import org.json.JSONObject;

public class LogIn extends AppCompatActivity {

    ActivityLogInBinding binding;

    RequestQueue requestQueue;

    EditText emailEt, passwordEt;

    TextView signUpTv;

    Button loginBtn;

    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getDataFromIntent();

        initializeVars_views();

        retrieveSession();

        loginBtn.setOnClickListener(v -> {
            saveLastSession();
            executeRequest();
        });

        signUpTv.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
        });
    }

    void postData(String email, String password) {

        showAnimation();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (Exception e) {

            Log.e("e", "postData: " + e.getMessage());
        }
        JsonObjectRequest request = new JsonObjectRequest(POST,
                "https://studentucas.awamr.com/api/auth/login/user",
                jsonObject, new Response.Listener<JSONObject>() {

            JSONObject object;
            String token = "Bearer ";

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {
                        object = response.getJSONObject("data");
                        Toast.makeText(LogIn.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        token += object.getString("token");
                        editor.putString(Constants.TOKEN, token);
                        Log.e("onResponse:token ", token);
                        editor.apply();
                        finish();
                        startActivity(intent);

                    } else {
                        hideAnimation();
                        Toast.makeText(LogIn.this, "" + response.get("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception1", e.getMessage());

                }
            }
        }, error -> {
            hideAnimation();
            Toast.makeText(LogIn.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(request);

    }

    void initializeVars_views() {
        requestQueue = Volley.newRequestQueue(this);
        binding.animationView.setVisibility(View.INVISIBLE);
        preferences = getSharedPreferences(Constants.SHARED_KEY, 0);
        editor = preferences.edit();
        emailEt = binding.loginProviderEtEmail;
        passwordEt = binding.loginProviderEtPassword;
        signUpTv = binding.loginProviderTvSignUp;
        loginBtn = binding.loginProviderBtnLogin;
        intent = new Intent(LogIn.this, AvailableJobs.class);

    }

    void showAnimation() {
        binding.loginProviderBtnLogin.setVisibility(View.INVISIBLE);
        binding.loginProviderEtPasswordLayout.setVisibility(View.INVISIBLE);
        binding.loginProviderEtEmailLayout.setVisibility(View.INVISIBLE);
        binding.loginProviderCbRemember.setVisibility(View.INVISIBLE);
        binding.loginProviderTvNewMember.setVisibility(View.INVISIBLE);
        binding.loginProviderTvForgetPassword.setVisibility(View.INVISIBLE);
        binding.loginProviderTvSignUp.setVisibility(View.INVISIBLE);
        binding.loginProviderLine3.setVisibility(View.INVISIBLE);
        binding.animationView.setVisibility(View.VISIBLE);
    }

    void hideAnimation() {
        binding.loginProviderBtnLogin.setVisibility(View.VISIBLE);
        binding.loginProviderEtPasswordLayout.setVisibility(View.VISIBLE);
        binding.loginProviderEtEmailLayout.setVisibility(View.VISIBLE);
        binding.loginProviderCbRemember.setVisibility(View.VISIBLE);
        binding.loginProviderTvNewMember.setVisibility(View.VISIBLE);
        binding.loginProviderTvForgetPassword.setVisibility(View.VISIBLE);
        binding.loginProviderTvSignUp.setVisibility(View.VISIBLE);
        binding.loginProviderLine3.setVisibility(View.VISIBLE);
        binding.animationView.setVisibility(View.INVISIBLE);
    }

    void saveLastSession() {

        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        boolean isChecked = binding.loginProviderCbRemember.isChecked();

        if (isChecked) {
            editor.putString(Constants.EMAIL, email);
            editor.putString(Constants.PASS, password);
            editor.putBoolean(Constants.IS_CHECKED, true);
            editor.apply();
        }
        if (!isChecked) {
            editor.putBoolean(Constants.IS_CHECKED, false);
        }
    }

    void retrieveSession() {

        boolean isChecked = preferences.getBoolean(Constants.IS_CHECKED, false);

        if (isChecked) {
            String email = preferences.getString(Constants.EMAIL, "");
            String password = preferences.getString(Constants.PASS, "");
            postData(email, password);

        }

    }

    void executeRequest() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        intent = new Intent(LogIn.this, AvailableJobs.class);

        postData(email, password);

    }

    void getDataFromIntent() {
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        if (email != null) {
            binding.loginProviderEtEmail.setText(email);
            binding.loginProviderEtPassword.setText(password);
        }
    }
}