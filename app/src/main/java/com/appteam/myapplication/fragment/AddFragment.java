package com.appteam.myapplication.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.RatingBar;

import com.appteam.myapplication.R;
import com.appteam.myapplication.database.OrderDatabase;
import com.appteam.myapplication.databinding.FragmentAddBinding;
import com.appteam.myapplication.model.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {
    private static final int RESULT_LOAD_IMG = 1234;
    private FragmentAddBinding binding;
    Order orderAdd;
    FirebaseStorage storage;
    StorageReference mountainImagesRef;
    String thumbnail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);
        storage = FirebaseStorage.getInstance();
        orderAdd = new Order();
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
                        orderAdd.setDatePicker(now.getTime());
                        binding.editDate.setText(orderAdd.getDatePicker());
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                    .show();
        });
        binding.btnAdd.setOnClickListener(v -> {
            orderAdd.setItemName(binding.editName.getText().toString());
            orderAdd.setPrice(Double.parseDouble(binding.editPrice.getText().toString()));
            long id = OrderDatabase.getInstance(requireContext()).insertOrder(orderAdd);
            StorageReference storageRef = storage.getReference();
            mountainImagesRef = storageRef.child("images/"+id+".jpg");
            uploadImage();
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        binding.editRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> orderAdd.setRating(rating));
        binding.imageOrder.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        });
    }

    private void uploadImage() {
        binding.imageOrder.setDrawingCacheEnabled(true);
        binding.imageOrder.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.imageOrder.getDrawable()).getBitmap();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_LOAD_IMG){
                Uri uri = data.getData();
                Log.d("AppLog",uri.getPath().toString());
                thumbnail = uri.toString();
                binding.imageOrder.setImageURI(uri);
            }
        }
    }
}