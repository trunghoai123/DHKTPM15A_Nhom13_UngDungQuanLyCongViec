package com.example.subproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.subproject.CRUD_Realtime.AddTaskActivity;
import com.example.subproject.CRUD_Realtime.RVActivity;
import com.example.subproject.databinding.ActivityGetImageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else{
//            startActivity(new Intent(MainActivity.this, GetImageActivity.class));
            startActivity(new Intent(MainActivity.this, RVActivity.class));
        }
    }
}