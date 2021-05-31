package com.appteam.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appteam.myapplication.R;
import com.appteam.myapplication.databinding.ItemOrderBinding;
import com.appteam.myapplication.fragment.OnItemClick;
import com.appteam.myapplication.model.Order;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Logger;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;


    FirebaseStorage storage;
    private ArrayList<Order> listOrder;

    public void setListOrder(ArrayList<Order> listOrder) {
        this.listOrder = listOrder;

    }
    private CompositeDisposable compositeDisposable;
    private OnItemClick onItemClick;
    public OrderAdapter(ArrayList<Order> listOrder, OnItemClick onItemClick,Context context) {
        this.listOrder = listOrder;
        this.onItemClick = onItemClick;
        this.context = context;
        compositeDisposable = new CompositeDisposable();
        Log.d("AppLog",listOrder.size()+"");
        if(listOrder.isEmpty()){
            onItemClick.stopLoading();
            Toast.makeText(context,"There is no data here! Click button to add new!",Toast.LENGTH_SHORT).show();
        }
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

            storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference ref = storageRef.child("images/"+order.getId()+".jpg");

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("AppLog",uri.toString());
                    Glide.with(context)
                            .load(uri)
                            .into(binding.imageOrder);
                    if(listOrder.size()>0 && order.getId()==listOrder.get(listOrder.size()-1).getId()){
                        onItemClick.stopLoading();
                    }
                }

            });



            binding.itemName.setText(order.getItemName());
            binding.price.setText(order.getPrice() +" VND");
//            Log.d("AppLog",s.getPath());
//            binding.imageOrder.setImageURI(s);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemOrderClick(order);
                }
            });
        }
    }
}
