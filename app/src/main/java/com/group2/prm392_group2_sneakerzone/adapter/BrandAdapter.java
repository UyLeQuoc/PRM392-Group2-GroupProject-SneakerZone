package com.group2.prm392_group2_sneakerzone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {

    private Context context;
    private List<Brand> brandList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public BrandAdapter(Context context, List<Brand> brandList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.brandList = brandList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brand, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.tvBrandName.setText(brand.getBrandName());
        holder.tvCreatedBy.setText(UserDBHelper.getInstance(context).getAllUsers().get(brand.getCreatedBy()-1).getName());
        holder.tvCreatedDate.setText(brand.getCreatedDate());

        holder.btnEdit.setOnClickListener(view -> onItemClickListener.onEditClick(position));
        holder.btnDelete.setOnClickListener(view -> onItemClickListener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {
        TextView tvBrandName, tvCreatedBy, tvCreatedDate;
        Button btnEdit, btnDelete;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBrandName = itemView.findViewById(R.id.tvBrandName);
            tvCreatedBy = itemView.findViewById(R.id.tvBrandCreatedBy);
            tvCreatedDate = itemView.findViewById(R.id.tvBrandCreatedDate);
            btnEdit = itemView.findViewById(R.id.btnBrandEdit);
            btnDelete = itemView.findViewById(R.id.btnBrandDelete);
        }
    }
}
