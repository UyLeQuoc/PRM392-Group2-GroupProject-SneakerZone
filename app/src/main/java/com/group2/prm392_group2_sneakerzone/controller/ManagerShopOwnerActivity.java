package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.ShopOwnerAdapter;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.util.List;

public class ManagerShopOwnerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShopOwnerAdapter shopOwnerAdapter;
    private UserDBHelper userDBHelper;
    private FloatingActionButton fabAddShopOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_user_role_shop_owner_page);

        userDBHelper = UserDBHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerViewUserShopOwner);
        fabAddShopOwner = findViewById(R.id.fabAddUserShopOwner);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadShopOwners();

        fabAddShopOwner.setOnClickListener(view -> {
            Intent intent = new Intent(ManagerShopOwnerActivity.this, AddShopOwnerActivity.class);
            startActivityForResult(intent, 100); // Request code 100 for adding new Shop Owner
        });
    }

    private void loadShopOwners() {
        List<User> shopOwnerList = userDBHelper.getUsersByRole(2); // Load users with role = 2 (Shop Owner)
        shopOwnerAdapter = new ShopOwnerAdapter(shopOwnerList, new ShopOwnerAdapter.OnShopOwnerActionListener() {
            @Override
            public void onEditShopOwner(User user) {
                Intent intent = new Intent(ManagerShopOwnerActivity.this, UpdateShopOwnerActivity.class);
                intent.putExtra("USER_ID", user.getUserId());
                startActivityForResult(intent, 101); // Request code 101 for updating Shop Owner
            }

            @Override
            public void onConfirmDeleteShopOwner(int userId) {
                userDBHelper.deleteUser(userId);
                Toast.makeText(ManagerShopOwnerActivity.this, "Shop Owner deleted", Toast.LENGTH_SHORT).show();
                loadShopOwners();  // Reload list after deletion
            }
        });
        recyclerView.setAdapter(shopOwnerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Reload the list of Shop Owners after add or update
            loadShopOwners();
        }
    }
}
