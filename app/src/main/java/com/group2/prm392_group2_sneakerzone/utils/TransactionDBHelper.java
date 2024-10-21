package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = InitialDb.DATABASE_VERSION;;

    // Singleton instance
    private static TransactionDBHelper instance;

    // Tên bảng và các cột của bảng Transactions
    private static final String TABLE_TRANSACTIONS = "Transactions";
    private static final String COLUMN_TRANSACTION_ID = "TransactionId";
    private static final String COLUMN_ORDER_ID = "OrderId";
    private static final String COLUMN_PAYMENT_METHOD = "PaymentMethod";
    private static final String COLUMN_PAYMENT_DATE = "PaymentDate";
    private static final String COLUMN_PAYMENT_STATUS = "PaymentStatus";

    // Singleton getInstance method
    public static synchronized TransactionDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TransactionDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private TransactionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    // Thêm dữ liệu mẫu cho bảng Transactions
    private void addTransactionSeed(SQLiteDatabase db, int orderId, String paymentMethod, String paymentDate, String paymentStatus) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderId);
        values.put(COLUMN_PAYMENT_METHOD, paymentMethod);
        values.put(COLUMN_PAYMENT_DATE, paymentDate);
        values.put(COLUMN_PAYMENT_STATUS, paymentStatus);

        db.insert(TABLE_TRANSACTIONS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // Thêm Transaction mới
    public void addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, transaction.getOrderId());
        values.put(COLUMN_PAYMENT_METHOD, transaction.getPaymentMethod());
        values.put(COLUMN_PAYMENT_DATE, transaction.getPaymentDate());
        values.put(COLUMN_PAYMENT_STATUS, transaction.getPaymentStatus());

        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    // Lấy tất cả Transactions
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRANSACTION_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_STATUS))
                );
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactionList;
    }

    // Lấy Transaction theo ID
    public Transaction getTransactionById(int transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TRANSACTION_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(transactionId)});

        if (cursor.moveToFirst()) {
            Transaction transaction = new Transaction(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRANSACTION_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_STATUS))
            );
            cursor.close();
            return transaction;
        }
        cursor.close();
        return null;
    }

    // Cập nhật Transaction
    public int updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, transaction.getOrderId());
        values.put(COLUMN_PAYMENT_METHOD, transaction.getPaymentMethod());
        values.put(COLUMN_PAYMENT_DATE, transaction.getPaymentDate());
        values.put(COLUMN_PAYMENT_STATUS, transaction.getPaymentStatus());

        return db.update(TABLE_TRANSACTIONS, values, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(transaction.getTransactionId())});
    }

    // Xóa Transaction
    public void deleteTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(transactionId)});
        db.close();
    }
}

