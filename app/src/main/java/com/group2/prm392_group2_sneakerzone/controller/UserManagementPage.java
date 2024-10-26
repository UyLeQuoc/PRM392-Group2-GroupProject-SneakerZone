package com.group2.prm392_group2_sneakerzone.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.UserAdapter;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.util.List;

public class UserManagementPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private UserDBHelper userDBHelper;
    private FloatingActionButton fabAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management_page);

        userDBHelper = UserDBHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerViewUsers);
        fabAddUser = findViewById(R.id.fabAddUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadUsers();

        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserManagementPage.this, AddUserActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void loadUsers() {
        List<User> userList = userDBHelper.getAllUsers();
        userAdapter = new UserAdapter(userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEditUser(User user) {
                // Start EditUserActivity with the user's data
                Intent intent = new Intent(UserManagementPage.this, EditUserActivity.class);
                intent.putExtra("USER_ID", user.getUserId());
                startActivityForResult(intent, 101);
            }

            @Override
            public void onConfirmDeleteUser(int userId) {
                // Show confirmation dialog for deletion
                new AlertDialog.Builder(UserManagementPage.this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userDBHelper.deleteUser(userId);
                                Toast.makeText(UserManagementPage.this, "User deleted", Toast.LENGTH_SHORT).show();
                                loadUsers();  // Refresh the list after deletion
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        recyclerView.setAdapter(userAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadUsers();  // Reload the user list after add/edit
        }
    }
}
