package com.group2.prm392_group2_sneakerzone.controller;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class UpdateShopOwnerActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 101;

    private EditText etName, etEmail, etPassword, etPhoneNumber, etAddress;
    private ImageView ivUserImage;
    private Button btnUpdateUser, btnSelectImage;
    private UserDBHelper userDBHelper;
    private int userId;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user); // Sử dụng lại layout của EditUserActivity

        // Ánh xạ các thành phần giao diện
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        ivUserImage = findViewById(R.id.ivUserImage);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        userDBHelper = UserDBHelper.getInstance(this);

        // Lấy thông tin Shop Owner từ Intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        User user = userDBHelper.getUserById(userId);
        if (user != null) {
            etName.setText(user.getName());
            etEmail.setText(user.getEmail());
            etPassword.setText(user.getPassword());
            etPhoneNumber.setText(user.getPhoneNumber());
            etAddress.setText(user.getAddress());

            if (user.getUserImage() != null && !user.getUserImage().isEmpty()) {
                selectedImageUri = Uri.parse(user.getUserImage());
                Glide.with(this).load(new File(user.getUserImage())).into(ivUserImage);
            }
        }

        // Sự kiện chọn ảnh
        btnSelectImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        // Cập nhật thông tin Shop Owner
        btnUpdateUser.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String address = etAddress.getText().toString();

            int role = 2; // Vai trò cố định là Shop Owner
            String imageUri = selectedImageUri != null ? selectedImageUri.toString() : "";

            User updatedUser = new User(userId, name, email, password, phoneNumber, address, role, true, imageUri);
            int result = userDBHelper.updateUser(updatedUser);

            if (result > 0) {
                Toast.makeText(UpdateShopOwnerActivity.this, "Shop Owner updated successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(UpdateShopOwnerActivity.this, "Failed to update Shop Owner.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri originalUri = data.getData();
            String internalImagePath = copyImageToInternalStorage(originalUri);

            if (internalImagePath != null) {
                selectedImageUri = Uri.parse(internalImagePath);
                ivUserImage.setImageURI(selectedImageUri);
            }
        }
    }

    private String copyImageToInternalStorage(Uri imageUri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            if (inputStream == null) return null;

            String fileName = "user_image_" + System.currentTimeMillis() + ".png";
            File file = new File(getFilesDir(), fileName);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
