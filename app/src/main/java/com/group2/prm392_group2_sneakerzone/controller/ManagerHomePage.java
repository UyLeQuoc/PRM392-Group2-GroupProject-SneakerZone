package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

public class ManagerHomePage extends AppCompatActivity {

    private Button btnManagement;
    private Button btnAddBrand;
    private Button btnManageStore;
    private Button btnLogout;
    private UserDBHelper userDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home_page);

        userDBHelper = UserDBHelper.getInstance(this);

        // Ánh xạ các nút
        btnManagement = findViewById(R.id.btnManagement);
        btnAddBrand = findViewById(R.id.btnAddBrand);
        btnManageStore = findViewById(R.id.manageStoresBtn);
        btnLogout = findViewById(R.id.button2);

        // Chuyển đến trang quản lý Shop Owner khi nhấn nút xxx Management
        btnManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerHomePage.this, ManagerShopOwnerActivity.class);
                startActivity(intent);
            }
        });

        // Chuyển đến trang quản lý thương hiệu
        btnAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerHomePage.this, BrandManagementPage.class);
                startActivity(intent);
            }
        });

        // Chuyển đến trang quản lý cửa hàng
        btnManageStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerHomePage.this, StoreManagement.class);
                startActivity(intent);
            }
        });

        // Đăng xuất và quay về màn hình đăng nhập
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDBHelper.setCurrentLoginUser(-1, -1); // Xóa thông tin đăng nhập hiện tại

                Intent intent = new Intent(ManagerHomePage.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa toàn bộ ngăn xếp
                startActivity(intent);
                finish(); // Kết thúc ManagerHomePage
            }
        });
    }
}
