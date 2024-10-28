package com.group2.prm392_group2_sneakerzone.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.StoreAdapter;
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;

import java.util.List;

public class StoreManagement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StoreAdapter storeAdapter;
    private List<Store> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_management);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_stores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load stores from database
        StoreDBHelper dbHelper = StoreDBHelper.getInstance(this);
        storeList = dbHelper.getAllStores();

        // Set up adapter
        storeAdapter = new StoreAdapter(this, storeList);
        recyclerView.setAdapter(storeAdapter);

        // Set up Add button
        FloatingActionButton fabAddStore = findViewById(R.id.fab_add_store);
        fabAddStore.setOnClickListener(v -> {
            // Handle Add button logic later
            Toast.makeText(this, "Add button clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
