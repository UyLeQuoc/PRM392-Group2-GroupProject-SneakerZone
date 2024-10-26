package com.group2.prm392_group2_sneakerzone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.User;

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

        // Handle edit and delete button clicks
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
        public TextView tvUserName, tvUserEmail;
        public Button btnEdit, btnDelete;

        public UserViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tvUserName);
            tvUserEmail = view.findViewById(R.id.tvUserEmail);
            btnEdit = view.findViewById(R.id.btnEditUser);
            btnDelete = view.findViewById(R.id.btnDeleteUser);
        }
    }

    // Interface to handle user actions
    public interface OnUserActionListener {
        void onEditUser(User user);                 // Called when edit button is clicked
        void onConfirmDeleteUser(int userId);       // Called when delete button is clicked
    }
}
