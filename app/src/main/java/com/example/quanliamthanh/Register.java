package com.example.quanliamthanh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanliamthanh.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private User u;
    private EditText user, email, password,repassword;
    private Button register;
    private TextView loginDirect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        user = findViewById(R.id.edtNhapUser);
        email = findViewById(R.id.edtNhapEmail);
        password = findViewById(R.id.edtNhapPass);
        repassword = findViewById(R.id.edtXacNhanPass);
        register = findViewById(R.id.btnDangKy);
        loginDirect = findViewById(R.id.txtDaCoTaiKhoan);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInput = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String name= user.getText().toString().trim();
                String repass = repassword.getText().toString().trim();
                if (emailInput.isEmpty() || pass.isEmpty() || repass.isEmpty() || name.isEmpty()) {
                    // Nếu có thông tin bị bỏ trống, thông báo lỗi và không tiếp tục tạo tài khoản
                    Toast.makeText(Register.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(repass)) {
                    // Nếu mật khẩu nhập lại không khớp, thông báo lỗi và không tiếp tục tạo tài khoản
                    Toast.makeText(Register.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    // Nếu email không hợp lệ, thông báo lỗi và không tiếp tục tạo tài khoản
                    email.setError("Email không hợp lệ");
                    return;
                }
                else{
                    mAuth.createUserWithEmailAndPassword(emailInput,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                firestore=FirebaseFirestore.getInstance();
                                u=new User();
                                u.setId(user.getUid());
                                u.setEmail(user.getEmail());
                                u.setFullName(name);
                                String id=user.getUid();

                                CollectionReference userRef= firestore.collection("users");
//                                userRef.add(u);
                                userRef.document(user.getUid()).set(u);
//                                startActivity(new Intent(Register.this,Login.class));
                                startActivity(new Intent(Register.this, Login.class));
                                finish();
                            }else {
                                Toast.makeText(Register.this,"Đăng ký thất bại" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        loginDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

    }
}