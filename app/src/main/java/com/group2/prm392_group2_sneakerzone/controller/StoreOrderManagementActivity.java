package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.OrderAdapter;
import com.group2.prm392_group2_sneakerzone.model.Order;
import com.group2.prm392_group2_sneakerzone.utils.OrderDBHelper;

import java.util.List;

public class StoreOrderManagementActivity extends AppCompatActivity {

    private static final String TAG = "StoreOrderManagementActivity";
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private int storeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_management);

        // Get StoreId from Intent
        Intent intent = getIntent();
        storeId = intent.getIntExtra("StoreId", -1);

        if (storeId == -1) {
            Toast.makeText(this, "Invalid Store ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Store ID received: " + storeId);

        // Initialize RecyclerView
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load orders from the database
        loadOrders();
    }

    private void loadOrders() {
        OrderDBHelper dbHelper = OrderDBHelper.getInstance(this);
        orderList = dbHelper.getAllOrders(); // Modify this line to filter by storeId

        // Filter orders by storeId
        orderList.removeIf(order -> order.getStoreId() != storeId);

        // Set up the adapter
        orderAdapter = new OrderAdapter(this, orderList, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onViewDetailsClick(int position) {
                Order order = orderList.get(position);
                Intent intent = new Intent(StoreOrderManagementActivity.this, OrderDetailPage.class);
                intent.putExtra("OrderId", order.getOrderId());
                startActivity(intent);
            }

//            @Override
//            public void onUpdateStatusClick(int position) {
//                // Handle update   status click
//                Order order = orderList.get(position);
//                // Implement status update logic
//                Toast.makeText(StoreOrderManagementActivity.this, "Updating status for Order ID: " + order.getOrderId(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDeleteClick(int position) {
//                // Handle delete order click
//                Order order = orderList.get(position);
//                OrderDBHelper.getInstance(StoreOrderManagementActivity.this).deleteOrder(order.getOrderId());
//                orderList.remove(position);
//                orderAdapter.notifyItemRemoved(position);
//                Toast.makeText(StoreOrderManagementActivity.this, "Order ID: " + order.getOrderId() + " deleted", Toast.LENGTH_SHORT).show();
//            }
        });

        ordersRecyclerView.setAdapter(orderAdapter);
    }
}
