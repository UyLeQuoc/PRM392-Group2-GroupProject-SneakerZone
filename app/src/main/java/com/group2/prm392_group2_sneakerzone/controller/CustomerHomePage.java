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
import com.group2.prm392_group2_sneakerzone.adapter.StoreCustomerAdapter;
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.util.List;

public class CustomerHomePage extends AppCompatActivity {

    private RecyclerView recyclerViewStores;
    private StoreCustomerAdapter storeCustomerAdapter;
    private StoreDBHelper storeDBHelper;
    private Button buttonLogout, buttonOrders;
    private UserDBHelper userDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_page);
        userDBHelper = UserDBHelper.getInstance(this);

        recyclerViewStores = findViewById(R.id.recyclerViewStores);
        recyclerViewStores.setLayoutManager(new LinearLayoutManager(this));

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonOrders = findViewById(R.id.buttonOrders);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logout logic, e.g., clear user session and return to login page
                Toast.makeText(CustomerHomePage.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerHomePage.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        buttonOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logout logic, e.g., clear user session and return to login page
                Intent intent = new Intent(CustomerHomePage.this, CustomerOrderHistoryActivity.class);
                intent.putExtra("CustomerId", userDBHelper.getCurrentLoginUser().getUserId()); // Pass the fixed store ID of 1
                startActivity(intent);
                startActivity(intent);
                finish();
            }
        });

        storeDBHelper = StoreDBHelper.getInstance(this);
        List<Store> storeList = storeDBHelper.getAllStores();

        storeCustomerAdapter = new StoreCustomerAdapter(this, storeList, this::openStoreDetail);
        recyclerViewStores.setAdapter(storeCustomerAdapter);
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
