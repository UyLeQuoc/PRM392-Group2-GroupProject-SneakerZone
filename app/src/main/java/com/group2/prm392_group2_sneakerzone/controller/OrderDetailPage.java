package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.OrderDetailAdapter;
import com.group2.prm392_group2_sneakerzone.model.Order;
import com.group2.prm392_group2_sneakerzone.model.OrderDetail;
import com.group2.prm392_group2_sneakerzone.utils.OrderDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.OrderDetailDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;
import java.util.List;

public class OrderDetailPage extends AppCompatActivity {

    private static final String TAG = "OrderDetailPage";
    private TextView tvOrderId, tvCustomerName, tvOrderDate, tvTotalAmount, tvOrderStatus;
    private RecyclerView rvOrderDetails;
    private int orderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_page);

        // Initialize TextViews
        tvOrderId = findViewById(R.id.tvOrderId);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);

        // Initialize RecyclerView
        rvOrderDetails = findViewById(R.id.rvOrderDetails);
        rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));

        // Get OrderId from Intent
        Intent intent = getIntent();
        orderId = intent.getIntExtra("OrderId", -1);

        if (orderId == -1) {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Order ID received: " + orderId);

        // Load order details
        loadOrderDetails();
    }

    private void loadOrderDetails() {
        // Load Order Information
        OrderDBHelper orderDBHelper = OrderDBHelper.getInstance(this);
        Order order = orderDBHelper.getOrderById(orderId);

        if (order != null) {
            tvOrderId.setText("Order ID: " + order.getOrderId());
            tvCustomerName.setText("Customer: " + getCustomerName(order.getCustomerId()));
            tvOrderDate.setText("Date: " + order.getOrderDate());
            tvTotalAmount.setText(String.format("Total: $%.2f", order.getTotalAmount()));
            tvOrderStatus.setText("Status: " + order.getOrderStatus());

            // Load Order Items
            loadOrderItems(orderId);
        } else {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadOrderItems(int orderId) {
        // Load Order Items
        OrderDetailDBHelper orderDetailDBHelper = OrderDetailDBHelper.getInstance(this);
        List<OrderDetail> orderDetails = orderDetailDBHelper.getOrderDetailsByOrderId(orderId);

        if (orderDetails != null && !orderDetails.isEmpty()) {
            OrderDetailAdapter adapter = new OrderDetailAdapter(this, orderDetails);
            rvOrderDetails.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No items found for this order", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCustomerName(int customerId) {
        // Assuming UserDBHelper has a method to get user name by ID
        return UserDBHelper.getInstance(this).getUserById(customerId).getName();
    }
}
