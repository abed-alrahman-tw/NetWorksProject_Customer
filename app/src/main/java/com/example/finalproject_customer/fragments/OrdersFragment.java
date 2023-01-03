package com.example.finalproject_customer.fragments;

import static com.android.volley.Request.Method.GET;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject_customer.adapters.orderstatusadapter.OrderStatus;
import com.example.finalproject_customer.adapters.orderstatusadapter.OrderStatusAdapter;
import com.example.finalproject_customer.databinding.FragmentOrdersBinding;
import com.example.finalproject_customer.interfaces.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrdersFragment extends Fragment {

    private static final String KEY = "param1";
    Context context;
    String token;
    SharedPreferences preferences;
    OrderStatusAdapter adapter;
    ArrayList<OrderStatus> works = new ArrayList<>();
    FragmentOrdersBinding binding;

    private String key;
    private String workID, workName, time;

    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance(String key) {

        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();

        args.putString(KEY, key);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            key = getArguments().getString(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOrdersBinding.inflate(inflater);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), RecyclerView.VERTICAL, false));

        adapter = new OrderStatusAdapter(works);

        binding.recyclerView.setAdapter(adapter);

        post();


        return binding.getRoot();
    }

    void post() {
        showAnimation();
        preferences = context.getSharedPreferences(Constants.SHARED_KEY, 0);
        token = preferences.getString(Constants.TOKEN, "");


        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(GET, "https://studentucas.awamr.com/api" + key,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("success")) {
                        String message = response.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            workID = array.getJSONObject(i).getString("id");
                            time = array.getJSONObject(i).getString("created_at");
                            time = time.substring(0, time.indexOf("T"));
                            workName = array.getJSONObject(i).getJSONObject("work").getString("name");
                            works.add(new OrderStatus(workName, workID, time));
                            adapter.notifyItemChanged(works.size() - 1);

                        }
                    }
                    hideAnimation();

                } catch (JSONException e) {
                    Log.e("error", e.getMessage());
                    hideAnimation();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideAnimation();
                Log.e("onErrorResponse: ", error.networkResponse.toString());
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

    private void showAnimation() {
        binding.animationView.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideAnimation() {

        binding.animationView.setVisibility(View.INVISIBLE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }
}