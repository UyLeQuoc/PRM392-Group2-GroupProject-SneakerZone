package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.StoreAdapter;
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;
import java.util.List;

public class CustomerHomePage extends AppCompatActivity {

    private RecyclerView recyclerViewStores;
    private StoreAdapter storeAdapter;
    private StoreDBHelper storeDBHelper;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_page);

        recyclerViewStores = findViewById(R.id.recyclerViewStores);
        recyclerViewStores.setLayoutManager(new LinearLayoutManager(this));

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Placeholder for logout action
                Toast.makeText(CustomerHomePage.this, "Logging out...", Toast.LENGTH_SHORT).show();
                // Navigate to the login screen or perform logout actions here
                Intent intent = new Intent(CustomerHomePage.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close current activity
            }
        });

        storeDBHelper = StoreDBHelper.getInstance(this);
        List<Store> storeList = storeDBHelper.getAllStores(); // Retrieve all stores from the database

        storeAdapter = new StoreAdapter(this, storeList);
        recyclerViewStores.setAdapter(storeAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storeDBHelper.close(); // Close database helper when activity is destroyed
    }
}
