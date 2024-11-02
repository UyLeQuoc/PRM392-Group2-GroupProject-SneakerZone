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

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class AddShopOwnerActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 101;

    private EditText etName, etEmail, etPassword, etPhoneNumber, etAddress;
    private ImageView ivUserImage;
    private Button btnAddUser, btnSelectImage;
    private UserDBHelper userDBHelper;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user); // Có thể dùng lại layout của AddUserActivity

        // Ánh xạ các thành phần giao diện
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        ivUserImage = findViewById(R.id.ivUserImage);
        btnAddUser = findViewById(R.id.btnAddUser);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        userDBHelper = UserDBHelper.getInstance(this);

        // Xử lý sự kiện chọn ảnh
        btnSelectImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        // Thêm người dùng với vai trò mặc định là Shop Owner (role = 2)
        btnAddUser.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String address = etAddress.getText().toString();

            // Đặt role mặc định là 2 (Shop Owner)
            int role = 2;
            String imageUri = selectedImageUri != null ? selectedImageUri.toString() : "";

            User newUser = new User(new Random().nextInt(1000000), name, email, password, phoneNumber, address, role, true, imageUri);
            long result = userDBHelper.insertUser(newUser);

            if (result > 0) {
                Toast.makeText(AddShopOwnerActivity.this, "Shop Owner added successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(AddShopOwnerActivity.this, "Failed to add Shop Owner.", Toast.LENGTH_SHORT).show();
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
