package com.group2.prm392_group2_sneakerzone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.model.OrderDetail;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.model.ProductSize;
import com.group2.prm392_group2_sneakerzone.utils.BrandDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.ProductDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.ProductSizeDBHelper;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private List<OrderDetail> orderDetailList;
    private Context context;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetailList) {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);

        // Retrieve product information based on the product ID
        ProductSizeDBHelper productDBHelper = ProductSizeDBHelper.getInstance(context);
        ProductSize productSize = productDBHelper.getProductSizeById(orderDetail.getProductSizeId());
        Product product = ProductDBHelper.getInstance(context).getProductById(productSize.getProductId());
        BrandDBHelper brandDBHelper = BrandDBHelper.getInstance(context);
        Brand brand = product != null ? brandDBHelper.getBrandById(product.getBrandId()) : null;

        if (product != null) {
            holder.tvProductName.setText(product.getProductName());
            holder.tvUnitPrice.setText(String.format("Unit: $%.2f", product.getPrice()));
            holder.tvQuantity.setText("Qty: " + orderDetail.getQuantity());
            holder.tvSubtotal.setText(String.format("Subtotal: $%.2f", orderDetail.getQuantity() * product.getPrice()));

            if (brand != null) {
                holder.tvBrandName.setText(brand.getBrandName());
            }
            if (productSize != null) {
                holder.tvProductSize.setText("Size: " + productSize.getSize());
            }
            // Load the product image if available
            if (product.getProductImage() != null) {
                // Assuming you're using a library like Glide or Picasso for image loading
                Glide.with(context)
                        .load(product.getProductImage())
                        .placeholder(R.drawable.ic_launcher_background) // Placeholder image resource
                        .into(holder.ivProductImage);
            }else{
                Glide.with(context)
                        .load(R.drawable.ic_launcher_background)
                        .into(holder.ivProductImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvUnitPrice, tvSubtotal, tvBrandName, tvProductSize;
        ImageView ivProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductSize = itemView.findViewById(R.id.tvProductSize);
            tvBrandName = itemView.findViewById(R.id.tvBrandName);

        }
    }
}