package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.StoreAdapter;
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.util.List;

public class StoreOwnerHomePage extends AppCompatActivity {

    private RecyclerView recyclerViewStores;
    private StoreAdapter storeAdapter;
    private StoreDBHelper storeDBHelper;
    private FloatingActionButton fabAddStore;
    private Button btnLogout;
    private TextView tvWelcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_home_page);

        // Initialize UI components
        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        recyclerViewStores = findViewById(R.id.recycler_view_stores);
        fabAddStore = findViewById(R.id.fab_add_store);
        btnLogout = findViewById(R.id.button2);

        // Get current owner information
        User currentOwner = UserDBHelper.getInstance(this).getCurrentLoginUser();
        if (currentOwner != null) {
            tvWelcomeMessage.setText("Welcome, " + currentOwner.getName());
        }

        // Initialize StoreDBHelper and RecyclerView
        storeDBHelper = StoreDBHelper.getInstance(this);
        recyclerViewStores.setLayoutManager(new LinearLayoutManager(this));

        // Get stores owned by the current logged-in user
        int ownerId = UserDBHelper.currentUserId; // Assuming currentUserId is set globally
        List<Store> ownerStores = storeDBHelper.getStoresByOwnerId(ownerId);

        // Set up the RecyclerView adapter with the owner's stores
        storeAdapter = new StoreAdapter(this, ownerStores);
        recyclerViewStores.setAdapter(storeAdapter);

        // Floating Action Button to add a new store
        fabAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddStoreActivity or open a dialog
                Toast.makeText(StoreOwnerHomePage.this, "Add Store Clicked", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(StoreOwnerHomePage.this, AddStoreActivity.class);
                // startActivity(intent);
            }
        });

        // Logout button to log out the user
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logout logic, e.g., clear user session and return to login page
                Toast.makeText(StoreOwnerHomePage.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StoreOwnerHomePage.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }


}
