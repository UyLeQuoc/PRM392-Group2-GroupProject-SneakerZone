package com.group2.prm392_group2_sneakerzone.adapter;

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
import com.group2.prm392_group2_sneakerzone.model.User;

import java.io.File;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnUserActionListener onUserActionListener;

    public UserAdapter(List<User> userList, OnUserActionListener listener) {
        this.userList = userList;
        this.onUserActionListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUserName.setText(user.getName());
        holder.tvUserEmail.setText(user.getEmail());

        // Set user role text based on the role value
        String roleText;
        switch (user.getRole()) {
            case 2:
                roleText = "Store Owner";
                break;
            case 3:
                roleText = "Manager";
                break;
            case 4:
                roleText = "Customer";
                break;
            default:
                roleText = "Unknown";
                break;
        }
        holder.tvUserRole.setText(roleText);

        // Load the user image if it exists
        if (user.getUserImage() != null) {
            Glide.with(holder.ivUserImage.getContext())
                    .load(new File(user.getUserImage()))
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.ivUserImage);
        } else {
            holder.ivUserImage.setImageResource(R.drawable.ic_placeholder); // Default image if no URI is available
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (onUserActionListener != null) {
                onUserActionListener.onEditUser(user);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (onUserActionListener != null) {
                onUserActionListener.onConfirmDeleteUser(user.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName, tvUserEmail, tvUserRole;
        public Button btnEdit, btnDelete;
        public ImageView ivUserImage;

        public UserViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tvUserName);
            tvUserEmail = view.findViewById(R.id.tvUserEmail);
            tvUserRole = view.findViewById(R.id.tvUserRole);
            ivUserImage = view.findViewById(R.id.ivUserImage);
            btnEdit = view.findViewById(R.id.btnEditUser);
            btnDelete = view.findViewById(R.id.btnDeleteUser);
        }
    }

    public interface OnUserActionListener {
        void onEditUser(User user);
        void onConfirmDeleteUser(int userId);
    }
}
