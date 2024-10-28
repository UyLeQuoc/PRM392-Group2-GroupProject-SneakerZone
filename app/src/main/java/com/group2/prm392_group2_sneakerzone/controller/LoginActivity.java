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
import com.group2.prm392_group2_sneakerzone.utils.InitialDb;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private UserDBHelper userDBHelper;
    public static int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitialDb db = InitialDb.getInstance(this);

        // Khởi tạo UserDBHelper sử dụng Singleton
        userDBHelper = UserDBHelper.getInstance(this);

        // Ánh xạ các thành phần trong layout
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Xử lý khi nhấn nút Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // Kiểm tra email và password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy thông tin người dùng từ DB dựa trên email và password
                User user = userDBHelper.getUserByEmailAndPassword(email, password);
                if (user != null) {
                    userId = user.getUserId();
                    // Chuyển hướng dựa trên vai trò của người dùng
                    switch (user.getRole()) {
                        case 1: // Admin
                            Intent adminIntent = new Intent(LoginActivity.this, AdminHomePage.class);
                            startActivity(adminIntent);
                            break;
                        case 2: // Store Owner
                            Intent storeOwnerIntent = new Intent(LoginActivity.this, StoreOwnerHomePage.class);
                            startActivity(storeOwnerIntent);
                            break;
                        case 3: // Manager
                            Intent managerIntent = new Intent(LoginActivity.this, ManagerHomePage.class);
                            startActivity(managerIntent);
                            break;
                        case 4: // Customer
                            Intent customerIntent = new Intent(LoginActivity.this, CustomerHomePage.class);
                            startActivity(customerIntent);
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "Role not recognized", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
