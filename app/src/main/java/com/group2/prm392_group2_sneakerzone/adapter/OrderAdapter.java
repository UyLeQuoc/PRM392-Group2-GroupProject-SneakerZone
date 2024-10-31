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
import com.group2.prm392_group2_sneakerzone.model.Order;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onViewDetailsClick(int position);
//        void onUpdateStatusClick(int position);
//        void onDeleteClick(int position);
    }

    public OrderAdapter(Context context, List<Order> orderList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText(String.valueOf(order.getOrderId()));
        holder.tvOrderDate.setText(order.getOrderDate());
        holder.tvTotalAmount.setText(String.format("$%.2f", order.getTotalAmount()));
        holder.tvOrderStatus.setText(order.getOrderStatus());

        // Retrieve customer name based on ID
        holder.tvCustomerName.setText(UserDBHelper.getInstance(context).getUserById(order.getCustomerId()).getName());

        holder.btnViewDetails.setOnClickListener(view -> onItemClickListener.onViewDetailsClick(position));
//        holder.btnUpdateStatus.setOnClickListener(view -> onItemClickListener.onUpdateStatusClick(position));
//        holder.btnDelete.setOnClickListener(view -> onItemClickListener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerName, tvOrderDate, tvTotalAmount, tvOrderStatus;
        Button btnViewDetails, btnUpdateStatus, btnDelete;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            btnViewDetails = itemView.findViewById(R.id.btnViewOrderDetails);
//            btnUpdateStatus = itemView.findViewById(R.id.btnUpdateOrderStatus);
//            btnDelete = itemView.findViewById(R.id.btnDeleteOrder);
        }
    }
}
