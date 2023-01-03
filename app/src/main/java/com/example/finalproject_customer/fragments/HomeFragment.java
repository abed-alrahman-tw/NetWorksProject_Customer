package com.example.finalproject_customer.fragments;

import static com.android.volley.Request.Method.GET;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject_customer.Data;
import com.example.finalproject_customer.R;
import com.example.finalproject_customer.activities.ActivityChoiceCard;
import com.example.finalproject_customer.adapters.workadapter.Work;
import com.example.finalproject_customer.adapters.workadapter.WorksAdapter;
import com.example.finalproject_customer.databinding.FragmentHomeBinding;
import com.example.finalproject_customer.interfaces.Constants;
import com.example.finalproject_customer.interfaces.OnClick;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment implements OnClick {


    FragmentHomeBinding binding;
    RecyclerView recycler;
    ArrayList<Work> works;
    String token;
    WorksAdapter adapter;
    SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;
    LinearLayoutManager lm;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initializeViewsAndVars();
        post();
        return binding.getRoot();
    }

    void initializeViewsAndVars() {

        recycler = binding.recycler;
        lm = new GridLayoutManager(context, 3, RecyclerView.VERTICAL, false);
        works = new ArrayList<>();
        adapter = new WorksAdapter(works, context, this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(lm);
        preferences = context.getSharedPreferences(Constants.SHARED_KEY, 0);
        token = preferences.getString(Constants.TOKEN, "");
        editor = preferences.edit();

    }

    void post() {
        showAnimation();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderBy", "ASC");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(GET, "https://studentucas.awamr.com/api/all/works",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray;
                JSONObject dataObject;

                String image = "";
                String description = "";
                String workId = "";


                try {
                    if (response.getBoolean("success")) {

                        hideAnimation();
                        String message = response.getString("message");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        jsonArray = response.getJSONArray("data");

                        //fromArray
                        for (int i = 0; i < jsonArray.length(); i++) {

                            dataObject = jsonArray.getJSONObject(i);
                            workId = dataObject.getString("id");
                            description = dataObject.getString("description");
                            image = dataObject.getString("icon");
                            works.add(new Work(image, description, workId));
                            adapter.notifyItemInserted(works.size() - 1);
                        }


                    } else
                        hideAnimation();

                } catch (JSONException e) {
                    Log.e("errorJSONEXCEOPTION", e.getMessage());
                    hideAnimation();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("errorJSONEXCEOPTION", "e" + error.getMessage());
                hideAnimation();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("Authorization", token);

                return map;
            }
        };
        queue.add(request);
    }

    @Override
    public void onClickListener(int id) {
        editor.putString(Constants.WORK_ID, String.valueOf(id));
        editor.apply();
        startActivity(new Intent(context, ActivityChoiceCard.class));
    }

    void showAnimation() {
        binding.recycler.setVisibility(View.INVISIBLE);
        binding.homeCustomeTvSelectService.setVisibility(View.INVISIBLE);
        binding.animationView.setVisibility(View.VISIBLE);
    }

    void hideAnimation() {
        binding.recycler.setVisibility(View.VISIBLE);
        binding.homeCustomeTvSelectService.setVisibility(View.VISIBLE);
        binding.animationView.setVisibility(View.INVISIBLE);
    }
    public void indicator() {
        ArrayList<Data> sliderDataArrayList = new ArrayList<>();

        sliderDataArrayList.add(new Data(getResources().getDrawable(R.drawable.work2)));
        sliderDataArrayList.add(new Data(getResources().getDrawable(R.drawable.work1)));
        sliderDataArrayList.add(new Data(getResources().getDrawable(R.drawable.work3)));


        SlidesImagesWorksAdapter adapter = new SlidesImagesWorksAdapter(this, sliderDataArrayList);


        binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);


        binding.slider.setSliderAdapter(adapter);

        binding.slider.setScrollTimeInSec(2);

        binding.slider.setAutoCycle(true);

        binding.slider.startAutoCycle();
    }


}