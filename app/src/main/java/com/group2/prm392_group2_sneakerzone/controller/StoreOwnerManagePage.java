package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.ProductAdapter;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.utils.ProductDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StoreOwnerManagePage extends AppCompatActivity {

    private TextView txtStoreName, txtStoreLocation;
    private ImageView ivStoreImage;
    private Button btnViewOrder;
    private Button btnAddProduct;
    private Button btnEdit;
    private int storeId;
    private Store store;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ProductDBHelper productDBHelper;
    private StoreDBHelper storeDBHelper;
    List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_management);

        productDBHelper = ProductDBHelper.getInstance(this);
        storeDBHelper = StoreDBHelper.getInstance(this);



        txtStoreName = findViewById(R.id.txt_store_name);
        txtStoreLocation = findViewById(R.id.txt_store_location);
        ivStoreImage = findViewById(R.id.img_store);
        btnViewOrder = findViewById(R.id.btn_view_store_orders);
        btnAddProduct = findViewById(R.id.btn_add_product);
        btnEdit = findViewById(R.id.btn_edit);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);



        Intent intent = getIntent();
        storeId = intent.getIntExtra("store_id", -1);
        store = storeDBHelper.getStoreById(storeId);
        String storeName = intent.getStringExtra("store_name");
        String storeLocation = intent.getStringExtra("store_location");
        String imagePath = intent.getStringExtra("store_image");

        loadAllProducts();


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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerManagePage.this, EditStoreActivity.class);
                intent.putExtra("store_id", store.getStoreId());
                intent.putExtra("store_name", store.getStoreName());
                intent.putExtra("store_location", store.getLocation());
                intent.putExtra("store_image", store.getStoreImage());
                intent.putExtra("created_date", store.getCreatedDate());
                startActivityForResult(intent, 100);
            }
        });



        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreOwnerManagePage.this, AddProductActivity.class);
                intent.putExtra("StoreId", storeId); // Pass the fixed store ID of 1
                startActivityForResult(intent, 100);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadAllProducts();  // Reload the user list after add/edit
            Toast.makeText(StoreOwnerManagePage.this, "Product saved successfully!", Toast.LENGTH_SHORT).show();

        }
    }

    private void loadAllProducts() {
        if (storeId == -1) {
            Toast.makeText(this, "Invalid Store ID", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            productList = productDBHelper.getProductsByStoreId(storeId);
        }

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList, this, new ProductAdapter.OnProductActionListener() {
            @Override
            public void onEditProduct(Product product) {
                Intent editProductIntent = new Intent(StoreOwnerManagePage.this, EditProductActivity.class);
                editProductIntent.putExtra("PRODUCT_ID", product.getProductId());
                editProductIntent.putExtra("StoreId", storeId);  // Truyền StoreId qua EditProductActivity

                startActivityForResult(editProductIntent, 101);
            }


        });
        recyclerViewProducts.setAdapter(productAdapter);
    }

}
