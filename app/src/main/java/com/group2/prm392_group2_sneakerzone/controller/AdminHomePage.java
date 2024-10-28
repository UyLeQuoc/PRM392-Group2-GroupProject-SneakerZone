package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.group2.prm392_group2_sneakerzone.R;

public class AdminHomePage extends AppCompatActivity {
    private Button btnUserManagement;
    private Button btnProductManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        btnUserManagement = findViewById(R.id.btnUserManagement);
        btnProductManagement = findViewById(R.id.btnProductManagement);


        btnUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, UserManagementPage.class);
                startActivity(intent);
            }
        });

        btnProductManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, ProductManagementPage.class);
                startActivity(intent);
            }
        });
    }
}