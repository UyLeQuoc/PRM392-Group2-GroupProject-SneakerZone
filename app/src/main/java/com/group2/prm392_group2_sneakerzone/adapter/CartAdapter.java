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
import com.group2.prm392_group2_sneakerzone.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private OnCartItemInteractionListener listener;

    // Interface for handling cart item interactions
    public interface OnCartItemInteractionListener {
        void onIncreaseQuantity(CartItem cartItem);
        void onDecreaseQuantity(CartItem cartItem);
        void onRemoveItem(CartItem cartItem);
    }

    public CartAdapter(List<CartItem> cartItems, OnCartItemInteractionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        // Bind data to views
        holder.productName.setText(cartItem.getProduct().getProductName());
        holder.productSize.setText("Size: " + cartItem.getSize());
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.productPrice.setText("$" + cartItem.getProduct().getPrice());

        // Load product image with Glide
        Glide.with(holder.itemView.getContext())
                .load(cartItem.getProduct().getProductImage())
                .into(holder.productImage);

        // Set up listeners for quantity buttons and remove button
        holder.increaseQuantityButton.setOnClickListener(v -> listener.onIncreaseQuantity(cartItem));
        holder.decreaseQuantityButton.setOnClickListener(v -> listener.onDecreaseQuantity(cartItem));
        holder.removeButton.setOnClickListener(v -> listener.onRemoveItem(cartItem));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ViewHolder class for CartAdapter
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productSize, quantity, productPrice;
        ImageView productImage;
        Button increaseQuantityButton, decreaseQuantityButton, removeButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productSize = itemView.findViewById(R.id.productSize);
            quantity = itemView.findViewById(R.id.quantity);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
