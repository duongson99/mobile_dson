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
import android.widget.RatingBar;

import com.appteam.myapplication.R;
import com.appteam.myapplication.model.Order;
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

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {
    private static final int RESULT_LOAD_IMG = 1234;
    Order orderAdd;
    FirebaseStorage storage;
    StorageReference mountainImagesRef;
    String thumbnail;
    EditText editDate, editName, editPrice;
    RatingBar ratingBar;
    ImageView imageOrder;
    Button btnAdd;
    View view;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);
        storage = FirebaseStorage.getInstance();
        orderAdd = new Order();
        initView(view);
        setListeners();
        return view;
    }

    private void initView(View view) {
        editDate = view.findViewById(R.id.edit_date);
        editName = view.findViewById(R.id.edit_name);
        editPrice = view.findViewById(R.id.edit_price);
        ratingBar = view.findViewById(R.id.edit_rating);
        imageOrder = view.findViewById(R.id.image_order);
        btnAdd = view.findViewById(R.id.btn_add);
    }

    private void setListeners() {
        editDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        now.set(Calendar.YEAR, year);
                        now.set(Calendar.MONTH, month);
                        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        orderAdd.setDatePicker(now.getTime());
                        editDate.setText(orderAdd.getDatePicker());
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                    .show();
        });
        btnAdd.setOnClickListener(v -> {
            orderAdd.setItemName(editName.getText().toString());
            orderAdd.setPrice(Long.parseLong(editPrice.getText().toString()));
            Calendar calendar = Calendar.getInstance();
            FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
            orderAdd.setThumbnail(calendar.getTimeInMillis());
            mDatabase.child(userCurrent.getUid()).child(calendar.getTimeInMillis()+"").setValue(orderAdd);
            StorageReference storageRef = storage.getReference();
            mountainImagesRef = storageRef.child("images/" +userCurrent.getUid()+"/" +orderAdd.getThumbnail()+ ".jpg");
            uploadImage();

        });
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> orderAdd.setRating((long)rating));
        imageOrder.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        });
    }

    private void uploadImage() {
        imageOrder.setDrawingCacheEnabled(true);
        imageOrder.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageOrder.getDrawable()).getBitmap();
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
                Navigation.findNavController(view).navigateUp();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG) {
                Uri uri = data.getData();
                Log.d("AppLog", uri.getPath().toString());
                thumbnail = uri.toString();
                imageOrder.setImageURI(uri);
            }
        }
    }
}