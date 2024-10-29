package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.ProductAdapter;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.utils.ProductDBHelper;
import java.util.List;

public class StoreDetailPage extends AppCompatActivity implements ProductAdapter.OnAddToCartClickListener {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private ProductDBHelper productDBHelper;
    private TextView storeName, storeLocation, storeOwner;
    private ImageView storeImageView;
    private Button viewCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        // Initialize views
        storeImageView = findViewById(R.id.storeImageView);
        storeName = findViewById(R.id.storeDetailName);
        storeLocation = findViewById(R.id.storeDetailLocation);
        storeOwner = findViewById(R.id.storeDetailOwner);
        viewCartButton = findViewById(R.id.viewCartButton);
        productRecyclerView = findViewById(R.id.productRecyclerView);

        // Get store details from the Intent
        Intent intent = getIntent();
        int storeId = intent.getIntExtra("STORE_ID", -1);
        String storeImage = intent.getStringExtra("STORE_IMAGE"); // Store image URL

        // Set store details
        storeName.setText(intent.getStringExtra("STORE_NAME"));
        storeLocation.setText(intent.getStringExtra("STORE_LOCATION"));
        storeOwner.setText("Owner ID: " + intent.getIntExtra("OWNER_ID", -1));

        // Load store image if available
        if (storeImage != null && !storeImage.isEmpty()) {
            Glide.with(this).load(storeImage).into(storeImageView);
        } else {
            storeImageView.setImageResource(R.drawable.ic_launcher_background); // Default image
        }

        // Initialize ProductDBHelper and load products
        productDBHelper = ProductDBHelper.getInstance(this);
        productList = productDBHelper.getProductsByStoreId(storeId);

        // Set up the product RecyclerView
        productAdapter = new ProductAdapter(this, productList, this);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productRecyclerView.setAdapter(productAdapter);

        // View Cart Button to open Cart Drawer
        viewCartButton.setOnClickListener(view -> showCartDrawer());
    }

    private void showCartDrawer() {
        BottomSheetDialog cartDrawer = new BottomSheetDialog(this);
        cartDrawer.setContentView(R.layout.layout_cart_drawer);
        cartDrawer.show();
    }

    @Override
    public void onAddToCartClick(Product product) {
        showAddToCartPopup(product);
    }

    private void showAddToCartPopup(Product product) {
        BottomSheetDialog addToCartDialog = new BottomSheetDialog(this);
        addToCartDialog.setContentView(R.layout.dialog_add_to_cart);

        TextView productName = addToCartDialog.findViewById(R.id.productName);
        TextView productPrice = addToCartDialog.findViewById(R.id.productPrice);
        Button addToCartButton = addToCartDialog.findViewById(R.id.addToCartButton);

        // Set product details
        productName.setText(product.getProductName());
        productPrice.setText("$" + product.getPrice());

        // Handle Add to Cart action
        addToCartButton.setOnClickListener(view -> {
            // Here, you could add the selected product, size, and quantity to the cart
            addToCartDialog.dismiss();
        });

        addToCartDialog.show();
    }
}
