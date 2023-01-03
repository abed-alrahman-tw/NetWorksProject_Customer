package com.example.finalproject_customer.activities;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject_customer.databinding.ActivitySignUpBinding;
import com.example.finalproject_customer.interfaces.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    //    boolean b;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RequestQueue requestQueue;
    EditText fullNameEt, emailEt, phoneNum, passwordEt;
    Button signupBtn;
    ImageView backBtn;
    TextView signInTv;
    CheckBox privacyCh;
    Spinner spinner;
    String jobId;
    Intent intent;
    ArrayList<String> spinnerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeVars_Views();

        binding.animationView.setVisibility(View.INVISIBLE);
        jobId = getJobWorkId();
        getAllWorks();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (privacyCh.isChecked()) {

                    String fullName = fullNameEt.getText().toString();
                    String password = passwordEt.getText().toString();
                    String email = emailEt.getText().toString();
                    String phone = phoneNum.getText().toString();

                    postData(email, password, phone, fullName, jobId);
                }else
                    Toast.makeText(SignUp.this, "please confirm our Terms & conditions", Toast.LENGTH_SHORT).show();
            }
        });
        binding.registerProviderTvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.registerProviderImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void initializeVars_Views() {

        fullNameEt = binding.registerProviderEtFullName;
        emailEt = binding.registerProviderEtEmail;
        phoneNum = binding.registerProviderEtPhoneNumber;
        passwordEt = binding.registerProviderEtPassword;
        backBtn = binding.registerProviderImgBack;
        spinnerList = new ArrayList<>();
        intent = new Intent(SignUp.this, LogIn.class);
        signupBtn = binding.registerProviderBtnRegister;
        signInTv = binding.registerProviderTvSignIn;
        privacyCh = binding.registerProviderCbAccept;
        requestQueue = Volley.newRequestQueue(this);
        preferences = getSharedPreferences(Constants.SHARED_KEY, 0);
        editor = preferences.edit();
        spinner = binding.registerProviderSpinner;
        spinnerList.add("Choose your work");


    }

    String getJobWorkId() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, androidx.constraintlayout.widget.R.layout.
                        support_simple_spinner_dropdown_item
                        , spinnerList);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                jobId = String.valueOf(id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return jobId;
    }

    void showAnimation() {
        binding.ccp.setVisibility(View.INVISIBLE);
        binding.registerProviderBtnRegister.setVisibility(View.INVISIBLE);
        binding.registerProviderEtFullNameLayout.setVisibility(View.INVISIBLE);
        binding.registerProviderEtEmailLayout.setVisibility(View.INVISIBLE);
        binding.registerProviderEtPasswordLayout.setVisibility(View.INVISIBLE);
        binding.registerProviderTvSignIn.setVisibility(View.INVISIBLE);
        binding.registerProviderSpinner.setVisibility(View.INVISIBLE);
        binding.registerProviderTvHaveAccount.setVisibility(View.INVISIBLE);
        binding.registerProviderEtPhoneNumber.setVisibility(View.INVISIBLE);
        binding.registerProviderLinear2.setVisibility(View.INVISIBLE);
        binding.registerProviderLine3.setVisibility(View.INVISIBLE);
        binding.registerProviderLinearNumber.setVisibility(View.INVISIBLE);
        binding.animationView.setVisibility(View.VISIBLE);
    }

    void hideAnimation() {

        binding.ccp.setVisibility(View.VISIBLE);
        binding.registerProviderBtnRegister.setVisibility(View.VISIBLE);
        binding.registerProviderCbAccept.setVisibility(View.VISIBLE);
        binding.registerProviderEtFullNameLayout.setVisibility(View.VISIBLE);
        binding.registerProviderEtEmailLayout.setVisibility(View.VISIBLE);
        binding.registerProviderEtPasswordLayout.setVisibility(View.VISIBLE);
        binding.registerProviderTvSignIn.setVisibility(View.VISIBLE);
        binding.registerProviderSpinner.setVisibility(View.VISIBLE);
        binding.registerProviderEtPhoneNumber.setVisibility(View.VISIBLE);
        binding.registerProviderTvHaveAccount.setVisibility(View.VISIBLE);
        binding.registerProviderLinear2.setVisibility(View.VISIBLE);
        binding.registerProviderLine3.setVisibility(View.VISIBLE);
        binding.registerProviderLinearNumber.setVisibility(View.VISIBLE);
        binding.animationView.setVisibility(View.INVISIBLE);
    }

    void getAllWorks() {
        showAnimation();
        StringRequest stringRequest = new StringRequest(GET,
                "https://studentucas.awamr.com/api/all/works",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject1 = jsonArray.getJSONObject(i);
                                String workName = jsonObject1.getString("name");
                                int workId = Integer.parseInt(jsonObject1.getString("id"));
                                spinnerList.add(workName);


                            }
                            hideAnimation();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideAnimation();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideAnimation();
            }
        });

        requestQueue.add(stringRequest);
        getJobWorkId();
    }

    void postData(String email, String password, String phone, String fullName, String workId) {

        showAnimation();

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("phone", phone);
            jsonObject.put("name", fullName);
            jsonObject.put("work_id", workId);

        } catch (Exception e) {

            Log.e("e", "postData: " + e.getMessage());
        }
        JsonObjectRequest request = new JsonObjectRequest(POST,
                "https://studentucas.awamr.com/api/auth/register/user",
                jsonObject, new Response.Listener<JSONObject>() {

            JSONObject object;
            String token = "Bearer ";

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getBoolean("success")) {
                        object = response.getJSONObject("data");

                        Toast.makeText(SignUp.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        token += object.getString("token");
                        editor.putString(Constants.TOKEN, token);
                        editor.apply();
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);

                    } else {
                        hideAnimation();
                        Toast.makeText(SignUp.this, "" + response.get("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    Log.e("Exception1", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideAnimation();
                Toast.makeText(SignUp.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

}