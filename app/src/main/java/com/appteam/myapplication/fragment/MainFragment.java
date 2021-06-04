package com.appteam.myapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appteam.myapplication.R;
import com.appteam.myapplication.adapter.OrderAdapter;
import com.appteam.myapplication.model.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class MainFragment extends Fragment implements OnItemClick{
    private OrderAdapter adapter;
    View view;
    ProgressBar progressbar;
    FloatingActionButton fabAdd;
    SearchView searchView;
    RecyclerView recyclerOrder;
    private DatabaseReference mDatabase;
    FirebaseUser currentUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference(currentUser.getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main,container, false);
        initView(view);
        progressbar.setVisibility(View.VISIBLE);
        setUpRecyclerView();
        setListeners();
        return view;
    }

    private void initView(View view) {
        progressbar = view.findViewById(R.id.progressbar);
        fabAdd = view.findViewById(R.id.fab_add);
        searchView = view.findViewById(R.id.search_view);
        recyclerOrder = view.findViewById(R.id.recycler_order);
    }

    private void setListeners() {
        fabAdd.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addFragment));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.search(query);
                Log.d("AppLog",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.search(newText);
                return true;
            }
        });
    }

    private void setUpRecyclerView() {
        adapter = new OrderAdapter(this,getContext());
        Query queryOrder = mDatabase.orderByKey();
        queryOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<Order> listOrder = new ArrayList<>();
                if (snapshot.exists()){
                    HashMap<String, Order> dataMap = (HashMap<String, Order>) snapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                        Order order = new Order((String)userData.get("itemName"),(String)userData.get("datePicker"),
                                (Long) userData.get("price"),(long) userData.get("rating"),(Long)userData.get("thumbnail"));
//                        HashMap<String, Order> userData = (HashMap<String, Order>) data;
                        listOrder.add(order);
//                        Order order = data;
//                        Order mUser = new Order((String) userData.get("name"), (int) (long) userData.get("age"));
                    }
                }
                stopLoading();
                adapter.setListOrder(listOrder);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        recyclerOrder.setAdapter(adapter);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onItemOrderClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("order",order);
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_updateFragment,bundle);
    }

    @Override
    public void stopLoading() {
        progressbar.setVisibility(View.GONE);
    }
}