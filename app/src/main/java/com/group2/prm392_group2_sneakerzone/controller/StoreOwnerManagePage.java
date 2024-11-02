package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group2.prm392_group2_sneakerzone.R;
import java.io.File;

public class StoreOwnerManagePage extends AppCompatActivity {

    private TextView txtStoreName, txtStoreLocation;
    private ImageView ivStoreImage;
    private Button btnViewOrder;
    private Button btnViewProducts;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_management);

        txtStoreName = findViewById(R.id.txt_store_name);
        txtStoreLocation = findViewById(R.id.txt_store_location);
        ivStoreImage = findViewById(R.id.img_store);
        btnViewOrder = findViewById(R.id.btn_view_store_orders);
        btnViewProducts = findViewById(R.id.btn_view_store_products);

        Intent intent = getIntent();
        storeId = intent.getIntExtra("store_id", -1);
        String storeName = intent.getStringExtra("store_name");
        String storeLocation = intent.getStringExtra("store_location");
        String imagePath = intent.getStringExtra("store_image");

        txtStoreName.setText(storeName);
        txtStoreLocation.setText(storeLocation);
        if (imagePath != null) {
            ivStoreImage.setImageURI(Uri.fromFile(new File(imagePath)));
        }
        btnViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerManagePage.this, StoreOrderManagementActivity.class);
                intent.putExtra("StoreId", storeId); // Pass the fixed store ID of 1
                startActivity(intent);
            }
        });

        btnViewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerManagePage.this, ProductManagementPage.class);
                intent.putExtra("StoreId", storeId); // Pass the fixed store ID of 1
                startActivity(intent);
            }
        });
    }
}
