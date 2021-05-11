package com.appteam.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appteam.myapplication.R;
import com.appteam.myapplication.databinding.ItemOrderBinding;
import com.appteam.myapplication.fragment.OnItemClick;
import com.appteam.myapplication.model.Order;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<Order> listOrder;

    public void setListOrder(ArrayList<Order> listOrder) {
        this.listOrder = listOrder;
    }

    private OnItemClick onItemClick;
    public OrderAdapter(ArrayList<Order> listOrder, OnItemClick onItemClick) {
        this.listOrder = listOrder;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @NotNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemOrderBinding view = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderViewHolder holder, int position) {
        holder.bind(listOrder.get(position));
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;
        public OrderViewHolder(@NonNull @NotNull ItemOrderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        public void bind(Order order){
            binding.itemName.setText(order.getItemName());
            binding.date.setText(order.getDatePicker());
            binding.price.setText(String.valueOf(order.getPrice()));
            binding.rating.setText(String.valueOf(order.getRating()));
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemOrderClick(order);
                }
            });
        }
    }
}
