package com.group2.prm392_group2_sneakerzone.adapter;

import android.content.Context;
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
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.utils.BrandDBHelper;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<Product> productList;
    private OnAddToCartClickListener onAddToCartClickListener;
    private BrandDBHelper brandDBHelper;

    // Interface for "Add to Cart" button click listener
    public interface OnAddToCartClickListener {
        void onAddToCartClick(Product product);
    }

    // Constructor for ProductManagementPage without context and listener
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    // Constructor for StoreDetailPage with context and listener
    public ProductAdapter(Context context, List<Product> productList, OnAddToCartClickListener listener) {
        this.productList = productList;
        this.onAddToCartClickListener = listener;
        this.brandDBHelper = BrandDBHelper.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Corrected the layout to item_product_customer
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("$" + product.getPrice());

        // Only fetch brand if BrandDBHelper is available (for StoreDetailPage)
        if (brandDBHelper != null) {
            Brand brand = brandDBHelper.getBrandById(product.getBrandId());
            if (brand != null) {
                holder.brandName.setText("Brand: " + brand.getBrandName());
            }
        } else {
            holder.brandName.setText("Brand ID: " + product.getBrandId());
        }

        // Load product image
        Glide.with(holder.itemView.getContext()).load(product.getProductImage()).into(holder.productImage);

        // Set visibility of the "Add to Cart" button based on whether the listener is present
        if (onAddToCartClickListener != null) {
            holder.addToCartButton.setVisibility(View.VISIBLE);
            holder.addToCartButton.setOnClickListener(v -> onAddToCartClickListener.onAddToCartClick(product));
        } else {
            holder.addToCartButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, brandName;
        Button addToCartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            brandName = itemView.findViewById(R.id.brandName);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
