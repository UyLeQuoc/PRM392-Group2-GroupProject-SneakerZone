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
    private Button btnLogout;
    private TextView tvWelcomeMessage;
    private List<Store> ownerStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_home_page);

        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        recyclerViewStores = findViewById(R.id.recycler_view_stores);
        btnLogout = findViewById(R.id.button2);

        User currentOwner = UserDBHelper.getInstance(this).getCurrentLoginUser();
        if (currentOwner != null) {
            tvWelcomeMessage.setText("Welcome, " + currentOwner.getName());
        }

        storeDBHelper = StoreDBHelper.getInstance(this);
        recyclerViewStores.setLayoutManager(new LinearLayoutManager(this));

        int ownerId = UserDBHelper.currentUserId;
        ownerStores = storeDBHelper.getStoresByOwnerId(ownerId);

        storeAdapter = new StoreAdapter(this, ownerStores);
        recyclerViewStores.setAdapter(storeAdapter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StoreOwnerHomePage.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StoreOwnerHomePage.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStoreList();
    }

    private void refreshStoreList() {
        ownerStores.clear();
        ownerStores.addAll(storeDBHelper.getStoresByOwnerId(UserDBHelper.currentUserId));
        storeAdapter.notifyDataSetChanged();
    }
}
