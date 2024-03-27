package com.example.quanliamthanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanliamthanh.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CategoryActivity extends AppCompatActivity {
    private EditText editTextCategoryName;
    private Button buttonAddCategory;

    private FirebaseFirestore db;
    private CollectionReference categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        db = FirebaseFirestore.getInstance();
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);

        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = editTextCategoryName.getText().toString();
                if (!categoryName.isEmpty()) {
                    Category newCategory = new Category("", categoryName); // Tạo đối tượng Category mới
                    addCategoryToFirestore(newCategory); // Thêm đối tượng Category vào Firestore
                } else {
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCategoryToFirestore(Category newCategory) {
        // Khởi tạo Firestore
        CollectionReference categories = db.collection("categories");

        // Thêm đối tượng Category vào Firestore
        categories.add(newCategory)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Thêm thành công
                        String categoryID = documentReference.getId(); // Lấy ID của document vừa được tạo
                        newCategory.setCategoryId(categoryID); // Cập nhật ID của Category

                        // Cập nhật document với categoryId mới
                        categories.document(categoryID)
                                .set(newCategory)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Cập nhật document thành công
                                        Toast.makeText(CategoryActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Cập nhật document thất bại
                                        Toast.makeText(CategoryActivity.this, "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi thêm thất bại (vd: hiển thị thông báo lỗi)
                        Toast.makeText(CategoryActivity.this, "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
