package com.group2.prm392_group2_sneakerzone.controller;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
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

public class AddStoreActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etStoreName, etStoreLocation;
    private ImageView ivStoreImage;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        etStoreName = findViewById(R.id.et_store_name);
        etStoreLocation = findViewById(R.id.et_store_location);
        ivStoreImage = findViewById(R.id.iv_store_image);

        Button btnChooseImage = findViewById(R.id.btn_choose_image);
        Button btnSaveStore = findViewById(R.id.btn_save_store);

        btnChooseImage.setOnClickListener(v -> openImagePicker());
        btnSaveStore.setOnClickListener(v -> saveStore());
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
            imagePath = getRealPathFromURI(selectedImageUri);
            ivStoreImage.setImageURI(selectedImageUri); // Display selected image
        }
    }

    // Get real path from URI for saving the image path as a string
    private String getRealPathFromURI(Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (uri.getAuthority().equals("com.android.providers.media.documents")) {
                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{documentId.split(":")[1]};
                path = getDataColumn(contentUri, selection, selectionArgs);
            }
        } else {
            path = getDataColumn(uri, null, null);
        }
        return path;
    }

    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        try (Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
        }
        return null;
    }

    private void saveStore() {
        String storeName = etStoreName.getText().toString().trim();
        String storeLocation = etStoreLocation.getText().toString().trim();

        if (storeName.isEmpty() || storeLocation.isEmpty() || imagePath == null) {
            Toast.makeText(this, "Please fill all fields and choose an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        Store store = new Store(0, storeName, imagePath, storeLocation, 1); // ownerId is set as 1 for example
        StoreDBHelper dbHelper = StoreDBHelper.getInstance(this);
        long result = dbHelper.addStore(store);

        Toast.makeText(this, "Store added successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Close the Add Store Activity
    }
}