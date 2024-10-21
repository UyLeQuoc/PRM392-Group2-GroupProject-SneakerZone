package com.group2.prm392_group2_sneakerzone.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.BrandAdapter;
import com.group2.prm392_group2_sneakerzone.adapter.UserAdapter;
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.model.User;
import com.group2.prm392_group2_sneakerzone.utils.BrandDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class BrandManagementPage extends AppCompatActivity implements BrandAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private BrandAdapter brandAdapter;
    private List<Brand> brandList;
    private BrandDBHelper dbHelper;
    private Button btnAddBrand;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_management);

        dbHelper = BrandDBHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        btnAddBrand = findViewById(R.id.btnAddBrand);

        loadBrandsFromDatabase();

        btnAddBrand.setOnClickListener(view -> showAddEditDialog(null, -1));
    }

    private void loadBrandsFromDatabase() {
        brandList = dbHelper.getAllBrands();
        brandAdapter = new BrandAdapter(this, brandList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(brandAdapter);
    }

    @Override
    public void onEditClick(int position) {
        Brand brand = brandList.get(position);
        showAddEditDialog(brand, position);
    }

    @Override
    public void onDeleteClick(int position) {
        Brand brand = brandList.get(position);
        dbHelper.deleteBrand(brand.getBrandId());
        brandList.remove(position);
        brandAdapter.notifyDataSetChanged();
    }

    private void showAddEditDialog(Brand brand, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_edit_brand, null);
        builder.setView(dialogView);

        EditText etBrandId = dialogView.findViewById(R.id.etBrandId);
        EditText etBrandName = dialogView.findViewById(R.id.etBrandName);
        EditText etCreatedBy = dialogView.findViewById(R.id.etBrandCreatedBy);
        EditText etBrandCreatedDate = dialogView.findViewById(R.id.etBrandCreatedDate);
        Button btnSave = dialogView.findViewById(R.id.btnBrandSave);

        if (brand != null) {
            etBrandId.setText(String.valueOf(brand.getBrandId()));
            etBrandName.setText(brand.getBrandName());
            etBrandCreatedDate.setText(brand.getCreatedDate());
            User createdByUser = getUserById(brand.getCreatedBy());
            if (createdByUser != null) {
                etCreatedBy.setText(createdByUser.getName()); // Display the user's name
            } else {
                etCreatedBy.setText("User not found");
            }
            etBrandId.setEnabled(false); // Disable editing ID for existing brands
            etCreatedBy.setEnabled(false); // Disable editing ID for existing brands
            etBrandCreatedDate.setEnabled(false); // Disable editing ID for existing brands
        } else {
            etBrandId.setVisibility(View.GONE); // Hide the ID field for new brands
            etCreatedBy.setText(getUserById(LoginActivity.userId).getName());
            etCreatedBy.setEnabled(false); // Disable editing ID for existing brands
            etBrandCreatedDate.setVisibility(View.GONE); // Hide the ID field for new brands
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(view -> {
            String brandName = etBrandName.getText().toString();


            if (position == -1) {
                // Add new brand
                Brand newBrand = new Brand(0, brandName,LoginActivity.userId , LocalDate.now().toString()); // ID is auto-increment
                dbHelper.addBrand(newBrand);
                loadBrandsFromDatabase(); // Reload data
                brandAdapter.notifyDataSetChanged();
            } else {
                // Update existing brand
                Brand updatedBrand = new Brand(brand.getBrandId(), brandName, brand.getCreatedBy(), brand.getCreatedDate());
                dbHelper.updateBrand(updatedBrand);
                loadBrandsFromDatabase(); // Reload data
                brandAdapter.notifyDataSetChanged();
            }

            dialog.dismiss();
        });

        dialog.show();
    }
    private User getUserById(int userId) {
        List<User> userList = UserDBHelper.getInstance(this).getAllUsers();
        for (User user : userList) {
            if (user.getUserId() == userId) {
                return user; // Return the user with matching userId
            }
        }
        return null; // Return null if no user is found with the matching userId
    }
}
