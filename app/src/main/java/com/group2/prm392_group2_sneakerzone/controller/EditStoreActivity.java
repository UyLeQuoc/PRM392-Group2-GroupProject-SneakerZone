package com.group2.prm392_group2_sneakerzone.controller;

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
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditStoreActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etStoreName, etStoreLocation;
    private ImageView ivStoreImage;
    private Button btnChooseImage, btnUpdateStore;
    private String imagePath, createdDate;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);

        etStoreName = findViewById(R.id.et_store_name);
        etStoreLocation = findViewById(R.id.et_store_location);
        ivStoreImage = findViewById(R.id.iv_store_image);
        btnChooseImage = findViewById(R.id.btn_choose_image);
        btnUpdateStore = findViewById(R.id.btn_update_store);

        // Get data from intent
        Intent intent = getIntent();
        storeId = intent.getIntExtra("store_id", -1);
        String storeName = intent.getStringExtra("store_name");
        String storeLocation = intent.getStringExtra("store_location");
        imagePath = intent.getStringExtra("store_image");
        createdDate = intent.getStringExtra("created_date"); // Retrieve original created date

        // Populate fields with existing store data
        etStoreName.setText(storeName);
        etStoreLocation.setText(storeLocation);
        if (imagePath != null) {
            ivStoreImage.setImageURI(Uri.fromFile(new File(imagePath)));
        }

        btnChooseImage.setOnClickListener(v -> openImagePicker());
        btnUpdateStore.setOnClickListener(v -> updateStore());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imagePath = selectedImageUri.getPath();
            ivStoreImage.setImageURI(selectedImageUri);
        }
    }

    private void updateStore() {
        String storeName = etStoreName.getText().toString().trim();
        String storeLocation = etStoreLocation.getText().toString().trim();

        if (storeName.isEmpty() || storeLocation.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set updated date to current date
        String updatedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Update store in the database
        Store store = new Store(storeId, storeName, imagePath, storeLocation, 1, updatedDate, updatedDate);
        StoreDBHelper dbHelper = StoreDBHelper.getInstance(this);

        dbHelper.updateStore(store);

        Toast.makeText(this, "Store updated successfully!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK); // Signal successful update to calling activity
        finish(); // Close the Edit Store Activity
    }
}
