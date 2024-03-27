package com.example.quanliamthanh.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.example.quanliamthanh.HomePage;
import com.example.quanliamthanh.R;
import com.example.quanliamthanh.model.Category;
import com.example.quanliamthanh.model.Product;
import com.example.quanliamthanh.model.Brand;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DialogAddProduct extends Dialog {
    private Context context;
    private List<Category> categoryList = new ArrayList<>();
    private ArrayList<Product> productList = new ArrayList<>();
    private List<Brand> brandList = new ArrayList<>();
    private EditText edtTitle;
    private ArrayAdapter<Product> productAdapter;
    private EditText edtPrice,edtImageUrl;
    private EditText edtQuantity;
    private ArrayAdapter<Category> categoryAdapter;
    private ArrayAdapter<Brand> brandAdapter;
    private Spinner categorySpinner, brandSpinner;
    private EditText edtDescription;
    private Button btnAddProduct;

    private ImageView imgProduct;
    private Button btnChooseImage;
    private FirebaseFirestore db;
    private Uri selectedImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    public DialogAddProduct(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_product);

        db = FirebaseFirestore.getInstance();
        edtTitle = findViewById(R.id.edtTitle);
        edtPrice = findViewById(R.id.edtPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtDescription = findViewById(R.id.edtDescription);
        categorySpinner = findViewById(R.id.spinnerCategory);
        brandSpinner = findViewById(R.id.spinnerBrand);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        categorySpinner.setAdapter(categoryAdapter);
        brandSpinner.setAdapter(brandAdapter);

        categoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        brandAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, brandList);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);

        fetchCategoriesFromFirestore(); // Gọi hàm để lấy dữ liệu categories từ Firestore
        fetchBrandsFromFirestore(); // Gọi hàm để lấy dữ liệu brands từ Firestore
        edtImageUrl = findViewById(R.id.edtImageUrl); // Initialize the EditText for image URL input
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToFirestore();
                dismiss();
            }
        });
    }
    private void fetchBrandsFromFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("BrandCollection")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    brandList.clear();
                    for (Brand brand : queryDocumentSnapshots.toObjects(Brand.class)) {
                        brandList.add(brand);
                    }
                    brandAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu cần
                });
    }

    private void fetchCategoriesFromFirestore() {
        // Fetch categories from Firestore and populate categoryList
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categoryList.clear();
                    for (Category category : queryDocumentSnapshots.toObjects(Category.class)) {
                        categoryList.add(category);
                    }
                    categoryAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle fetch failure if needed
                });
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

    private void addProductToFirestore() {
        String title = edtTitle.getText().toString();
        double priceValue = Double.parseDouble(edtPrice.getText().toString());
        String quantity = edtQuantity.getText().toString();
        String description = edtDescription.getText().toString();
        Category selectedCategory = (Category) categorySpinner.getSelectedItem();
        Brand selectedBrand = (Brand) brandSpinner.getSelectedItem();
        String imageUrl = edtImageUrl.getText().toString().trim();
        if (title.isEmpty() || edtPrice.getText().toString().isEmpty() || quantity.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(context, "Vui lòng điền đủ thông tin và chọn ảnh!", Toast.LENGTH_SHORT).show();
        }
        else {
            String productId = generateProductId();
            // Tải hình ảnh từ URL và hiển thị trong ImageView
            ImageView imgProduct = findViewById(R.id.imgProduct); // Thay thế id này bằng id của ImageView trong dialog
            Glide.with(context)
                    .load(imageUrl)
                    .into(imgProduct);

            Product product = new Product(productId, title, priceValue, quantity, description, selectedCategory.getCategoryName(), selectedBrand.getName(), imageUrl);

            // Lưu product vào Firestore
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("ProductCollection")
                    .document(productId)
                    .set(product)
                    .addOnSuccessListener(aVoid -> {
                        fetchProductFromFirestore();

                        Intent intent = new Intent(context, HomePage.class);
                        context.startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private String generateProductId() {
        return UUID.randomUUID().toString(); // Sử dụng UUID để tạo ID duy nhất
    }

}