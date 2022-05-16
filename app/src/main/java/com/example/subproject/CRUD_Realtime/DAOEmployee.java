package com.example.subproject.CRUD_Realtime;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOEmployee {
    private DatabaseReference databaseReference;
    public DAOEmployee()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(CongViec.class.getSimpleName());
    }
    public Task<Void> add(CongViec emp)
    {
        return databaseReference.push().setValue(emp);
    }

    public Task<Void> update(String key, HashMap<String ,Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }
    public Task<Void> remove(String key)
    {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key)
    {
//        if(key == null)
//        {
            return databaseReference.orderByKey().limitToFirst(100);
//        }
//        else
//        if(key.equals("all")){
//            return databaseReference.orderByKey();
//        }
//        return databaseReference.orderByKey().startAfter(key).limitToFirst(100);
    }

    public Query get()
    {
        return databaseReference;
    }
}

