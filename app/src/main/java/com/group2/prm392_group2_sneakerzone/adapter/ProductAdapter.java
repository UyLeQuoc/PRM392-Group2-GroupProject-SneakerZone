package com.group2.prm392_group2_sneakerzone.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.group2.prm392_group2_sneakerzone.controller.EditProductActivity;
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.utils.BrandDBHelper;

import java.io.File;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private  List<Product> productList;
    private  OnProductActionListener onProductActionListener;
    private  BrandDBHelper brandDBHelper;
    private  Context context;

    public ProductAdapter(List<Product> productList, Context context, OnProductActionListener listener) {
        this.productList = productList;
        this.context = context;
        this.onProductActionListener = listener;
        this.brandDBHelper = BrandDBHelper.getInstance(context);
    }

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        this.brandDBHelper = BrandDBHelper.getInstance(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("$" + product.getPrice());
        holder.description.setText(product.getDescription());

        // Load brand name using BrandDBHelper
        Brand brand = brandDBHelper.getBrandById(product.getBrandId());
        if (brand != null) {
            holder.brandName.setText("Brand: " + brand.getBrandName());
        } else {
            holder.brandName.setText("Brand ID: " + product.getBrandId());
        }

        // Load product image using Glide
        File imageFile = new File(product.getProductImage());
        if (imageFile.exists()) {
            Glide.with(context)
                    .load(Uri.fromFile(imageFile))
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_placeholder);
        }

        // Set up detail button to open EditProductActivity
        holder.btnDetail.setOnClickListener(v -> {
            if (onProductActionListener != null) {
                onProductActionListener.onEditProduct(product);
            }
        });

        // Set up detail button to open EditProductActivity
        holder.btnDetail.setOnClickListener(v -> {
            if (onProductActionListener != null) {
                onProductActionListener.onEditProduct(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, brandName, description;
        Button btnDetail;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgProduct);
            productName = itemView.findViewById(R.id.tvProductName);
            productPrice = itemView.findViewById(R.id.tvProductPrice);
            brandName = itemView.findViewById(R.id.tvProductBrandName);
            description = itemView.findViewById(R.id.tvProductDescription);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }

    public interface OnProductActionListener {
        void onEditProduct(Product product);
    }
}
