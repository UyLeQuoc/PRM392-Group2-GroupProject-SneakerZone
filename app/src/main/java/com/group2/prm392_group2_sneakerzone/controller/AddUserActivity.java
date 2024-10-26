package com.group2.prm392_group2_sneakerzone.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.util.Random;

public class AddUserActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etPhoneNumber, etAddress;
    private Button btnAddUser;
    private UserDBHelper userDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        btnAddUser = findViewById(R.id.btnAddUser);

        userDBHelper = UserDBHelper.getInstance(this);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String phoneNumber = etPhoneNumber.getText().toString();
                    String address = etAddress.getText().toString();

                    int randomId = new Random().nextInt(1000000);

                    User newUser = new User(randomId, name, email, password, phoneNumber, address, 1, true);
                    long result = userDBHelper.insertUser(newUser);

                    if (result > 0) {
                        Toast.makeText(AddUserActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);  // Signal that a new user was added
                        finish();
                    } else {
                        Toast.makeText(AddUserActivity.this, "Failed to add user.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Validation method to check if fields are empty
    private boolean validateInputs() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Name is required");
            return false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Email is required");
            return false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("Password is required");
            return false;
        }
        if (etPhoneNumber.getText().toString().trim().isEmpty()) {
            etPhoneNumber.setError("Phone number is required");
            return false;
        }
        if (etAddress.getText().toString().trim().isEmpty()) {
            etAddress.setError("Address is required");
            return false;
        }
        return true;
    }
}
