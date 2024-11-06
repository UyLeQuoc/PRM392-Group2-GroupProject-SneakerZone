package com.group2.prm392_group2_sneakerzone.controller;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.utils.BrandDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.ProductDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddProductActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 101;

    private EditText etProductName, etProductPrice, etProductDescription;
    private Spinner spinnerBrand, spinnerStore;
    private ImageView ivProductImage;
    private Button btnSelectImage, btnAddProduct;
    private ProductDBHelper productDBHelper;
    private Uri selectedImageUri;
    private int selectedBrandId = 1; // Default brand ID
    private int selectedStoreId = 1; // Default store ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        spinnerBrand = findViewById(R.id.spinnerBrand);
        spinnerStore = findViewById(R.id.spinnerStore);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAddProduct = findViewById(R.id.btnSave);

        productDBHelper = ProductDBHelper.getInstance(this);

        // Nhận Store ID từ Intent
        Intent intent = getIntent();
        selectedStoreId = intent.getIntExtra("StoreId", -1);

        loadBrands();
        loadStores();

        // Chọn hình ảnh
        btnSelectImage.setOnClickListener(view -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
        });

        // Thêm sản phẩm
        btnAddProduct.setOnClickListener(view -> {
            String name = etProductName.getText().toString();
            String description = etProductDescription.getText().toString();
            double price = Double.parseDouble(etProductPrice.getText().toString());

            int randomId = new Random().nextInt(1000000);
            String imageUri = selectedImageUri != null ? selectedImageUri.toString() : "";

            Product newProduct = new Product(randomId, name, imageUri, selectedBrandId, selectedStoreId, price, description, "2024-01-01", "2024-01-01");
            long result = productDBHelper.addProduct(newProduct);

            if (result > 0) {
                Toast.makeText(AddProductActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(AddProductActivity.this, "Failed to add product.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBrands() {
        BrandDBHelper brandDBHelper = BrandDBHelper.getInstance(this);
        List<Brand> brands = brandDBHelper.getAllBrands();
        List<String> brandNames = new ArrayList<>();

        for (Brand brand : brands) {
            brandNames.add(brand.getBrandName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brandNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrand.setAdapter(adapter);

        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBrandId = brands.get(position).getBrandId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadStores() {
        StoreDBHelper storeDBHelper = StoreDBHelper.getInstance(this);
        List<String> storeNames = new ArrayList<>();

        // Nếu Store ID được truyền vào, chỉ hiển thị Store này
        if (selectedStoreId != -1) {
            Store store = storeDBHelper.getStoreById(selectedStoreId);
            if (store != null) {
                storeNames.add(store.getStoreName());
                spinnerStore.setEnabled(false); // Vô hiệu hóa Spinner nếu đã có Store ID
            }
        } else {
            List<Store> stores = storeDBHelper.getAllStores();
            for (Store store : stores) {
                storeNames.add(store.getStoreName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, storeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStore.setAdapter(adapter);

        spinnerStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedStoreId == -1) { // Chỉ cập nhật nếu không có Store ID cố định
                    StoreDBHelper dbHelper = StoreDBHelper.getInstance(AddProductActivity.this);
                    selectedStoreId = dbHelper.getAllStores().get(position).getStoreId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
                ivProductImage.setImageURI(selectedImageUri);
            }
        }
    }

    private String copyImageToInternalStorage(Uri imageUri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            if (inputStream == null) return null;

            String fileName = "product_image_" + System.currentTimeMillis() + ".png";
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
