package com.group2.prm392_group2_sneakerzone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
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

        holder.tvProductName.setText(product.getProductName());
        holder.tvProductId.setText("ID: " + product.getProductId());
        holder.tvProductBrandId.setText("Brand ID: " + product.getBrandId());
        holder.tvProductDescription.setText(product.getDescription());

        // Set image resource for ImageView (you might need to change this based on your actual implementation)
        // holder.imgProduct.setImageResource(R.drawable.product_background); // Sử dụng ảnh mặc định nếu có
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgProduct;
        public TextView tvProductName, tvProductId, tvProductBrandId, tvProductDescription;

        public ProductViewHolder(View view) {
            super(view);

//               imgProduct = view.findViewById(R.id.imgProduct);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvProductId = view.findViewById(R.id.tvProductId);
            tvProductBrandId = view.findViewById(R.id.tvProductBrandId);
            tvProductDescription = view.findViewById(R.id.tvProductDescription);
        }
    }
}
