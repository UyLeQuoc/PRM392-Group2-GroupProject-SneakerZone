package com.group2.prm392_group2_sneakerzone.adapter;

import android.content.Context;
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
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.utils.BrandDBHelper;

import java.io.File;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {


    private final List<Product> productList;
    private OnAddToCartClickListener onAddToCartClickListener;
    private BrandDBHelper brandDBHelper;
    private final Context context;




    // Interface for "Add to Cart" button click listener
    public interface OnAddToCartClickListener {
        void onAddToCartClick(Product product);
    }

    // Constructor for ProductManagementPage without context and listener
    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    // Constructor for StoreDetailPage with context and listener
    public ProductAdapter(Context context, List<Product> productList, OnAddToCartClickListener listener) {
        this.productList = productList;
        this.onAddToCartClickListener = listener;
        this.brandDBHelper = BrandDBHelper.getInstance(context);
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Corrected the layout to item_product_customer
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("$" + product.getPrice());
        holder.description.setText(product.getDescription());

        // Only fetch brand if BrandDBHelper is available (for StoreDetailPage)
        if (brandDBHelper != null) {
            Brand brand = brandDBHelper.getBrandById(product.getBrandId());
            if (brand != null) {
                holder.brandName.setText("Brand: " + brand.getBrandName());
            }
        } else {
            holder.brandName.setText("Brand ID: " + product.getBrandId());
        }

        // Load the store image using Glide
        File imageFile = new File(product.getProductImage());
        if (imageFile.exists()) {
            Glide.with(context)
                    .load(Uri.fromFile(imageFile))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Load product image
       // Glide.with(holder.itemView.getContext()).load(product.getProductImage()).into(holder.productImage);

        // Set visibility of the "Add to Cart" button based on whether the listener is present
//        if (onAddToCartClickListener != null) {
//            holder.addToCartButton.setVisibility(View.VISIBLE);
//            holder.addToCartButton.setOnClickListener(v -> onAddToCartClickListener.onAddToCartClick(product));
//        } else {
//            holder.addToCartButton.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, brandName, description;
      //  Button addToCartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgProduct);
            productName = itemView.findViewById(R.id.tvProductName);
            productPrice = itemView.findViewById(R.id.tvProductPrice);
            brandName = itemView.findViewById(R.id.tvProductBrandName);
            description = itemView.findViewById(R.id.tvProductDescription);

            //addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
