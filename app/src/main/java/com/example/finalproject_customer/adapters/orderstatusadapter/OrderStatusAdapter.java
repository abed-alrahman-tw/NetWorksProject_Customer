package com.example.finalproject_customer.adapters.orderstatusadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_customer.databinding.OrderStautsItemBinding;

import java.util.ArrayList;

public class OrderStatusAdapter extends RecyclerView.Adapter<OStatusViewHolder> {
    ArrayList<OrderStatus> works;

    public OrderStatusAdapter(ArrayList<OrderStatus> works) {
        this.works = works;
    }

    @NonNull
    @Override
    public OStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderStautsItemBinding binding = OrderStautsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);


        return new OStatusViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OStatusViewHolder holder, int position) {


        OrderStatus order = works.get(position);
        holder.jobName.setText(order.getJobName());
        holder.orderNumber.append(order.getOrderNumber());
        holder.orderDateTv.setText(order.getOrderDate());

    }

    @Override
    public int getItemCount() {
        return works.size();
    }
}
