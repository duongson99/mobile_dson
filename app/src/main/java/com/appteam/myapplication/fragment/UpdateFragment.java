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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.appteam.myapplication.R;
import com.appteam.myapplication.model.Order;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class UpdateFragment extends Fragment {
    private static final int RESULT_LOAD_IMG = 2345;
    View view;
    private Order orderDetail;
    private String thumbnail;
    FirebaseStorage storage;
    StorageReference mountainImagesRef;
    StorageReference storageRef;
    EditText editDate, editName, editPrice;
    RatingBar ratingBar;
    ImageView imageOrder;
    Button btnDelete, btnUpdate;
    private DatabaseReference mDatabase;
    Calendar calendar = Calendar.getInstance();
    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        orderDetail = new Order();
        orderDetail = (Order) getArguments().getSerializable("order");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update, container, false);
        initView(view);


        setListeners();
        return view;
    }

    private void setListeners() {
        editDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        now.set(Calendar.YEAR, year);
                        now.set(Calendar.MONTH, month);
                        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        orderDetail.setDatePicker(now.getTime());
                        editDate.setText(orderDetail.getDatePicker());
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                    .show();
        });
        btnDelete.setOnClickListener(v -> {
            mountainImagesRef = storageRef.child("images/" + userCurrent.getUid() + "/" + orderDetail.getThumbnail() + ".jpg");
            mountainImagesRef.delete().addOnSuccessListener(
                    aVoid ->
                    {

                    })
                    .addOnFailureListener(exception -> {

                            }
                    );
            mDatabase.child(userCurrent.getUid()).child(orderDetail.getThumbnail() + "").removeValue();
            Navigation.findNavController(view).navigateUp();
        });
        btnUpdate.setOnClickListener(v -> {
            orderDetail.setItemName(editName.getText().toString());
            orderDetail.setPrice(Long.parseLong(editPrice.getText().toString()));
            Map<String, Object> updates = new HashMap<>();
            updates.put("datePicker", orderDetail.getDatePicker());
            updates.put("itemName", orderDetail.getItemName());
            updates.put("price", orderDetail.getPrice());
            updates.put("rating", orderDetail.getRating());
            mDatabase.child(userCurrent.getUid()).child(orderDetail.getThumbnail() + "").updateChildren(updates);
            mountainImagesRef = storageRef.child("images/" + userCurrent.getUid() + "/" + orderDetail.getThumbnail() + ".jpg");
            uploadImage();

        });
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> orderDetail.setRating((long) rating));
        imageOrder.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        });
    }

    private void initView(View view) {
        editDate = view.findViewById(R.id.edit_date);
        editName = view.findViewById(R.id.edit_name);
        editPrice = view.findViewById(R.id.edit_price);
        ratingBar = view.findViewById(R.id.edit_rating);
        imageOrder = view.findViewById(R.id.image_order_update);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnUpdate = view.findViewById(R.id.btn_update);
        progressBar = view.findViewById(R.id.progress_load);

        editName.setText(orderDetail.getItemName());
        editDate.setText(orderDetail.getDatePicker());
        editPrice.setText(String.valueOf(orderDetail.getPrice()));
        ratingBar.setRating(orderDetail.getRating());

        mountainImagesRef = storageRef.child("images/" + userCurrent.getUid() + "/" + orderDetail.getThumbnail() + ".jpg");

        mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("AppLog", uri.toString());
                Glide.with(getContext())
                        .load(uri)
                        .into(imageOrder);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG) {
                Uri uri = data.getData();
                imageOrder.setImageURI(uri);
            }
        }
    }

    private void uploadImage() {
        imageOrder.setDrawingCacheEnabled(true);
        imageOrder.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageOrder.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        progressBar.setVisibility(View.VISIBLE);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Navigation.findNavController(view).navigateUp();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}