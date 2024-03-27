package com.example.quanliamthanh;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanliamthanh.model.Brand;
import com.example.quanliamthanh.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BrandActivty extends AppCompatActivity {
    private EditText editTextBrandName;
    private Button buttonAddBrand;

    private FirebaseFirestore db;
    private CollectionReference BrandCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        db = FirebaseFirestore.getInstance();
        editTextBrandName = findViewById(R.id.editTextBrandName);
        buttonAddBrand = findViewById(R.id.buttonAddBrand);

        buttonAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String brandName = editTextBrandName.getText().toString();
                if (!brandName.isEmpty()) {
                    Brand newBrand = new Brand("", brandName); // Tạo đối tượng Category mới
                    addBrandToFirestore(newBrand); // Thêm đối tượng newBrand vào Firestore
                } else {
                    Toast.makeText(BrandActivty.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addBrandToFirestore(Brand newBrand) {
        // Khởi tạo Firestore
        CollectionReference BrandCollection = db.collection("BrandCollection");

        // Thêm đối tượng brand vào Firestore
        BrandCollection.add(newBrand)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Thêm thành công
                        String brandID = documentReference.getId(); // Lấy ID của document vừa được tạo
                        newBrand.setId(brandID); // Cập nhật ID của Brand

                        // Cập nhật document với BrandId mới
                        BrandCollection.document(brandID)
                                .set(newBrand)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Cập nhật document thành công
                                        Toast.makeText(BrandActivty.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Cập nhật document thất bại
                                        Toast.makeText(BrandActivty.this, "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi thêm thất bại (vd: hiển thị thông báo lỗi)
                        Toast.makeText(BrandActivty.this, "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}