package com.group2.prm392_group2_sneakerzone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.controller.AddOrUpdateProductSizeActivity;
import com.group2.prm392_group2_sneakerzone.model.ProductSize;

import java.util.List;

public class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.ProductSizeViewHolder> {

    private List<ProductSize> productSizeList;
    private Context context;

    public ProductSizeAdapter(List<ProductSize> productSizeList, Context context) {
        this.productSizeList = productSizeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_size, parent, false);
        return new ProductSizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSizeViewHolder holder, int position) {
        ProductSize productSize = productSizeList.get(position);
        holder.tvSize.setText("Size: " + productSize.getSize());
        holder.tvQuantity.setText("Quantity: " + productSize.getQuantity());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddOrUpdateProductSizeActivity.class);
            intent.putExtra("PRODUCT_SIZE_ID", productSize.getProductSizeId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productSizeList.size();
    }

    public static class ProductSizeViewHolder extends RecyclerView.ViewHolder {
        TextView tvSize, tvQuantity;
        Button btnEdit;

        public ProductSizeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}

