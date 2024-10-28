package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InitialDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SneakerZoneDB";
    public static int DATABASE_VERSION = 7;

    // Common columns
    private static final String COLUMN_CREATED_BY = "CreatedBy";
    private static final String COLUMN_CREATED_DATE = "CreatedDate";
    private static final String COLUMN_UPDATED_DATE = "UpdatedDate";

    // Users table and columns
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "UserId";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    private static final String COLUMN_ADDRESS = "Address";
    private static final String COLUMN_IS_ACTIVE = "IsActive";
    private static final String COLUMN_ROLE = "Role";
    private static final String COLUMN_USER_IMAGE = "UserImage";

    // Orders table and columns
    private static final String TABLE_ORDERS = "Orders";
    private static final String COLUMN_ORDER_ID = "OrderId";
    private static final String COLUMN_CUSTOMER_ID = "CustomerId";
    private static final String COLUMN_TOTAL_AMOUNT = "TotalAmount";
    private static final String COLUMN_ORDER_DATE = "OrderDate";
    private static final String COLUMN_PAYMENT_STATUS = "PaymentStatus";

    // OrderDetails table and columns
    private static final String TABLE_ORDER_DETAILS = "OrderDetails";
    private static final String COLUMN_ORDER_DETAIL_ID = "OrderDetailId";
    private static final String COLUMN_PRODUCT_SIZE_ID = "ProductSizeId";
    private static final String COLUMN_QUANTITY = "Quantity";
    private static final String COLUMN_UNIT_PRICE = "UnitPrice";

    // Transactions table and columns
    private static final String TABLE_TRANSACTIONS = "Transactions";
    private static final String COLUMN_TRANSACTION_ID = "TransactionId";
    private static final String COLUMN_PAYMENT_METHOD = "PaymentMethod";
    private static final String COLUMN_PAYMENT_DATE = "PaymentDate";

    // Brands table and columns
    private static final String TABLE_BRANDS = "Brands";
    private static final String COLUMN_BRAND_ID = "BrandId";
    private static final String COLUMN_BRAND_NAME = "BrandName";

    // Stores table and columns
    private static final String TABLE_STORES = "Stores";
    private static final String COLUMN_STORE_ID = "StoreId";
    private static final String COLUMN_STORE_NAME = "StoreName";
    private static final String COLUMN_STORE_IMAGE = "StoreImage";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_OWNER_ID = "OwnerId";

    // Products table and columns
    private static final String TABLE_PRODUCTS = "Products";
    private static final String COLUMN_PRODUCT_ID = "ProductId";
    private static final String COLUMN_PRODUCT_NAME = "ProductName";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_PRICE = "Price";

    // Singleton instance
    private static InitialDb instance;

    // Private constructor
    private InitialDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton getInstance method
    public static synchronized InitialDb getInstance(Context context) {
        if (instance == null) {
            instance = new InitialDb(context.getApplicationContext());
            instance.getReadableDatabase();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_IS_ACTIVE + " BOOLEAN, " +
                COLUMN_ROLE + " INTEGER, " +
                COLUMN_USER_IMAGE + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Brands table
        String CREATE_BRANDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BRANDS + " (" +
                COLUMN_BRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BRAND_NAME + " TEXT, " +
                COLUMN_CREATED_BY + " INTEGER, " +
                COLUMN_CREATED_DATE + " TEXT)";
        db.execSQL(CREATE_BRANDS_TABLE);

        // Orders table
        String CREATE_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CUSTOMER_ID + " INTEGER, " +
                COLUMN_STORE_ID + " INTEGER, " +
                COLUMN_TOTAL_AMOUNT + " REAL, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_PAYMENT_STATUS + " TEXT)";
        db.execSQL(CREATE_ORDERS_TABLE);

        // OrderDetails table
        String CREATE_ORDER_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_DETAILS + " (" +
                COLUMN_ORDER_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_ID + " INTEGER, " +
                COLUMN_PRODUCT_SIZE_ID + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_UNIT_PRICE + " REAL)";
        db.execSQL(CREATE_ORDER_DETAILS_TABLE);

        // Transactions table
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + " (" +
                COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_ID + " INTEGER, " +
                COLUMN_PAYMENT_METHOD + " TEXT, " +
                COLUMN_PAYMENT_DATE + " TEXT, " +
                COLUMN_PAYMENT_STATUS + " TEXT)";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);

        // Stores table
        String CREATE_STORES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STORES + " (" +
                COLUMN_STORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STORE_NAME + " TEXT NOT NULL, " +
                COLUMN_STORE_IMAGE + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_OWNER_ID + " INTEGER, " +
                COLUMN_CREATED_DATE + " TEXT NOT NULL, " +
                COLUMN_UPDATED_DATE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_OWNER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_STORES_TABLE);

        // Products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                COLUMN_BRAND_ID + " INTEGER, " +
                COLUMN_STORE_ID + " INTEGER, " +
                COLUMN_PRICE + " REAL NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CREATED_DATE + " TEXT NOT NULL, " +
                COLUMN_UPDATED_DATE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_BRAND_ID + ") REFERENCES " + TABLE_BRANDS + "(" + COLUMN_BRAND_ID + "), " +
                "FOREIGN KEY (" + COLUMN_STORE_ID + ") REFERENCES " + TABLE_STORES + "(" + COLUMN_STORE_ID + "))";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Seed initial data
        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        addUserSeed(db, "Admin", "admin@gmail.com", "123456", "0123456789", "Admin Address", 1, true, "admin.png");
        addUserSeed(db, "Store Owner", "storeowner@gmail.com", "123456", "0123456789", "Store Owner Address", 2, true, "owner.png");
        addUserSeed(db, "Manager", "manager@gmail.com", "123456", "0123456789", "Manager Address", 3, true, "manager.png");
        addUserSeed(db, "Customer", "customer@gmail.com", "123456", "0123456789", "Customer Address", 4, true, "customer.png");

        addBrandSeed(db, "Nike", 1, "2024-01-01");
        addBrandSeed(db, "Adidas", 1, "2024-01-01");
        addBrandSeed(db, "Puma", 2, "2024-01-01");

        addOrderSeed(db, 1, 1, 250.50, "2024-01-01", "Paid");
        addOrderSeed(db, 2, 1, 450.00, "2024-01-02", "Pending");

        addOrderDetailSeed(db, 1, 1, 2, 150.00);
        addOrderDetailSeed(db, 2, 2, 1, 300.00);

        addTransactionSeed(db, 1, "CreditCard", "2024-01-01", "Success");
        addTransactionSeed(db, 2, "PayPal", "2024-01-02", "Pending");
        addTransactionSeed(db, 3, "BankTransfer", "2024-01-03", "Failed");

        addStoreSeed(db, "Sneaker Central", "sneaker_central.png", "123 Main Street", 2, "2024-01-01", "2024-01-01");
        addStoreSeed(db, "Kickz Hub", "kickz_hub.png", "456 Broadway", 3, "2024-01-01", "2024-01-01");
    }

    private void addUserSeed(SQLiteDatabase db, String name, String email, String password, String phone, String address, int role, boolean isActive, String userImage) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE_NUMBER, phone);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_IS_ACTIVE, isActive ? 1 : 0);
        values.put(COLUMN_USER_IMAGE, userImage);
        db.insert(TABLE_USERS, null, values);
    }

    private void addBrandSeed(SQLiteDatabase db, String brandName, int createdBy, String createdDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRAND_NAME, brandName);
        values.put(COLUMN_CREATED_BY, createdBy);
        values.put(COLUMN_CREATED_DATE, createdDate);
        db.insert(TABLE_BRANDS, null, values);
    }

    private void addOrderSeed(SQLiteDatabase db, int customerId, int storeId, double totalAmount, String orderDate, String paymentStatus) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_ID, customerId);
        values.put(COLUMN_STORE_ID, storeId);
        values.put(COLUMN_TOTAL_AMOUNT, totalAmount);
        values.put(COLUMN_ORDER_DATE, orderDate);
        values.put(COLUMN_PAYMENT_STATUS, paymentStatus);
        db.insert(TABLE_ORDERS, null, values);
    }

    private void addOrderDetailSeed(SQLiteDatabase db, int orderId, int productSizeId, int quantity, double unitPrice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderId);
        values.put(COLUMN_PRODUCT_SIZE_ID, productSizeId);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT_PRICE, unitPrice);
        db.insert(TABLE_ORDER_DETAILS, null, values);
    }

    private void addTransactionSeed(SQLiteDatabase db, int orderId, String paymentMethod, String paymentDate, String paymentStatus) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderId);
        values.put(COLUMN_PAYMENT_METHOD, paymentMethod);
        values.put(COLUMN_PAYMENT_DATE, paymentDate);
        values.put(COLUMN_PAYMENT_STATUS, paymentStatus);
        db.insert(TABLE_TRANSACTIONS, null, values);
    }

    private void addStoreSeed(SQLiteDatabase db, String storeName, String storeImage, String location, int ownerId, String createdDate, String updatedDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STORE_NAME, storeName);
        values.put(COLUMN_STORE_IMAGE, storeImage);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_OWNER_ID, ownerId);
        values.put(COLUMN_CREATED_DATE, createdDate);
        values.put(COLUMN_UPDATED_DATE, updatedDate);
        db.insert(TABLE_STORES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
        onCreate(db);
    }
}
