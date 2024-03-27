package com.example.quanliamthanh;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanliamthanh.adapter.DialogAddProduct;
import com.example.quanliamthanh.adapter.ProductAdapter;
import com.example.quanliamthanh.model.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayAdapter<Product> productAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DialogAddProduct dialogAddProduct;
    private Button addButton;

    private PopupWindow popupWindow;

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        addButton = findViewById(R.id.myButton);

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch products from Firestore and populate productList
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("ProductCollection")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Product product = documentSnapshot.toObject(Product.class);
                        productList.add(product);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle fetch failure if needed
                });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialogAddProduct();
            }
        });

        // Gắn ItemTouchHelper vào RecyclerView để bắt sự kiện swipe
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Xoá sản phẩm khi người dùng swipe trên item
                int position = viewHolder.getAdapterPosition();
                Product product = productList.get(position);
                String productId = product.getProductId();
                deleteProductFromFirestore(productId);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void startDialogAddProduct() {
        // Create a new instance of DialogAddProduct if not already created
        if (dialogAddProduct == null) {
            dialogAddProduct = new DialogAddProduct(HomePage.this);
            dialogAddProduct.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
//                    refreshProductList();
                }
            });
        }
        // Show the existing dialog if already created
        dialogAddProduct.show();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
// thao tác bấm vào nút logout trên thanh toolbar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_logout) {
            logoutAndNavigateToLogin();
        } else if (itemId == R.id.nav_brand) {
            navigateToBrandPage();
        } else if (itemId == R.id.nav_category) {
            navigateToCategoryPage();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
// sự kiện thoát xoá thông tin đăng nhập và trở lại trang login
    private void logoutAndNavigateToLogin() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(HomePage.this, Login.class);
        startActivity(intent);
        finish();
    }
    private void navigateToBrandPage() {
        Intent intent = new Intent(HomePage.this, BrandActivty.class);
        startActivity(intent);
    }
    private void navigateToCategoryPage() {
        Intent intent = new Intent(HomePage.this, CategoryActivity.class);
        startActivity(intent);
    }
    private void fetchProductFromFirestore () {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("ProductCollection")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear(); // Clear the existing list before adding new data
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Product product = documentSnapshot.toObject(Product.class);
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
//                    initRecyclerView();
                })
                .addOnFailureListener(e -> {
                    // Handle fetch failure if needed
                });
    }
    private void deleteProductFromFirestore(String productId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("ProductCollection")
                .document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Delete successful, update student list and notify RecyclerView
                    fetchProductFromFirestore();
                })
                .addOnFailureListener(e -> {
                    // Handle delete failure if needed
                });
    }
}