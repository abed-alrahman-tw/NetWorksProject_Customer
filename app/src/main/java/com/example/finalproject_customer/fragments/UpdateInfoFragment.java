package com.example.finalproject_customer.fragments;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.Request.Method.GET;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject_customer.activities.LogIn;
import com.example.finalproject_customer.databinding.FragmentUpdateInfoBinding;
import com.example.finalproject_customer.interfaces.Constants;
import com.example.finalproject_customer.multipartrequest.DataPart;
import com.example.finalproject_customer.multipartrequest.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UpdateInfoFragment extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String token;
    ActivityResultLauncher launcher;
    Uri picture;
    String name, phone;
    boolean imageSelected;
    String workId;
    FragmentUpdateInfoBinding binding;


    public UpdateInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(Constants.SHARED_KEY, 0);
        editor = preferences.edit();
        token = preferences.getString(Constants.TOKEN, "");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateInfoBinding.inflate(inflater, container, false);

        binding.animationView.setVisibility(View.INVISIBLE);
        getPhoto(binding);

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.nameEt.getText().toString();
                phone = binding.phoneEt.getText().toString();


                if (name != null && phone != null && imageSelected) {

                    Bitmap bitmap = null;

                    try {
                        bitmap =
                                MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picture);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    uploadData(bitmap, phone, name);

                } else
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


        return binding.getRoot();
    }

    private void uploadData(final Bitmap bitmap, String phone, String name) {
        showAnimation();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                "https://studentucas.awamr.com/api/update/user",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getContext(), obj.getString("message"),
                                    Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hideAnimation();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error + "", Toast.LENGTH_LONG).show();
                        hideAnimation();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);
                return map;

            }


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                workId = preferences.getString(Constants.WORK_ID, "");
                map.put("name", name);
                map.put("phone", phone);
                map.put("work_id", workId);

                return map;

            }

            @Override
            protected Map<String, DataPart> getByteData() {

                Map<String, DataPart> params = new HashMap<>();

                long imageName = System.currentTimeMillis();

                params.put("photo", new DataPart(imageName + ".png", getFileDataFromDrawable(bitmap)));

                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add
                (volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    void getPhoto(FragmentUpdateInfoBinding binding) {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {


                        if (result.getResultCode() == RESULT_OK) {

                            Intent in = result.getData();
                            picture = in.getData();
                            imageSelected = true;
                            binding.imageView.setImageURI(picture);


                        } else if (result.getResultCode() == 0) {
                            Toast.makeText(getContext(),
                                    "UNDONE", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    void showAnimation() {
        binding.imageView.setVisibility(View.INVISIBLE);
        binding.logOut.setVisibility(View.INVISIBLE);
        binding.submitBtn.setVisibility(View.INVISIBLE);
        binding.loginProviderEtEmailLayout.setVisibility(View.INVISIBLE);
        binding.loginProviderEtPasswordLayout.setVisibility(View.INVISIBLE);
        binding.animationView.setVisibility(View.VISIBLE);
    }

    void hideAnimation() {
        binding.imageView.setVisibility(View.VISIBLE);
        binding.logOut.setVisibility(View.VISIBLE);
        binding.submitBtn.setVisibility(View.VISIBLE);
        binding.loginProviderEtEmailLayout.setVisibility(View.VISIBLE);
        binding.loginProviderEtPasswordLayout.setVisibility(View.VISIBLE);
        binding.animationView.setVisibility(View.INVISIBLE);
    }

    void logOut() {
        editor.putBoolean(Constants.IS_CHECKED, false);
        editor.apply();
        showAnimation();
        StringRequest request = new StringRequest(GET, "https://studentucas.awamr.com/api/auth/logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response));
                            Toast.makeText(getContext(), obj.getString("message"),
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getActivity(), LogIn.class));
                            getActivity().finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideAnimation();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("Authorization", token);
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }
}