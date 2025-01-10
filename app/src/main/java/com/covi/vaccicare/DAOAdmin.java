package com.covi.vaccicare;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOAdmin {
    private DatabaseReference databaseReference;
    public  DAOAdmin(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Admin.class.getSimpleName());
    }
    public Task<Void> add(String key, Admin vd)
    {
        return databaseReference.child(key).setValue(vd);
    }
    public Task<Void> update(String key, HashMap<String,Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }
    public Task<Void> remove(String key)
    {

        return databaseReference.child(key).removeValue();
    }
    public Query get(){
        return databaseReference;
    }
}
