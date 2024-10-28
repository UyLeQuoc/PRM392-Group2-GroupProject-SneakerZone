package com.group2.prm392_group2_sneakerzone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.Store;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    private List<Store> storeList;
    private Context context;

    public StoreAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store_customer, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.storeName.setText(store.getStoreName());
        holder.storeId.setText("ID: " + store.getStoreId());
        holder.storeLocation.setText("Location: " + store.getLocation());
        holder.storeOwnerId.setText("Owner ID: " + store.getOwnerId());

        // Load image if available; otherwise, use placeholder
        if (store.getStoreImage() != null && !store.getStoreImage().isEmpty()) {
            // Use any image loading library like Glide or Picasso here if needed
            holder.storeImage.setImageResource(R.drawable.ic_launcher_background); // Replace with actual loading code
        } else {
            holder.storeImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView storeImage;
        TextView storeName, storeId, storeLocation, storeOwnerId;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImage = itemView.findViewById(R.id.storeImage);
            storeName = itemView.findViewById(R.id.storeName);
            storeId = itemView.findViewById(R.id.storeId);
            storeLocation = itemView.findViewById(R.id.storeLocation);
            storeOwnerId = itemView.findViewById(R.id.storeOwnerId);
        }
    }
}


