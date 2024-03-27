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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;

    private TextView loginDirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        mAuth = FirebaseAuth.getInstance();
        loginDirect = findViewById(R.id.txtBack);


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(ForgotPass.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                } else {
                    // Gửi email đặt lại mật khẩu
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPass.this, "Đã gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ForgotPass.this, "Gửi email đặt lại mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                    startActivity(new Intent(ForgotPass.this, Login.class));
                                    finish();
                                }
                            });
                }
            }
        });
        loginDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPass.this, Login.class));
            }
        });
    }
}