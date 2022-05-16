package com.example.subproject.CRUD_Realtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subproject.GetImageActivity;
import com.example.subproject.ImagePickerActivity;
import com.example.subproject.LoginActivity;
import com.example.subproject.MainActivity;
import com.example.subproject.R;
import com.example.subproject.databinding.ActivityGetImageBinding;
import com.example.subproject.databinding.ActivityRvactivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class RVActivity extends AppCompatActivity {
    ActivityRvactivityBinding binding;
    StorageReference storageReference;
    String email;
    FirebaseUser user;
    FirebaseAuth mAuth;
    //    search
    DatabaseReference ref;
    SearchView searchView;

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

    boolean press;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRvactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
//        setContentView(R.layout.activity_rvactivity);
        searchView = findViewById(R.id.searchView);
        ImageView btn_show = findViewById(R.id.btn_show);
        txt_option = findViewById(R.id.txt_option);
        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.rv);
        imgAvatar = findViewById(R.id.imgAvatar);
        imgLogout = findViewById(R.id.imgLogout);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter= new RVAdapter(this);
//        adapter_view = new RVAdapter(this);
        recyclerView.setAdapter(adapter);
        dao = new DAOEmployee();
        loadData("");
        press = false;
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                email = profile.getEmail();
            }
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem < lastVisible+3)
                {
                    if(!isLoading)
                    {
                        isLoading = true;
                        String str = searchView.getQuery().toString();
                        loadData(str);
                    }
                }
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RVActivity.this,AddTaskActivity.class);
                startActivity(intent);
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RVActivity.this, ImagePickerActivity.class));
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(RVActivity.this, LoginActivity.class));
                finish();
            }
        });
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    press = true;
                    loadData(s);
                    return true;
                }
            });
        }

//        get avatar
        String imageName = email;
        storageReference = FirebaseStorage.getInstance().getReference(imageName);

        try {
            File localFile = File.createTempFile("tempFile", ".jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            binding.imgAvatar.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RVActivity.this, "failed to retrive", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private void loadDataSearch(String str)
//    {
//        swipeRefreshLayout.setRefreshing(true);
//      if(str.trim().equals("")){
//          dao.get(key).addListenerForSingleValueEvent(new ValueEventListener()
//          {
//              @Override
//              public void onDataChange(@NonNull DataSnapshot snapshot)
//              {
//                  ArrayList<CongViec> emps = new ArrayList<>();
//
//
//                  recyclerView.setAdapter(adapter_view);
////                  adapter_view.setItems(emps);
//                  adapter_view.notifyDataSetChanged();
//                  isLoading = false;
//                  swipeRefreshLayout.setRefreshing(false);
//              }
//              @Override
//              public void onCancelled(@NonNull DatabaseError error)
//              {
//                  swipeRefreshLayout.setRefreshing(false);
//              }
//          });
//      }
//      else{
//          recyclerView.setAdapter(adapter);
//      }
//    }

//    private void loadDataSearch(String str)
//    {
//        swipeRefreshLayout.setRefreshing(true);
//        dao.get(key).addListenerForSingleValueEvent(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                ArrayList<CongViec> emps = new ArrayList<>();
//                for (DataSnapshot data : snapshot.getChildren())
//                {
//                    CongViec emp = data.getValue(CongViec.class);
//                    if(emp.getTitle().toLowerCase().contains(str.toLowerCase())){
//                        emp.setKey(data.getKey());
//                        emps.add(emp);
//                    }
////                    key = data.getKey();
//                }
//                adapter.removeAllItems();
//                adapter.setItems(emps);
////                System.out.println(emps);
////                adapter_view.setItems(adapter.getListItem());
//                adapter.notifyDataSetChanged();
//                isLoading = false;
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error)
//            {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }

    private void loadData(String str)
    {
//        if(press){
//            key = null;
//            press = false;
//        }
        swipeRefreshLayout.setRefreshing(true);
        if(str.trim().equals("")){
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
//                        key = data.getKey();
                    }
                    if(key == null){
                        adapter.removeAllItems();
                    }
                    System.out.println(emps.size());
                    adapter.setItems(emps);
//                System.out.println(emps);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        else{
            key = null;
            dao.get("all").addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    ArrayList<CongViec> emps = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        CongViec emp = data.getValue(CongViec.class);
                        if(emp.getTitle().toLowerCase().contains(str.toLowerCase())){
                            emp.setKey(data.getKey());
                            emps.add(emp);
                        }
                    }
                    adapter.removeAllItems();
                    adapter.setItems(emps);
//                System.out.println(emps);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
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
}