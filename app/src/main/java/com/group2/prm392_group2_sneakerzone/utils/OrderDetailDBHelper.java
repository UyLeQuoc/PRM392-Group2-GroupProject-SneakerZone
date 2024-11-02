package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = InitialDb.DATABASE_VERSION;;

    // Singleton instance
    private static OrderDetailDBHelper instance;

    // Tên bảng và các cột của bảng OrderDetails
    private static final String TABLE_ORDER_DETAILS = "OrderDetails";
    private static final String COLUMN_ORDER_DETAIL_ID = "OrderDetailId";
    private static final String COLUMN_ORDER_ID = "OrderId";
    private static final String COLUMN_PRODUCT_SIZE_ID = "ProductSizeId";
    private static final String COLUMN_QUANTITY = "Quantity";
    private static final String COLUMN_UNIT_PRICE = "UnitPrice";

    // Singleton getInstance method
    public static synchronized OrderDetailDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new OrderDetailDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private OrderDetailDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    // Thêm dữ liệu mẫu cho bảng OrderDetails
    private void addOrderDetailSeed(SQLiteDatabase db, int orderId, int productSizeId, int quantity, double unitPrice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderId);
        values.put(COLUMN_PRODUCT_SIZE_ID, productSizeId);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT_PRICE, unitPrice);

        db.insert(TABLE_ORDER_DETAILS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        onCreate(db);
    }

    // Thêm OrderDetail mới
    public void addOrderDetail(OrderDetail orderDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderDetail.getOrderId());
        values.put(COLUMN_PRODUCT_SIZE_ID, orderDetail.getProductSizeId());
        values.put(COLUMN_QUANTITY, orderDetail.getQuantity());
        values.put(COLUMN_UNIT_PRICE, orderDetail.getUnitPrice());

        db.insert(TABLE_ORDER_DETAILS, null, values);
        db.close();
    }

    // Lấy tất cả OrderDetails
    public List<OrderDetail> getAllOrderDetails() {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderDetail orderDetail = new OrderDetail(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DETAIL_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_SIZE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNIT_PRICE))
                );
                orderDetailList.add(orderDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderDetailList;
    }

    // Lấy OrderDetail theo ID
    public OrderDetail getOrderDetailById(int orderDetailId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ORDER_DETAILS + " WHERE " + COLUMN_ORDER_DETAIL_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderDetailId)});

        if (cursor.moveToFirst()) {
            OrderDetail orderDetail = new OrderDetail(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DETAIL_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_SIZE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNIT_PRICE))
            );
            cursor.close();
            return orderDetail;
        }
        cursor.close();
        return null;
    }

    // Cập nhật OrderDetail
    public int updateOrderDetail(OrderDetail orderDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderDetail.getOrderId());
        values.put(COLUMN_PRODUCT_SIZE_ID, orderDetail.getProductSizeId());
        values.put(COLUMN_QUANTITY, orderDetail.getQuantity());
        values.put(COLUMN_UNIT_PRICE, orderDetail.getUnitPrice());

        return db.update(TABLE_ORDER_DETAILS, values, COLUMN_ORDER_DETAIL_ID + " = ?", new String[]{String.valueOf(orderDetail.getOrderDetailId())});
    }

    // Xóa OrderDetail
    public void deleteOrderDetail(int orderDetailId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER_DETAILS, COLUMN_ORDER_DETAIL_ID + " = ?", new String[]{String.valueOf(orderDetailId)});
        db.close();
    }
    // In OrderDetailDBHelper class
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_DETAILS + " WHERE " + COLUMN_ORDER_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                OrderDetail orderDetail = new OrderDetail(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DETAIL_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_SIZE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNIT_PRICE))
                );
                orderDetailList.add(orderDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderDetailList;
    }

}
