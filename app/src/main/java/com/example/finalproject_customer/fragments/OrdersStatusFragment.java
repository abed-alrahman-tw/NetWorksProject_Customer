package com.example.finalproject_customer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalproject_customer.adapters.viewpageradapter.ViewPagerAdapter;
import com.example.finalproject_customer.databinding.FragmentOrdersStatusBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class OrdersStatusFragment extends Fragment {

    FragmentOrdersStatusBinding binding;
    TabLayout tabLayout;
    Context context;
    ViewPager2 viewPager2;

    public OrdersStatusFragment() {
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

        binding = FragmentOrdersStatusBinding.inflate(inflater, container, false);
        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager2;

        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Pending");
        tabs.add("UnderWay");
        tabs.add("Completed");
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(OrdersFragment.newInstance("/order/pending/user"));
        fragments.add(OrdersFragment.newInstance("/order/un/complete/user"));
        fragments.add(OrdersFragment.newInstance("/order/complete/user"));

        viewPager2.setAdapter(new ViewPagerAdapter((FragmentActivity) context, fragments));


        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout,
                viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                tab.setText(tabs.get(position));

            }
        });
        mediator.attach();

        return binding.getRoot();
    }

}
