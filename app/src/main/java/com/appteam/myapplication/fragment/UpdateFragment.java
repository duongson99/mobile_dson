package com.appteam.myapplication.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.appteam.myapplication.R;
import com.appteam.myapplication.database.OrderDatabase;
import com.appteam.myapplication.databinding.FragmentUpdateBinding;
import com.appteam.myapplication.model.Order;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.logging.Logger;

import static android.app.Activity.RESULT_OK;


public class UpdateFragment extends Fragment {
    private static final int RESULT_LOAD_IMG = 2345;
    private FragmentUpdateBinding binding;
    private Order orderDetail;
    private String thumbnail;
    FirebaseStorage storage;
    StorageReference mountainImagesRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(inflater, container, false);
        orderDetail = new Order();
        orderDetail = (Order) getArguments().getSerializable("order");
        initView();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.editDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        now.set(Calendar.YEAR, year);
                        now.set(Calendar.MONTH, month);
                        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        orderDetail.setDatePicker(now.getTime());
                        binding.editDate.setText(orderDetail.getDatePicker());
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                    .show();
        });
        binding.btnDelete.setOnClickListener(v -> {
            OrderDatabase.getInstance(requireContext()).deleteOrder(orderDetail);
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        binding.btnUpdate.setOnClickListener(v -> {
            orderDetail.setItemName(binding.editName.getText().toString());
            orderDetail.setPrice(Double.parseDouble(binding.editPrice.getText().toString()));
            StorageReference storageRef = storage.getReference();
            mountainImagesRef = storageRef.child("images/"+orderDetail.getId()+".jpg");
            uploadImage();
            OrderDatabase.getInstance(requireContext()).updateOrder(orderDetail);
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        binding.editRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> orderDetail.setRating(rating));
        binding.imageOrderUpdate.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        });
    }

    private void initView() {
        binding.editName.setText(orderDetail.getItemName());
        binding.editDate.setText(orderDetail.getDatePicker());
        binding.editPrice.setText(String.valueOf(orderDetail.getPrice()));
        binding.editRating.setRating(orderDetail.getRating());
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child("images/"+orderDetail.getId()+".jpg");

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("AppLog",uri.toString());
                Glide.with(getContext())
                        .load(uri)
                        .into(binding.imageOrderUpdate);
            }

        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_LOAD_IMG){
                Uri uri = data.getData();
                binding.imageOrderUpdate.setImageURI(uri);
            }
        }
    }
    private void uploadImage() {
        binding.imageOrderUpdate.setDrawingCacheEnabled(true);
        binding.imageOrderUpdate.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.imageOrderUpdate.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }
}