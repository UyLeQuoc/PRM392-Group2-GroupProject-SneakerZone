package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = InitialDb.DATABASE_VERSION;

    // Singleton instance
    private static OrderDBHelper instance;

    // Tên bảng và các cột của bảng Orders
    private static final String TABLE_ORDERS = "Orders";
    private static final String COLUMN_ORDER_ID = "OrderId";
    private static final String COLUMN_CUSTOMER_ID = "CustomerId";
    private static final String COLUMN_STORE_ID = "StoreId";
    private static final String COLUMN_TOTAL_AMOUNT = "TotalAmount";
    private static final String COLUMN_ORDER_DATE = "OrderDate";
    private static final String COLUMN_ORDER_STATUS = "OrderStatus";

    // Singleton getInstance method
    public static synchronized OrderDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new OrderDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private OrderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    // Thêm dữ liệu mẫu cho bảng Orders
    private void addOrderSeed(SQLiteDatabase db, int customerId, int storeId, double totalAmount, String orderDate, String orderStatus) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_ID, customerId);
        values.put(COLUMN_STORE_ID, storeId);
        values.put(COLUMN_TOTAL_AMOUNT, totalAmount);
        values.put(COLUMN_ORDER_DATE, orderDate);
        values.put(COLUMN_ORDER_STATUS, orderStatus);

        db.insert(TABLE_ORDERS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    // Thêm Order mới
    public void addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_ID, order.getCustomerId());
        values.put(COLUMN_STORE_ID, order.getStoreId());
        values.put(COLUMN_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_ORDER_STATUS, order.getOrderStatus());

        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    // Add Order and Return ID
    public long addOrderAndGetId(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_ID, order.getCustomerId());
        values.put(COLUMN_STORE_ID, order.getStoreId());
        values.put(COLUMN_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_ORDER_STATUS, order.getOrderStatus());

        long orderId = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return orderId; // Return the auto-generated Order ID
    }

    // Lấy tất cả Orders
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    // Lấy Order theo ID
    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            Order order = new Order(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS))
            );
            cursor.close();
            return order;
        }
        cursor.close();
        return null;
    }

    // Cập nhật Order
    public int updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_ID, order.getCustomerId());
        values.put(COLUMN_STORE_ID, order.getStoreId());
        values.put(COLUMN_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_ORDER_DATE, order.getOrderDate());
        values.put(COLUMN_ORDER_STATUS, order.getOrderStatus());

        return db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(order.getOrderId())});
    }

    // Xóa Order
    public void deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
        db.close();
    }
}
