package com.appteam.myapplication.fragment;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

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

import java.util.Calendar;


public class UpdateFragment extends Fragment {
    private FragmentUpdateBinding binding;
    private Order orderDetail;

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
            OrderDatabase.getInstance(requireContext()).updateOrder(orderDetail);
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        binding.editRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> orderDetail.setRating(rating));
    }

    private void initView() {
        binding.editName.setText(orderDetail.getItemName());
        binding.editDate.setText(orderDetail.getDatePicker());
        binding.editPrice.setText(String.valueOf(orderDetail.getPrice()));
        binding.editRating.setRating(orderDetail.getRating());
    }
}