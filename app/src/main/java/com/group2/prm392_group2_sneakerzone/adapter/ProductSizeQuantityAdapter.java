package com.group2.prm392_group2_sneakerzone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.ProductSize;
import java.util.List;

public class ProductSizeQuantityAdapter extends RecyclerView.Adapter<ProductSizeQuantityAdapter.ProductSizeViewHolder> {

    private List<ProductSize> productSizes;

    public ProductSizeQuantityAdapter(List<ProductSize> productSizes) {
        this.productSizes = productSizes;
    }

    @NonNull
    @Override
    public ProductSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_size_quantity, parent, false);
        return new ProductSizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSizeViewHolder holder, int position) {
        ProductSize productSize = productSizes.get(position);
        holder.sizeTextView.setText("Size: " + productSize.getSize());
        holder.quantityTextView.setText("Quantity: " + productSize.getQuantity());
    }

    @Override
    public int getItemCount() {
        return productSizes != null ? productSizes.size() : 0;
    }

    public void updateData(List<ProductSize> newProductSizes) {
        this.productSizes = newProductSizes;
        notifyDataSetChanged();
    }

    public static class ProductSizeViewHolder extends RecyclerView.ViewHolder {
        TextView sizeTextView, quantityTextView;

        public ProductSizeViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeTextView = itemView.findViewById(R.id.sizeTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }
    }
}
