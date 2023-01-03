package com.example.finalproject_customer.fragments;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject_customer.Data;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SlidesImagesWorksAdapter extends SliderViewAdapter {
    ArrayList<Data> drawables;

    public SlidesImagesWorksAdapter(Object p0, ArrayList<Data> sliderDataArrayList) {
        drawables = sliderDataArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        return null ;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

    }

    @Override
    public int getCount() {
        return 3;
    }
}
