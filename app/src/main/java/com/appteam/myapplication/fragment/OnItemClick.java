package com.appteam.myapplication.fragment;

import com.appteam.myapplication.model.Order;

public interface OnItemClick {
    void onItemOrderClick(Order order);
    void stopLoading();
}
