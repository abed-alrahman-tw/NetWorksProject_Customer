package com.example.finalproject_customer.adapters.workadapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_customer.databinding.RecyclerItemBinding;

public class WorkViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView description;

    public WorkViewHolder(@NonNull RecyclerItemBinding binding) {
        super(binding.getRoot());

        imageView = binding.imageView;
        description = binding.description;


    }
}
