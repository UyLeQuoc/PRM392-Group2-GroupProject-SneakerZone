package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

public class EditUserActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etPhoneNumber, etAddress;
    private Button btnUpdateUser;
    private UserDBHelper userDBHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);

        userDBHelper = UserDBHelper.getInstance(this);

        // Retrieve user ID and details
        userId = getIntent().getIntExtra("USER_ID", -1);
        User user = userDBHelper.getUserById(userId);
        if (user != null) {
            etName.setText(user.getName());
            etEmail.setText(user.getEmail());
            etPassword.setText(user.getPassword());
            etPhoneNumber.setText(user.getPhoneNumber());
            etAddress.setText(user.getAddress());
        }

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String address = etAddress.getText().toString();

                User updatedUser = new User(userId, name, email, password, phoneNumber, address, 1, true, "");
                int result = userDBHelper.updateUser(updatedUser);

                if (result > 0) {
                    Toast.makeText(EditUserActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditUserActivity.this, "Failed to update user.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
