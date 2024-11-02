package com.group2.prm392_group2_sneakerzone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.User;

import java.io.File;
import java.util.List;

public class ShopOwnerAdapter extends RecyclerView.Adapter<ShopOwnerAdapter.ShopOwnerViewHolder> {

    private List<User> shopOwnerList;
    private OnShopOwnerActionListener onShopOwnerActionListener;

    public ShopOwnerAdapter(List<User> shopOwnerList, OnShopOwnerActionListener listener) {
        this.shopOwnerList = shopOwnerList;
        this.onShopOwnerActionListener = listener;
    }

    @NonNull
    @Override
    public ShopOwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_role_shop_owner, parent, false);
        return new ShopOwnerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopOwnerViewHolder holder, int position) {
        User shopOwner = shopOwnerList.get(position);
        holder.tvUserName.setText(shopOwner.getName());
        holder.tvUserEmail.setText(shopOwner.getEmail());
        holder.tvUserRole.setText("Shop Owner");

        // Load user image if it exists
        if (shopOwner.getUserImage() != null) {
            Glide.with(holder.ivUserImage.getContext())
                    .load(new File(shopOwner.getUserImage()))
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.ivUserImage);
        } else {
            holder.ivUserImage.setImageResource(R.drawable.ic_placeholder);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (onShopOwnerActionListener != null) {
                onShopOwnerActionListener.onEditShopOwner(shopOwner);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (onShopOwnerActionListener != null) {
                onShopOwnerActionListener.onConfirmDeleteShopOwner(shopOwner.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopOwnerList.size();
    }

    public static class ShopOwnerViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName, tvUserEmail, tvUserRole;
        public Button btnEdit, btnDelete;
        public ImageView ivUserImage;

        public ShopOwnerViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tvUserName);
            tvUserEmail = view.findViewById(R.id.tvUserEmail);
            tvUserRole = view.findViewById(R.id.tvUserRole);
            ivUserImage = view.findViewById(R.id.ivUserImage);
            btnEdit = view.findViewById(R.id.btnEditUser);
            btnDelete = view.findViewById(R.id.btnDeleteUser);
        }
    }

    public interface OnShopOwnerActionListener {
        void onEditShopOwner(User user);
        void onConfirmDeleteShopOwner(int userId);
    }
}
