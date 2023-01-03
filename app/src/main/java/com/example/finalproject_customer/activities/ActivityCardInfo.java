package com.example.finalproject_customer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.finalproject_customer.R;
import com.example.finalproject_customer.databinding.ActivityCardInfoBinding;
import com.example.finalproject_customer.interfaces.Constants;
import com.example.finalproject_customer.multipartrequest.DataPart;
import com.example.finalproject_customer.multipartrequest.VolleyMultipartRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityCardInfo extends AppCompatActivity implements OnMapReadyCallback {

    public static final int REQUEST_CODE = 101;
    ActivityCardInfoBinding binding;
    String token;
    SharedPreferences preferences;
    String details;
    boolean isDone;
    double latitude;
    double longitude;
    String workId, phone, moreDetails;
    Location currentLocation;
    FusedLocationProviderClient fusedLocation;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.animationView.setVisibility(View.INVISIBLE);
        fetchLastLocation();
        getDataFromShred();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etMoreDetails.getText().toString().isEmpty() &&
                        !binding.etPhoneNumber.getText().toString().isEmpty() &&
                        isDone) {

                    moreDetails = binding.etMoreDetails.getText().toString();
                    phone = binding.etPhoneNumber.getText().toString();

                    Uri pic = Uri.parse(getIntent().getStringExtra("pic"));
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ActivityCardInfo.this.getContentResolver(), pic);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadData(bitmap);
                } else {
                    Toast.makeText(ActivityCardInfo.this, "please enter the required data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.LOCATION_HARDWARE}, REQUEST_CODE);
            return;
        }

        fusedLocation = LocationServices.getFusedLocationProviderClient(this);

        Task<Location> task = fusedLocation.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    setupMap();
                    currentLocation = location;
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        if (currentLocation.getLatitude() > 0 || currentLocation.getLongitude() > 0) {
            isDone = true;
            googleMap.addMarker(new MarkerOptions().title("My Address").position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom
                    (new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), map.getMaxZoomLevel()));
        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                map.clear();
                ActivityCardInfo.this.latitude = latLng.latitude;
                ActivityCardInfo.this.longitude = latLng.longitude;
                isDone = true;
                googleMap.addMarker(new MarkerOptions().title("MY Address").position(latLng));
            }
        });

    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    void getDataFromShred() {

        preferences = getSharedPreferences(Constants.SHARED_KEY, 0);
        details = preferences.getString(Constants.DETAILS, "there is no details");

        token = preferences.getString(Constants.TOKEN, "");
    }

    private void uploadData(final Bitmap bitmap) {
        showAnimation();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                "https://studentucas.awamr.com/api/create/order",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"),
                                    Toast.LENGTH_SHORT).show();

                            if(obj.getBoolean("success"))
                            startActivity(new Intent(ActivityCardInfo.this, ActivityDoneChoice.class));

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        hideAnimation();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error + "", Toast.LENGTH_LONG).show();
                        Log.e("GotError", "" + error.getMessage());
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
                map.put("work_id", workId);
                map.put("details", details);
                map.put("details_address", moreDetails);
                map.put("phone", phone);
                map.put("lat", String.valueOf(latitude));
                map.put("long", String.valueOf(longitude));
                return map;

            }

            @Override
            protected Map<String, DataPart> getByteData() {

                Map<String, DataPart> params = new HashMap<>();

                long imageName = System.currentTimeMillis();

                params.put("photos[]", new DataPart(imageName + ".png", getFileDataFromDrawable(bitmap)));

                return params;
            }
        };
        Volley.newRequestQueue(this).add
                (volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLastLocation();
        }
    }

    void showAnimation() {
        binding.btnNext.setVisibility(View.INVISIBLE);
        binding.etPhoneNumber.setVisibility(View.INVISIBLE);
        binding.etMoreDetailsLayout.setVisibility(View.INVISIBLE);
        binding.fragment.setVisibility(View.INVISIBLE);
        binding.linearNumber.setVisibility(View.INVISIBLE);
        binding.animationView.setVisibility(View.VISIBLE);

    }

    void hideAnimation() {
        binding.btnNext.setVisibility(View.VISIBLE);
        binding.etPhoneNumber.setVisibility(View.VISIBLE);
        binding.etMoreDetailsLayout.setVisibility(View.VISIBLE);
        binding.linearNumber.setVisibility(View.VISIBLE);
        binding.fragment.setVisibility(View.VISIBLE);
        binding.animationView.setVisibility(View.INVISIBLE);
    }


}

