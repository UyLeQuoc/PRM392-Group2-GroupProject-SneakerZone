package com.group2.prm392_group2_sneakerzone.controller;

import android.content.ContentResolver;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.ProductSizeQuantityAdapter;
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.model.ProductSize;
import com.group2.prm392_group2_sneakerzone.model.Store;
import com.group2.prm392_group2_sneakerzone.utils.BrandDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.ProductDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.ProductSizeDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.StoreDBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 101;

    private EditText etProductName, etProductPrice, etProductDescription;
    private Spinner spinnerBrand, spinnerStore, spinnerProductSizes;
    private ImageView ivProductImage;
    private Button btnUpdateProduct, btnSelectImage, btnDeleteProduct, btnAddDetails;
    private ProductDBHelper productDBHelper;
    private ProductSizeDBHelper productSizeDBHelper;
    private int productId;
    private Uri selectedImageUri;
    private int selectedBrandId = 1;
    private int selectedStoreId = 1;
    private RecyclerView recyclerViewProductSizes;
    private ProductSizeQuantityAdapter productSizeQuantityAdapter;
    private List<ProductSize> productSizes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize views
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        spinnerBrand = findViewById(R.id.spinnerBrand);
        spinnerStore = findViewById(R.id.spinnerStore);
        spinnerProductSizes = findViewById(R.id.spinnerProductSizes);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnUpdateProduct = findViewById(R.id.btnUpdate);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnDeleteProduct = findViewById(R.id.btnDelete);
        recyclerViewProductSizes = findViewById(R.id.recyclerViewProductSizes);
        btnAddDetails = findViewById(R.id.btnAddDetails);

        productDBHelper = ProductDBHelper.getInstance(this);
        productSizeDBHelper = ProductSizeDBHelper.getInstance(this);
        Intent intent = getIntent();
        selectedStoreId = intent.getIntExtra("StoreId", -1);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        Product product = productDBHelper.getProductById(productId);
        if (product != null) {
            etProductName.setText(product.getProductName());
            etProductPrice.setText(String.valueOf(product.getPrice()));
            etProductDescription.setText(product.getDescription());
            selectedBrandId = product.getBrandId();
            selectedStoreId = product.getStoreId();

            if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
                selectedImageUri = Uri.parse(product.getProductImage());
                Glide.with(this).load(new File(product.getProductImage())).into(ivProductImage);
            }
        }

        loadBrands();
        loadStores();
        loadProductSizes();

        btnSelectImage.setOnClickListener(view -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
        });

        btnUpdateProduct.setOnClickListener(view -> updateProduct());

        btnDeleteProduct.setOnClickListener(view -> confirmDeleteProduct());

        btnAddDetails.setOnClickListener(view -> {
            Intent detailsIntent = new Intent(EditProductActivity.this, ProductSizeDetailsActivity.class);
            detailsIntent.putExtra("PRODUCT_ID", productId);
            startActivity(detailsIntent);
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

        if (selectedStoreId != -1) {
            Store store = storeDBHelper.getStoreById(selectedStoreId);
            if (store != null) {
                storeNames.add(store.getStoreName());
                spinnerStore.setEnabled(false); // Disable Spinner if Store ID exists
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
                if (selectedStoreId == -1) {
                    selectedStoreId = storeDBHelper.getAllStores().get(position).getStoreId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadProductSizes() {
        productSizes = productSizeDBHelper.getSizesByProductId(productId);

        List<String> sizeOptions = new ArrayList<>();
        for (ProductSize productSize : productSizes) {
            sizeOptions.add(productSize.getSize());
        }

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeOptions);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductSizes.setAdapter(sizeAdapter);

        productSizeQuantityAdapter = new ProductSizeQuantityAdapter(new ArrayList<>());
        recyclerViewProductSizes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductSizes.setAdapter(productSizeQuantityAdapter);

        spinnerProductSizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSize = sizeOptions.get(position);
                List<ProductSize> filteredSizes = filterSizesBySize(selectedSize);
                productSizeQuantityAdapter.updateData(filteredSizes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private List<ProductSize> filterSizesBySize(String size) {
        List<ProductSize> filteredSizes = new ArrayList<>();
        for (ProductSize productSize : productSizes) {
            if (productSize.getSize().equals(size)) {
                filteredSizes.add(productSize);
            }
        }
        return filteredSizes;
    }

    private void updateProduct() {
        String name = etProductName.getText().toString();
        String description = etProductDescription.getText().toString();
        double price = Double.parseDouble(etProductPrice.getText().toString());
        String imageUri = selectedImageUri != null ? selectedImageUri.toString() : "";

        Product updatedProduct = new Product(productId, name, imageUri, selectedBrandId, selectedStoreId, price, description, "2024-01-01", "2024-01-01");
        int result = productDBHelper.updateProduct(updatedProduct);

        if (result > 0) {
            Toast.makeText(EditProductActivity.this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(EditProductActivity.this, "Failed to update product.", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDeleteProduct() {
        new AlertDialog.Builder(EditProductActivity.this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    productDBHelper.deleteProduct(productId);
                    Toast.makeText(EditProductActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
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
