package com.appteam.myapplication.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appteam.myapplication.R;
import com.appteam.myapplication.adapter.OrderAdapter;
import com.appteam.myapplication.database.OrderDatabase;
import com.appteam.myapplication.databinding.FragmentMainBinding;
import com.appteam.myapplication.model.Order;

public class MainFragment extends Fragment implements OnItemClick{
    private FragmentMainBinding binding;
    private OrderAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater,container,false);
        binding.progressbar.setVisibility(View.VISIBLE);
        setUpRecyclerView();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.fabAdd.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_mainFragment_to_addFragment));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.setListOrder(OrderDatabase.getInstance(requireContext()).searchOrder(query));
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setListOrder(OrderDatabase.getInstance(requireContext()).searchOrder(newText));
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void setUpRecyclerView() {
        adapter = new OrderAdapter(OrderDatabase.getInstance(requireContext()).getAllOrder(),this,getContext());
        binding.recyclerOrder.setAdapter(adapter);
        binding.recyclerOrder.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onItemOrderClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("order",order);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_mainFragment_to_updateFragment,bundle);
    }

    @Override
    public void stopLoading() {
        binding.progressbar.setVisibility(View.GONE);
    }
}