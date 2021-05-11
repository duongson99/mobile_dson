package com.appteam.myapplication.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

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

import java.util.Calendar;
import java.util.Date;

public class AddFragment extends Fragment {
    private FragmentAddBinding binding;
    Order orderAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);
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
            OrderDatabase.getInstance(requireContext()).insertOrder(orderAdd);
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        binding.editRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> orderAdd.setRating(rating));
    }
}