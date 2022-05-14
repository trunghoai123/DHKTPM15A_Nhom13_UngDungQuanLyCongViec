package com.example.subproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etEmail;
    TextInputEditText etPassword;
    TextInputEditText etReTypePass;
    TextView tvLoginHere;
    Button btnRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etTaskTitle);
        etPassword = findViewById(R.id.etPass);
        tvLoginHere = findViewById(R.id.tvGoToLogin);
        btnRegister = findViewById(R.id.btnAddTask);
        etReTypePass = findViewById(R.id.etReTypePass);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view -> {
            createUser();
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
        }
        else if (TextUtils.isEmpty(password)) {
            etReTypePass.setError("Please Re-type Password cannot be empty");
            etReTypePass.requestFocus();
        }
        else {
           if(etPassword.getText().toString().equals(etReTypePass.getText().toString())){ // nhập lại đúng mật khẩu
               mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                       } else {
                           Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }
           else{ // nhập lại sai mật khẩu
               etReTypePass.setError("Re-typed incorrect Password");
               etReTypePass.requestFocus();
           }
        }
    }
}