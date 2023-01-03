package com.example.finalproject_customer.adapters.workadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_customer.R;
import com.example.finalproject_customer.databinding.RecyclerItemBinding;
import com.example.finalproject_customer.interfaces.OnClick;

import java.util.ArrayList;

public class WorksAdapter extends RecyclerView.Adapter<WorkViewHolder> {

    ArrayList<Work> works;
    Context context;
    OnClick onClick;

    public WorksAdapter(ArrayList<Work> works, Context context, OnClick onClick) {

        this.works = works;
        this.onClick = onClick;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemBinding binding = RecyclerItemBinding.inflate(LayoutInflater.from(context), parent, false);

        return new WorkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {
        Work work = works.get(position);
        holder.description.setText(work.getDescription());

//        Glide.with(context).load(order.image).into(holder.imageView);
        holder.imageView.setImageResource(R.drawable.carpenter);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClickListener(Integer.parseInt(work.getWorkId()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return works.size();
    }

}
