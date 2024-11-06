package com.group2.prm392_group2_sneakerzone.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.ProductSize;
import com.group2.prm392_group2_sneakerzone.utils.ProductSizeDBHelper;

public class AddOrUpdateProductSizeActivity extends AppCompatActivity {

    private EditText etSize, etQuantity;
    private Button btnSaveProductSize;
    private ProductSizeDBHelper dbHelper;
    private int productId;
    private int productSizeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_product_size);

        etSize = findViewById(R.id.etSize);
        etQuantity = findViewById(R.id.etQuantity);
        btnSaveProductSize = findViewById(R.id.btnSaveProductSize);

        dbHelper = ProductSizeDBHelper.getInstance(this);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        if (getIntent().hasExtra("PRODUCT_SIZE_ID")) {
            productSizeId = getIntent().getIntExtra("PRODUCT_SIZE_ID", -1);
            loadProductSizeDetails();
        }

        btnSaveProductSize.setOnClickListener(v -> saveProductSize());
    }

    private void loadProductSizeDetails() {
        ProductSize productSize = dbHelper.getProductSizeById(productSizeId);
        if (productSize != null) {
            etSize.setText(productSize.getSize());
            etQuantity.setText(String.valueOf(productSize.getQuantity()));
        }
    }

    private void saveProductSize() {
        String size = etSize.getText().toString();
        int quantity = Integer.parseInt(etQuantity.getText().toString());

        if (productSizeId == -1) {
            // Create ProductSize
            dbHelper.addProductSize(new ProductSize(0, productId, size, quantity, "auto_created_date", "auto_updated_date"));
            Toast.makeText(this, "Product size added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            // Update ProductSize
            dbHelper.updateProductSize(new ProductSize(productSizeId, productId, size, quantity, "auto_created_date", "auto_updated_date"));
            Toast.makeText(this, "Product size updated successfully!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}

