package com.example.subproject.CRUD_Realtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subproject.ImagePickerActivity;
import com.example.subproject.MainActivity;
import com.example.subproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class RVActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter adapter;
    DAOEmployee dao;
    boolean isLoading = false;
    String key = null;
    ImageView imgAvatar;
    ImageView btn_show;
    TextView txt_option;
    private Context context;
    ImageView imgLogout;
    EditText edtSearch;
    Button btnSearch;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvactivity);
        ImageView btn_show = findViewById(R.id.btn_show);
        txt_option = findViewById(R.id.txt_option);
        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.rv);
        imgAvatar = findViewById(R.id.imgAvatar);
        imgLogout = findViewById(R.id.imgLogout);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter= new RVAdapter(this);
        recyclerView.setAdapter(adapter);
        dao = new DAOEmployee();
        loadData();
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
                {
                    if(!isLoading)
                    {
                        isLoading=true;
                        loadData();
                    }
                }
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RVActivity.this,AddTaskActivity.class);
                startActivity(intent);
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RVActivity.this, ImagePickerActivity.class));
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtSearch.getText().length()>0){
                    loadDataSearch();
                }
            }
        });


    }
    private void loadDataSearch()
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cvRef = rootRef.child("CongViec");
        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ArrayList<CongViec> emps = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren())
                {

                    CongViec emp = data.getValue(CongViec.class);
                    emp.setKey(data.getKey());
                    if(emp.getTitle().contains(edtSearch.getText().toString())) {
                        emps.add(emp);
                        key = data.getKey();
                    }
                }

                adapter.setItems(emps);
                adapter.notifyDataSetChanged();
                isLoading =false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        cvRef.addListenerForSingleValueEvent(valueEventListener);
    }
    private void loadData()
    {
        swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ArrayList<CongViec> emps = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    CongViec emp = data.getValue(CongViec.class);
                    emp.setKey(data.getKey());
                    emps.add(emp);
                    key = data.getKey();
                }
                adapter.setItems(emps);
                adapter.notifyDataSetChanged();
                isLoading =false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


}