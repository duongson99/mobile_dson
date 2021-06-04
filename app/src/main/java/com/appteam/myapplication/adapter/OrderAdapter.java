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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;


    FirebaseStorage storage;
    private List<Order> listOrder =  new ArrayList<>();
    private List<Order> listDisplay = new ArrayList<>();
    public void search(String search){
        listDisplay.removeAll(listDisplay);
        if(search.equals("")){
            listDisplay.addAll(listOrder);
        }else {
            for (Order order : listOrder) {
                if (order.getItemName().contains(search)) {
                    listDisplay.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void setListOrder(List<Order> listOrder) {
        this.listOrder.addAll(listOrder);
        this.listDisplay.addAll(listOrder);
        Log.d("AppLog",listOrder.size()+"");
        if(listOrder.isEmpty()){
            onItemClick.stopLoading();
            Toast.makeText(context,"There is no data here! Click button to add new!",Toast.LENGTH_SHORT).show();
        }

    }
    private CompositeDisposable compositeDisposable;
    private OnItemClick onItemClick;
    public OrderAdapter(OnItemClick onItemClick,Context context) {
        this.onItemClick = onItemClick;
        this.context = context;
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
        holder.bind(listDisplay.get(position));
    }

    @Override
    public int getItemCount() {
        return listDisplay.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;
        public OrderViewHolder(@NonNull @NotNull ItemOrderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        public void bind(Order order){
            Log.d("AppLog",order.toString());
            FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
            storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference mountainImagesRef = storageRef.child("images/" +userCurrent.getUid()+"/" +order.getThumbnail()+ ".jpg");

            mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("AppLog",uri.toString());
                    Glide.with(context)
                            .load(uri)
                            .into(binding.imageOrder);
                    if(listDisplay.size()>0 && order.getThumbnail().equals(listDisplay.get(listDisplay.size() - 1).getThumbnail())){
                        onItemClick.stopLoading();
                    }
                }

            });

            binding.itemName.setText(order.getItemName());
            binding.price.setText(order.getPrice() +" VND");
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemOrderClick(order);
                }
            });
        }
    }
}
