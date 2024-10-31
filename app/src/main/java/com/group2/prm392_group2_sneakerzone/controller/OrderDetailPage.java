package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.Order;
import com.group2.prm392_group2_sneakerzone.utils.OrderDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

public class OrderDetailPage extends AppCompatActivity {

    private static final String TAG = "OrderDetailPage";
    private TextView tvOrderId, tvCustomerName, tvOrderDate, tvTotalAmount, tvOrderStatus;
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
        OrderDBHelper dbHelper = OrderDBHelper.getInstance(this);
        Order order = dbHelper.getOrderById(orderId);

        if (order != null) {
            tvOrderId.setText(String.valueOf(order.getOrderId()));
            tvCustomerName.setText(getCustomerName(order.getCustomerId()));
            tvOrderDate.setText(order.getOrderDate());
            tvTotalAmount.setText(String.format("$%.2f", order.getTotalAmount()));
            tvOrderStatus.setText(order.getOrderStatus());
        } else {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String getCustomerName(int customerId) {
        // Assuming UserDBHelper has a method to get user name by ID
        return UserDBHelper.getInstance(this).getUserById(customerId).getName();
    }
}
