package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.ProductSizeAdapter;
import com.group2.prm392_group2_sneakerzone.model.ProductSize;
import com.group2.prm392_group2_sneakerzone.utils.ProductSizeDBHelper;

import java.util.List;

public class ProductSizeDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProductSizes;
    private Button addProductSizeButton;
    private ProductSizeAdapter adapter;
    private List<ProductSize> productSizeList;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_size_details);

        recyclerViewProductSizes = findViewById(R.id.recyclerViewProductSizes);
        addProductSizeButton = findViewById(R.id.addProductSizeButton);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        loadProductSizes();

        addProductSizeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddOrUpdateProductSizeActivity.class);
            intent.putExtra("PRODUCT_ID", productId);
            startActivity(intent);
        });
    }

    private void loadProductSizes() {
        // Lấy danh sách ProductSize từ database cho productId
        ProductSizeDBHelper dbHelper = ProductSizeDBHelper.getInstance(this);
        productSizeList = dbHelper.getSizesByProductId(productId);

        recyclerViewProductSizes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductSizeAdapter(productSizeList, this);
        recyclerViewProductSizes.setAdapter(adapter);
    }
}

