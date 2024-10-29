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
        buttonLogout.setOnClickListener(v -> {
            Toast.makeText(CustomerHomePage.this, "Logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CustomerHomePage.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        storeDBHelper = StoreDBHelper.getInstance(this);
        List<Store> storeList = storeDBHelper.getAllStores();

        storeAdapter = new StoreAdapter(this, storeList, this::openStoreDetail);
        recyclerViewStores.setAdapter(storeAdapter);
    }

    private void openStoreDetail(Store store) {
        Intent intent = new Intent(CustomerHomePage.this, StoreDetailPage.class);
        intent.putExtra("STORE_ID", store.getStoreId());
        intent.putExtra("STORE_NAME", store.getStoreName());
        intent.putExtra("STORE_LOCATION", store.getLocation());
        intent.putExtra("OWNER_ID", store.getOwnerId());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storeDBHelper.close();
    }
}
