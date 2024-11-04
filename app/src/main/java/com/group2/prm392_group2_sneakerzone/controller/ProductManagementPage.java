package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.ProductAdapter;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.utils.ProductDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductDBHelper productDBHelper;
    private int storeId;
    List<Product> productList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        productDBHelper = ProductDBHelper.getInstance(this);

        // Get StoreId from Intent
        Intent intent = getIntent();
        storeId = intent.getIntExtra("StoreId", -1);

        if (storeId == -1) {
            Toast.makeText(this, "Invalid Store ID", Toast.LENGTH_SHORT).show();
            finish();
            productList = productDBHelper.getAllProducts();

        }else{
            productList = productDBHelper.getProductsByStoreId(storeId);

        }

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList,this);
        recyclerView.setAdapter(productAdapter);
    }
}
