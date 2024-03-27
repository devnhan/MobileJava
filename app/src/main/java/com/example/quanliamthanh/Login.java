package com.example.quanliamthanh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanliamthanh.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText Email, Password;
    private Button login;
    private TextView RegisterDirect, ForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth .getInstance();
        firestore=FirebaseFirestore.getInstance();
        Email = findViewById(R.id.inputEmail);
        Password = findViewById(R.id.inputPassword);
        login = findViewById(R.id.btnDangNhap);
        RegisterDirect = findViewById(R.id.txtDangKy);
        ForgotPassword = findViewById(R.id.txtQuenPass);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String pass = Password.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if (!pass.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Login.this,"Login thành công",Toast.LENGTH_SHORT).show();
                                        FirebaseUser userRecived= mAuth.getCurrentUser();
                                        DocumentReference userRef=firestore.collection("users").document(userRecived.getUid());
                                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    // Get the document snapshot
                                                    DocumentSnapshot document = task.getResult();

                                                    if (document.exists()) {
                                                        // The document exists, extract the data and convert it to your User model
                                                        User userModel = document.toObject(User.class);

                                                        Intent intent = new Intent(Login.this, HomePage.class);
//                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        intent.putExtra("user_data", userModel);

                                                        startActivity(intent);

                                                    } else {
                                                        // The document doesn't exist, handle the error or show a message
                                                        // if the user is not found in the Firestore database
                                                    }
                                                } else {

                                                    Exception exception = task.getException();
                                                    if (exception instanceof FirebaseFirestoreException) {
                                                        // Firestore-specific exception handling
                                                    } else {
                                                        // Other generic exception handling
                                                    }
                                                }
                                            }
                                        });
                                        Intent intent = new Intent(Login.this, HomePage.class);
                                        intent.putExtra("user_data", userRecived);
                                        startActivity(intent);

                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this,"Login thất bại",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        Password.setError("Pass không được để trống");
                    }
                } else if (email.isEmpty()) {
                    Email.setError("Email không được để trống");
                } else {
                    Email.setError("Vui lòng nhập đúng định dạng Email");
                }
            }
        });
            RegisterDirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     startActivity(new Intent(Login.this,Register.class));
                }
            });
            ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialog();
            }
        });



    }
    private void showForgotPasswordDialog(){
        startActivity(new Intent(Login.this,ForgotPass.class));
    }
}