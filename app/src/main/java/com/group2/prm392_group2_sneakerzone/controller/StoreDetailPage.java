package com.group2.prm392_group2_sneakerzone.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group2.prm392_group2_sneakerzone.R;
import com.group2.prm392_group2_sneakerzone.adapter.CartAdapter;
import com.group2.prm392_group2_sneakerzone.adapter.ProductCustomerAdapter;
import com.group2.prm392_group2_sneakerzone.api.CreateOrder;
import com.group2.prm392_group2_sneakerzone.model.Brand;
import com.group2.prm392_group2_sneakerzone.model.CartItem;
import com.group2.prm392_group2_sneakerzone.model.Order;
import com.group2.prm392_group2_sneakerzone.model.OrderDetail;
import com.group2.prm392_group2_sneakerzone.model.Product;
import com.group2.prm392_group2_sneakerzone.model.ProductSize;
import com.group2.prm392_group2_sneakerzone.utils.BrandDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.OrderDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.OrderDetailDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.ProductDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.ProductSizeDBHelper;
import com.group2.prm392_group2_sneakerzone.utils.UserDBHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class StoreDetailPage extends AppCompatActivity implements ProductCustomerAdapter.OnAddToCartClickListener, CartAdapter.OnCartItemInteractionListener {

    private RecyclerView productRecyclerView;
    private ProductCustomerAdapter productAdapter;
    private List<Product> productList;
    private ProductDBHelper productDBHelper;
    private ProductSizeDBHelper productSizeDBHelper;
    private BrandDBHelper brandDBHelper;
    private TextView storeName, storeLocation, storeOwner;
    private ImageView storeImageView;
    private Button viewCartButton;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private TextView totalAmountText;
    private double totalAmount;
    private int storeId;
    // Declare BottomSheetDialog as a field
    private BottomSheetDialog cartDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        StrictMode.ThreadPolicy policy = new
        StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Initialize views and other setup
        storeImageView = findViewById(R.id.storeImageView);
        storeName = findViewById(R.id.storeDetailName);
        storeLocation = findViewById(R.id.storeDetailLocation);
        storeOwner = findViewById(R.id.storeDetailOwner);
        viewCartButton = findViewById(R.id.viewCartButton);
        productRecyclerView = findViewById(R.id.productRecyclerView);

        // Get store details from Intent and set up views
        Intent intent = getIntent();
        storeId = intent.getIntExtra("STORE_ID", -1);
        String storeImage = intent.getStringExtra("STORE_IMAGE");
        storeName.setText(intent.getStringExtra("STORE_NAME"));
        storeLocation.setText(intent.getStringExtra("STORE_LOCATION"));
        storeOwner.setText("Owner ID: " + intent.getIntExtra("OWNER_ID", -1));

        if (storeImage != null && !storeImage.isEmpty()) {
            Glide.with(this).load(storeImage).into(storeImageView);
        } else {
            storeImageView.setImageResource(R.drawable.ic_launcher_background);
        }

        // Initialize database helpers
        productDBHelper = ProductDBHelper.getInstance(this);
        productSizeDBHelper = ProductSizeDBHelper.getInstance(this);
        brandDBHelper = BrandDBHelper.getInstance(this);

        // Load products for this store and set up RecyclerView
        productList = productDBHelper.getProductsByStoreId(storeId);
        productAdapter = new ProductCustomerAdapter(this, productList, this);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productRecyclerView.setAdapter(productAdapter);

        // View Cart Button to open Cart Drawer
        viewCartButton.setOnClickListener(view -> showCartDrawer());
    }

    private void showCartDrawer() {
        cartDrawer = new BottomSheetDialog(this);
        cartDrawer.setContentView(R.layout.layout_cart_drawer);

        RecyclerView cartRecyclerView = cartDrawer.findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView emptyCartText = cartDrawer.findViewById(R.id.emptyCartText);
        totalAmountText = cartDrawer.findViewById(R.id.totalAmountText);
        Button checkoutButton = cartDrawer.findViewById(R.id.checkoutButton);

        if (cartItems.isEmpty()) {
            emptyCartText.setVisibility(View.VISIBLE);
            totalAmountText.setVisibility(View.GONE);
            checkoutButton.setEnabled(false);
        } else {
            emptyCartText.setVisibility(View.GONE);
            totalAmountText.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(true);

            cartAdapter = new CartAdapter(cartItems, this);
            cartRecyclerView.setAdapter(cartAdapter);
            calculateTotalAmount();
        }

        checkoutButton.setOnClickListener(v -> handleCheckout());

        cartDrawer.show();
    }

    private void calculateTotalAmount() {
        totalAmount = 0.0;
        for (CartItem item : cartItems) {
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
        }

        if (totalAmountText != null) {
            totalAmountText.setText("Total: $" + String.format("%.2f", totalAmount));
        }
    }

    private void handleCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int customerId = UserDBHelper.currentUserId; // Replace with actual customer ID
        Order order = new Order(0, customerId, storeId, totalAmount, "2024-11-01", "Pending");
        int orderId = createOrder(order);

        for (CartItem item : cartItems) {
            OrderDetail orderDetail = new OrderDetail(0, orderId, item.getProduct().getProductId(), item.getQuantity(), item.getProduct().getPrice());
            createOrderDetail(orderDetail);
        }

        initiateZaloPayPayment(totalAmount); // Initiates payment after order details are created

        // Clear the cart and update the UI
        cartItems.clear();
        calculateTotalAmount();
    }

    private void initiateZaloPayPayment(double amount) {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(String.valueOf((int) amount));
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String zpTransToken = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(this, zpTransToken, "PRM392_Group2_SneakerZone://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {

                        showAlert("Payment Success", String.format("TransactionId: %s - TransToken: %s", transactionId, transToken));
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {

                        showAlert("Payment Canceled", String.format("Transaction canceled. zpTransToken: %s", zpTransToken));
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {

                        showAlert("Payment Error", String.format("Error: %s - zpTransToken: %s", zaloPayError.toString(), zpTransToken));
                    }
                });
            } else {
                Toast.makeText(this, "Failed to create payment order", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }



    private int createOrder(Order order) {
        OrderDBHelper orderDBHelper = OrderDBHelper.getInstance(this);
        long orderId = orderDBHelper.addOrderAndGetId(order);
        return (int) orderId;
    }

    private void createOrderDetail(OrderDetail orderDetail) {
        OrderDetailDBHelper orderDetailDBHelper = OrderDetailDBHelper.getInstance(this);
        orderDetailDBHelper.addOrderDetail(orderDetail);
    }

    @Override
    public void onAddToCartClick(Product product) {
        showAddToCartPopup(product);
    }

    private void showAddToCartPopup(Product product) {
        BottomSheetDialog addToCartDialog = new BottomSheetDialog(this);
        addToCartDialog.setContentView(R.layout.dialog_add_to_cart);

        // Initialize dialog views
        ImageView productImage = addToCartDialog.findViewById(R.id.productImage);
        TextView productName = addToCartDialog.findViewById(R.id.productName);
        TextView productPrice = addToCartDialog.findViewById(R.id.productPrice);
        TextView productDescription = addToCartDialog.findViewById(R.id.productDescription);
        TextView productBrand = addToCartDialog.findViewById(R.id.productBrand);
        Spinner sizeSpinner = addToCartDialog.findViewById(R.id.sizeSpinner);
        TextView quantityInStock = addToCartDialog.findViewById(R.id.quantityInStock);
        TextView quantityText = addToCartDialog.findViewById(R.id.quantityText);
        Button decreaseButton = addToCartDialog.findViewById(R.id.decreaseButton);
        Button increaseButton = addToCartDialog.findViewById(R.id.increaseButton);
        Button addToCartButton = addToCartDialog.findViewById(R.id.addToCartButton);

        // Set product details
        productName.setText(product.getProductName());
        productPrice.setText("$" + product.getPrice());
        productDescription.setText(product.getDescription());
        Glide.with(this).load(product.getProductImage()).into(productImage);

        // Fetch brand name
        Brand brand = brandDBHelper.getBrandById(product.getBrandId());
        if (brand != null) {
            productBrand.setText(brand.getBrandName());
        }

        // Load available sizes for the product
        List<ProductSize> productSizes = productSizeDBHelper.getSizesByProductId(product.getProductId());
        List<String> sizeOptions = new ArrayList<>();
        for (ProductSize size : productSizes) {
            sizeOptions.add(size.getSize());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);

        final int[] quantity = {1};
        quantityText.setText(String.valueOf(quantity[0]));

        // Set initial max stock based on the selected size
        final int[] maxStock = {productSizes.get(0).getQuantity()}; // Default to first size's stock
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductSize selectedSize = productSizes.get(position);
                maxStock[0] = selectedSize.getQuantity(); // Update max stock for selected size
                quantityInStock.setText(String.valueOf(selectedSize.getQuantity()));
                quantity[0] = 1; // Reset quantity when size changes
                quantityText.setText(String.valueOf(quantity[0]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Increase and decrease quantity buttons
        decreaseButton.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                quantityText.setText(String.valueOf(quantity[0]));
            }
        });

        increaseButton.setOnClickListener(v -> {
            if (quantity[0] < maxStock[0]) {
                quantity[0]++;
                quantityText.setText(String.valueOf(quantity[0]));
            } else {
                Toast.makeText(this, "Exceeds available stock", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Add to Cart action
        addToCartButton.setOnClickListener(view -> {
            String selectedSize = sizeSpinner.getSelectedItem().toString();
            boolean itemExistsInCart = false;

            for (CartItem item : cartItems) {
                if (item.getProduct().getProductId() == product.getProductId() && item.getSize().equals(selectedSize)) {
                    // Check if adding the quantity exceeds max stock
                    if (item.getQuantity() + quantity[0] <= maxStock[0]) {
                        item.setQuantity(item.getQuantity() + quantity[0]);
                        Toast.makeText(this, "Added " + quantity[0] + " of " + product.getProductName() + " (Size: " + selectedSize + ") to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Cannot exceed maximum stock of " + maxStock[0], Toast.LENGTH_SHORT).show();
                    }
                    itemExistsInCart = true;
                    break;
                }
            }

            // Add new item if it does not exist in cart
            if (!itemExistsInCart) {
                if (quantity[0] <= maxStock[0]) {
                    cartItems.add(new CartItem(product, selectedSize, quantity[0]));
                    Toast.makeText(this, "Added " + quantity[0] + " of " + product.getProductName() + " (Size: " + selectedSize + ") to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cannot exceed maximum stock of " + maxStock[0], Toast.LENGTH_SHORT).show();
                }
            }

            calculateTotalAmount(); // Update the total amount after adding
            addToCartDialog.dismiss();
        });

        addToCartDialog.show();
    }


    @Override
    public void onIncreaseQuantity(CartItem cartItem) {
        int maxStock = productSizeDBHelper.getSizeByProductIdAndSize(cartItem.getProduct().getProductId(), cartItem.getSize()).getQuantity();
        if (cartItem.getQuantity() < maxStock) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Cannot exceed maximum stock of " + maxStock, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDecreaseQuantity(CartItem cartItem) {
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        } else {
            Toast.makeText(this, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
        }
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRemoveItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartAdapter.notifyDataSetChanged();
        calculateTotalAmount();

        if (cartItems.isEmpty() && cartDrawer != null) {
            TextView emptyCartText = cartDrawer.findViewById(R.id.emptyCartText);
            Button checkoutButton = cartDrawer.findViewById(R.id.checkoutButton);

            if (totalAmountText != null) totalAmountText.setVisibility(View.GONE);
            if (emptyCartText != null) emptyCartText.setVisibility(View.VISIBLE);
            if (checkoutButton != null) checkoutButton.setEnabled(false);
        }
    }
}