package com.group2.prm392_group2_sneakerzone.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.controller.EditStoreActivity;
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;

import java.io.File;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private final Context context;
    private final List<Store> storeList;

    public StoreAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.storeName.setText(store.getStoreName());
        holder.storeLocation.setText(store.getLocation());

        // Load the store image using Glide
        File imageFile = new File(store.getStoreImage());
        if (imageFile.exists()) {
            Glide.with(context)
                    .load(Uri.fromFile(imageFile))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.storeImage);
        } else {
            holder.storeImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Handle Edit button click
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditStoreActivity.class);
            intent.putExtra("store_id", store.getStoreId());
            intent.putExtra("store_name", store.getStoreName());
            intent.putExtra("store_location", store.getLocation());
            intent.putExtra("store_image", store.getStoreImage());
            context.startActivity(intent);
        });

        // Handle Delete button click
        holder.btnDelete.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(context)
                    .setTitle("Delete Store")
                    .setMessage("Are you sure you want to delete this store?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Remove the store from the database
                        StoreDBHelper dbHelper = StoreDBHelper.getInstance(context);
                        dbHelper.deleteStore(store.getStoreId());

                        // Remove from list and notify adapter
                        storeList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, storeList.size());

                        Toast.makeText(context, "Store deleted successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView storeName, storeLocation;
        ImageView storeImage;
        Button btnEdit, btnDelete;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.txt_store_name);
            storeLocation = itemView.findViewById(R.id.txt_store_location);
            storeImage = itemView.findViewById(R.id.img_store);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}