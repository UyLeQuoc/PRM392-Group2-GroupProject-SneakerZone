package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = InitialDb.DATABASE_VERSION;
    // Table and columns for Products
    private static final String TABLE_PRODUCTS = "Products";
    private static final String COLUMN_PRODUCT_ID = "ProductId";
    private static final String COLUMN_PRODUCT_NAME = "ProductName";
    private static final String COLUMN_PRODUCT_IMAGE = "ProductImage";
    private static final String COLUMN_BRAND_ID = "BrandId";
    private static final String COLUMN_STORE_ID = "StoreId";
    private static final String COLUMN_PRICE = "Price";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_CREATED_DATE = "CreatedDate";
    private static final String COLUMN_UPDATED_DATE = "UpdatedDate";
    // Singleton instance
    private static ProductDBHelper instance;

    // Private constructor
    private ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton getInstance method
    public static synchronized ProductDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ProductDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Add table creation code if needed
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Add new Product
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_PRODUCT_IMAGE, product.getProductImage());  // New field
        values.put(COLUMN_BRAND_ID, product.getBrandId());
        values.put(COLUMN_STORE_ID, product.getStoreId());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_CREATED_DATE, product.getCreatedDate());
        values.put(COLUMN_UPDATED_DATE, product.getUpdatedDate());

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Retrieve all Products
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE)),  // New field
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BRAND_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_DATE))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // Retrieve Product by ID
    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE)),  // New field
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BRAND_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_DATE))
            );
            cursor.close();
            return product;
        }
        cursor.close();
        return null;
    }

    // Update Product
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_PRODUCT_IMAGE, product.getProductImage());  // New field
        values.put(COLUMN_BRAND_ID, product.getBrandId());
        values.put(COLUMN_STORE_ID, product.getStoreId());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_CREATED_DATE, product.getCreatedDate());
        values.put(COLUMN_UPDATED_DATE, product.getUpdatedDate());

        return db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getProductId())});
    }

    // Delete Product
    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }
}
