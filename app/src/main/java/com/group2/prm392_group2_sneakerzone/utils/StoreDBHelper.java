package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.Store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = InitialDb.DATABASE_VERSION;

    // Singleton instance
    private static StoreDBHelper instance;

    // Table and columns for Stores
    private static final String TABLE_STORES = "Stores";
    private static final String COLUMN_STORE_ID = "StoreId";
    private static final String COLUMN_STORE_NAME = "StoreName";
    private static final String COLUMN_STORE_IMAGE = "StoreImage";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_OWNER_ID = "OwnerId";
    private static final String COLUMN_CREATED_AT = "CreatedDate";

    // Singleton getInstance method
    public static synchronized StoreDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new StoreDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private StoreDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Define create table SQL query for stores
        String CREATE_STORES_TABLE = "CREATE TABLE " + TABLE_STORES + " ("
                + COLUMN_STORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_STORE_NAME + " TEXT, "
                + COLUMN_STORE_IMAGE + " TEXT, "
                + COLUMN_LOCATION + " TEXT, "
                + COLUMN_OWNER_ID + " INTEGER)";
        db.execSQL(CREATE_STORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
        onCreate(db);
    }

    // Add new Store
    public long addStore(Store store) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STORE_NAME, store.getStoreName());
        values.put(COLUMN_STORE_IMAGE, store.getStoreImage());
        values.put(COLUMN_LOCATION, store.getLocation());
        values.put(COLUMN_OWNER_ID, store.getOwnerId());
        values.put(COLUMN_CREATED_AT, new Date().getTime());

        long result = db.insert(TABLE_STORES, null, values);
        db.close();
        return result;
    }

    // Get all stores
    public List<Store> getAllStores() {
        List<Store> storeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Store store = new Store(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORE_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OWNER_ID))
                );
                storeList.add(store);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeList;
    }

    // Get store by ID
    public Store getStoreById(int storeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STORES + " WHERE " + COLUMN_STORE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(storeId)});

        if (cursor.moveToFirst()) {
            Store store = new Store(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORE_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OWNER_ID))
            );
            cursor.close();
            return store;
        }
        cursor.close();
        return null;
    }

    // Update store
    public int updateStore(Store store) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STORE_NAME, store.getStoreName());
        values.put(COLUMN_STORE_IMAGE, store.getStoreImage());
        values.put(COLUMN_LOCATION, store.getLocation());
        values.put(COLUMN_OWNER_ID, store.getOwnerId());

        return db.update(TABLE_STORES, values, COLUMN_STORE_ID + " = ?", new String[]{String.valueOf(store.getStoreId())});
    }

    // Delete store
    public void deleteStore(int storeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORES, COLUMN_STORE_ID + " = ?", new String[]{String.valueOf(storeId)});
        db.close();
    }
}
