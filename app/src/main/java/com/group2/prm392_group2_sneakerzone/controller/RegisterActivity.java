package com.group2.prm392_group2_sneakerzone.controller;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 101;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 102;

    private EditText registerName, registerEmail, registerPassword, et_regis_PhoneNumber, et_regis_Address;
    private Button btnRegister, btn_regis_SelectImage;
    private ImageView iv_regis_UserImage;
    private UserDBHelper userDBHelper;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        et_regis_PhoneNumber = findViewById(R.id.et_regis_PhoneNumber);
        et_regis_Address = findViewById(R.id.et_regis_Address);
        iv_regis_UserImage = findViewById(R.id.iv_regis_UserImage);
        btnRegister = findViewById(R.id.btnRegister);
        btn_regis_SelectImage = findViewById(R.id.btn_regis_SelectImage);

        userDBHelper = UserDBHelper.getInstance(this);

        // Request storage permission if needed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }

        btn_regis_SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the image picker
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    String name = registerName.getText().toString();
                    String email = registerEmail.getText().toString();
                    String password = registerPassword.getText().toString();
                    String phoneNumber = et_regis_PhoneNumber.getText().toString();
                    String address = et_regis_Address.getText().toString();

                    int randomId = new Random().nextInt(1000000);
                    String imageUri = selectedImageUri != null ? selectedImageUri.toString() : null;

                    User newUser = new User(randomId, name, email, password, phoneNumber, address, 4, true, imageUri);
                    long result = userDBHelper.insertUser(newUser);

                    if (result > 0) {
                        Toast.makeText(RegisterActivity.this, "Register account successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Kết thúc RegisterActivity
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to register.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean validateInputs() {
        if (registerName.getText().toString().trim().isEmpty()) {
            registerName.setError("Name is required");
            return false;
        }
        if (registerEmail.getText().toString().trim().isEmpty()) {
            registerEmail.setError("Email is required");
            return false;
        }
        if (registerPassword.getText().toString().trim().isEmpty()) {
            registerPassword.setError("Password is required");
            return false;
        }
        if (et_regis_PhoneNumber.getText().toString().trim().isEmpty()) {
            et_regis_PhoneNumber.setError("Phone number is required");
            return false;
        }
        if (et_regis_Address.getText().toString().trim().isEmpty()) {
            et_regis_Address.setError("Address is required");
            return false;
        }
        return true;
    }


    // Updated method to copy image to internal storage
    private String copyImageToInternalStorage(Uri imageUri) {
        try {
            ContentResolver contentResolver = getContentResolver();

            // Open the image as an InputStream using ContentResolver
            try (InputStream inputStream = contentResolver.openInputStream(imageUri)) {
                if (inputStream == null) {
                    return null;
                }

                // Create a file in the internal storage
                String fileName = "user_image_" + System.currentTimeMillis() + ".png";
                File file = new File(getFilesDir(), fileName);
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    // Copy the image data into the internal storage
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                // Return the path to the saved file
                return file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri originalUri = data.getData();  // Get the selected image URI

            // Copy the image to internal storage and get the path
            String internalImagePath = copyImageToInternalStorage(originalUri);

            // Update the selectedImageUri and display the image
            if (internalImagePath != null) {
                selectedImageUri = Uri.parse(internalImagePath);
                iv_regis_UserImage.setImageURI(selectedImageUri);  // Display the copied image
            }
        }
    }

}
