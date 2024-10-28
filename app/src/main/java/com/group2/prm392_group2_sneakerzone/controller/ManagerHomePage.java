package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.group2.prm392_group2_sneakerzone.R;

public class ManagerHomePage extends AppCompatActivity {

    private Button btnAddBrand;
    private Button btnManageStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home_page);

        btnAddBrand = findViewById(R.id.btnAddBrand);

        // Set click listener to navigate to BrandManagementPage
        btnAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerHomePage.this, BrandManagementPage.class);
                startActivity(intent);
            }
        });

        btnManageStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerHomePage.this, StoreManagement.class);
                startActivity(intent);
            }
        });
    }
}
