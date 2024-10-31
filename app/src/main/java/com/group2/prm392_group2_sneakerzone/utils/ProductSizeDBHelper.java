package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.ProductSize;

import java.util.ArrayList;
import java.util.List;

public class ProductSizeDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = InitialDb.DATABASE_VERSION;

    // Singleton instance
    private static ProductSizeDBHelper instance;

    // Table and column names for ProductSizes
    private static final String TABLE_PRODUCT_SIZES = "ProductSizes";
    private static final String COLUMN_PRODUCT_SIZE_ID = "ProductSizeId";
    private static final String COLUMN_PRODUCT_ID = "ProductId";
    private static final String COLUMN_SIZE = "Size";
    private static final String COLUMN_QUANTITY = "Quantity";
    private static final String COLUMN_CREATED_DATE = "CreatedDate";
    private static final String COLUMN_UPDATED_DATE = "UpdatedDate";

    // Singleton getInstance method
    public static synchronized ProductSizeDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ProductSizeDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private ProductSizeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Define the SQL statement to create the ProductSizes table
        String CREATE_PRODUCT_SIZES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_SIZES + " ("
                + COLUMN_PRODUCT_SIZE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_ID + " INTEGER, "
                + COLUMN_SIZE + " TEXT, "
                + COLUMN_QUANTITY + " INTEGER, "
                + COLUMN_CREATED_DATE + " TEXT, "
                + COLUMN_UPDATED_DATE + " TEXT, "
                + "FOREIGN KEY (" + COLUMN_PRODUCT_ID + ") REFERENCES Products(ProductId))";
        db.execSQL(CREATE_PRODUCT_SIZES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_SIZES);
        onCreate(db);
    }

    // Method to add a new ProductSize
    public void addProductSize(ProductSize productSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID, productSize.getProductId());
        values.put(COLUMN_SIZE, productSize.getSize());
        values.put(COLUMN_QUANTITY, productSize.getQuantity());
        values.put(COLUMN_CREATED_DATE, productSize.getCreatedDate());
        values.put(COLUMN_UPDATED_DATE, productSize.getUpdatedDate());

        db.insert(TABLE_PRODUCT_SIZES, null, values);
        db.close();
    }

    // Method to retrieve all sizes for a specific product
    public List<ProductSize> getSizesByProductId(int productId) {
        List<ProductSize> productSizeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_SIZES + " WHERE " + COLUMN_PRODUCT_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            do {
                ProductSize productSize = new ProductSize(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_SIZE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_DATE))
                );
                productSizeList.add(productSize);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productSizeList;
    }

    // Method to get a specific ProductSize by its ID
    public ProductSize getProductSizeById(int productSizeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCT_SIZES + " WHERE " + COLUMN_PRODUCT_SIZE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productSizeId)});

        if (cursor.moveToFirst()) {
            ProductSize productSize = new ProductSize(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_SIZE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_DATE))
            );
            cursor.close();
            return productSize;
        }
        cursor.close();
        return null;
    }

    // Method to update a ProductSize
    public int updateProductSize(ProductSize productSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID, productSize.getProductId());
        values.put(COLUMN_SIZE, productSize.getSize());
        values.put(COLUMN_QUANTITY, productSize.getQuantity());
        values.put(COLUMN_CREATED_DATE, productSize.getCreatedDate());
        values.put(COLUMN_UPDATED_DATE, productSize.getUpdatedDate());

        return db.update(TABLE_PRODUCT_SIZES, values, COLUMN_PRODUCT_SIZE_ID + " = ?", new String[]{String.valueOf(productSize.getProductSizeId())});
    }

    // Method to delete a ProductSize
    public void deleteProductSize(int productSizeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT_SIZES, COLUMN_PRODUCT_SIZE_ID + " = ?", new String[]{String.valueOf(productSizeId)});
        db.close();
    }

    // Method to delete all sizes of a specific product
    public void deleteSizesByProductId(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT_SIZES, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public ProductSize getSizeByProductIdAndSize(int productId, String size) {
        SQLiteDatabase db = this.getReadableDatabase();
        ProductSize productSize = null;

        // Query to find the specific size for a given product
        String query = "SELECT * FROM " + TABLE_PRODUCT_SIZES + " WHERE "
                + COLUMN_PRODUCT_ID + " = ? AND "
                + COLUMN_SIZE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId), size});

        // If the size is found, create a ProductSize object with the retrieved data
        if (cursor.moveToFirst()) {
            productSize = new ProductSize(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_SIZE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_DATE))
            );
        }
        cursor.close();
        return productSize;
    }

}
