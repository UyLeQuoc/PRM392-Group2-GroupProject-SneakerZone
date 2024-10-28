package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InitialDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SneakerZoneDB";
    public static  int DATABASE_VERSION = 6;
    // Singleton instance
    private static InitialDb instance;

    // Cột chung
    private static final String COLUMN_CREATED_BY = "CreatedBy";
    private static final String COLUMN_CREATED_DATE = "CreatedDate";
    private static final String COLUMN_UPDATED_DATE = "UpdatedDate";


    // Tên bảng và các cột của bảng Users
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "UserId";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    private static final String COLUMN_ADDRESS = "Address";
    private static final String COLUMN_IS_ACTIVE = "IsActive";
    private static final String COLUMN_ROLE = "Role";

    // Tên bảng và các cột của bảng Orders
    private static final String TABLE_ORDERS = "Orders";
    private static final String COLUMN_ORDER_ID = "OrderId";
    private static final String COLUMN_CUSTOMER_ID = "CustomerId";
    private static final String COLUMN_TOTAL_AMOUNT = "TotalAmount";
    private static final String COLUMN_ORDER_DATE = "OrderDate";
    private static final String COLUMN_PAYMENT_STATUS = "PaymentStatus";

    // Tên bảng và các cột của bảng OrderDetails
    private static final String TABLE_ORDER_DETAILS = "OrderDetails";
    private static final String COLUMN_ORDER_DETAIL_ID = "OrderDetailId";
    private static final String COLUMN_PRODUCT_SIZE_ID = "ProductSizeId";
    private static final String COLUMN_QUANTITY = "Quantity";
    private static final String COLUMN_PRICE = "Price";

    // Tên bảng và các cột của bảng Transactions
    private static final String TABLE_TRANSACTIONS = "Transactions";
    private static final String COLUMN_TRANSACTION_ID = "TransactionId";
    private static final String COLUMN_PAYMENT_METHOD = "PaymentMethod";
    private static final String COLUMN_PAYMENT_DATE = "PaymentDate";

    // Tên bảng và các cột của bảng Brands
    private static final String TABLE_BRANDS = "Brands";
    private static final String COLUMN_BRAND_ID = "BrandId";
    private static final String COLUMN_BRAND_NAME = "BrandName";


    // Định nghĩa tên bảng và các cột của bảng Stores mà không trùng với các biến đã có
    private static final String TABLE_STORES = "Stores";
    private static final String COLUMN_STORE_ID = "StoreId";
    private static final String COLUMN_STORE_NAME = "StoreName";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_OWNER_ID = "OwnerId";

    // Tên bảng và các cột của bảng Products
    private static final String TABLE_PRODUCTS = "Products";
    private static final String COLUMN_PRODUCT_ID = "ProductId";
    private static final String COLUMN_PRODUCT_NAME = "ProductName";
    private static final String COLUMN_DESCRIPTION = "Description";

    // Singleton getInstance method
    public static synchronized InitialDb getInstance(Context context) {
        if (instance == null) {
            instance = new InitialDb(context.getApplicationContext());
            instance.getReadableDatabase();
        }
//        instance.seedData(instance.getWritableDatabase());
        return instance;
    }
    // Private constructor
    private InitialDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        String CREATE_USERS_TABLE = "CREATE TABLE if not exists " + TABLE_USERS + "  (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_IS_ACTIVE + " BOOLEAN, " +
                COLUMN_ROLE + " INTEGER)";
        db.execSQL(CREATE_USERS_TABLE);

        // Tạo bảng Brands
        String CREATE_BRANDS_TABLE = "CREATE TABLE if not exists " + TABLE_BRANDS + " (" +
                COLUMN_BRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BRAND_NAME + " TEXT, " +
                COLUMN_CREATED_BY + " INTEGER, " +
                COLUMN_CREATED_DATE + " TEXT)";
        db.execSQL(CREATE_BRANDS_TABLE);

        String CREATE_ORDERS_TABLE = "CREATE TABLE if not exists " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CUSTOMER_ID + " INTEGER, " +
                COLUMN_STORE_ID + " INTEGER, " +
                COLUMN_TOTAL_AMOUNT + " REAL, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_PAYMENT_STATUS + " TEXT)";
        db.execSQL(CREATE_ORDERS_TABLE);

        String CREATE_ORDER_DETAILS_TABLE = "CREATE TABLE if not exists " + TABLE_ORDER_DETAILS + " (" +
                COLUMN_ORDER_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_ID + " INTEGER, " +
                COLUMN_PRODUCT_SIZE_ID + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_PRICE + " REAL)";
        db.execSQL(CREATE_ORDER_DETAILS_TABLE);

        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE if not exists " + TABLE_TRANSACTIONS + " (" +
                COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_ID + " INTEGER, " +
                COLUMN_PAYMENT_METHOD + " TEXT, " +
                COLUMN_PAYMENT_DATE + " TEXT, " +
                COLUMN_PAYMENT_STATUS + " TEXT)";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);

        // Trong phương thức onCreate của lớp InitialDb, thêm mã sau để tạo bảng Stores:
        String CREATE_STORES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STORES + " (" +
                COLUMN_STORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STORE_NAME + " TEXT NOT NULL, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_OWNER_ID + " INTEGER, " +
                COLUMN_CREATED_DATE + " TEXT NOT NULL, " +  // đã có trong lớp này
                COLUMN_UPDATED_DATE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_OWNER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(CREATE_STORES_TABLE);

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                COLUMN_BRAND_ID + " INTEGER, " +  // đã có trong lớp này
                COLUMN_STORE_ID + " INTEGER, " +  // đã có trong lớp này
                COLUMN_PRICE + " REAL NOT NULL, " +  // đã có trong lớp này
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CREATED_DATE + " TEXT NOT NULL, " +  // đã có trong lớp này
                COLUMN_UPDATED_DATE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_BRAND_ID + ") REFERENCES " + TABLE_BRANDS + "(" + COLUMN_BRAND_ID + "), " +
                "FOREIGN KEY (" + COLUMN_STORE_ID + ") REFERENCES " + TABLE_STORES + "(" + COLUMN_STORE_ID + ")" +
                ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        addUserSeed(db, "Admin", "admin@gmail.com", "123456", "0123456789", "Admin Address", 1, true);
        addUserSeed(db, "Store Owner", "storeowner@gmail.com", "123456", "0123456789", "Store Owner Address", 2, true);
        addUserSeed(db, "Manager", "manager@gmail.com", "123456", "0123456789", "Manager Address", 3, true);
        addUserSeed(db, "Customer", "customer@gmail.com", "123456", "0123456789", "Customer Address", 4, true);

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

        // Seed data cho bảng Stores
        addStoreSeed(db, "Sneaker Central", "123 Main Street", 2, "2024-01-01", "2024-01-01");
        addStoreSeed(db, "Kickz Hub", "456 Broadway", 3, "2024-01-01", "2024-01-01");
        addStoreSeed(db, "Shoe Palace", "789 Elm St", 2, "2024-01-02", "2024-01-02");

        // Seed data cho bảng Products
        addProductSeed(db, "Nike Air Max", 1, 1, 120.00, "Comfortable running shoes", "2024-01-01", "2024-01-01");
        addProductSeed(db, "Adidas Ultra Boost", 2, 1, 140.00, "High-performance running shoes", "2024-01-01", "2024-01-01");
        addProductSeed(db, "Puma Classic", 3, 2, 90.00, "Stylish everyday sneakers", "2024-01-02", "2024-01-02");
        addProductSeed(db, "Nike Air Force 1", 1, 2, 110.00, "Classic white sneakers", "2024-01-02", "2024-01-02");
        addProductSeed(db, "Adidas Yeezy Boost", 2, 3, 220.00, "Premium limited edition sneakers", "2024-01-03", "2024-01-03");
        addProductSeed(db, "Puma RS-X", 3, 3, 100.00, "Retro-inspired casual sneakers", "2024-01-03", "2024-01-03");
        addProductSeed(db, "Nike React Element", 1, 4, 130.00, "Lightweight and comfortable", "2024-01-04", "2024-01-04");
        addProductSeed(db, "Adidas NMD", 2, 4, 150.00, "Modern lifestyle sneakers", "2024-01-04", "2024-01-04");
    }

    private void addUserSeed(SQLiteDatabase db, String name, String email, String password, String phone, String address, int role, boolean isActive) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE_NUMBER, phone);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_IS_ACTIVE, isActive ? 1 : 0);

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
    // Thêm dữ liệu mẫu cho bảng OrderDetails
    private void addOrderDetailSeed(SQLiteDatabase db, int orderId, int productSizeId, int quantity, double price) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderId);
        values.put(COLUMN_PRODUCT_SIZE_ID, productSizeId);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_PRICE, price);

        db.insert(TABLE_ORDER_DETAILS, null, values);
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

    // Phương thức thêm dữ liệu mẫu cho Stores
    private void addStoreSeed(SQLiteDatabase db, String storeName, String location, int ownerId, String createdDate, String updatedDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STORE_NAME, storeName);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_OWNER_ID, ownerId);
        values.put(COLUMN_CREATED_DATE, createdDate);
        values.put(COLUMN_UPDATED_DATE, updatedDate);

        db.insert(TABLE_STORES, null, values);
    }

    // Phương thức thêm dữ liệu mẫu cho Products
    private void addProductSeed(SQLiteDatabase db, String productName, int brandId, int storeId, double price, String description, String createdDate, String updatedDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_BRAND_ID, brandId);
        values.put(COLUMN_STORE_ID, storeId);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CREATED_DATE, createdDate);
        values.put(COLUMN_UPDATED_DATE, updatedDate);

        db.insert(TABLE_PRODUCTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        onCreate(db);
    }
}

