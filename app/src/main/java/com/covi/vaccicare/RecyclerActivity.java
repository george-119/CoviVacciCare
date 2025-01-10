package com.covi.vaccicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity{


    RecyclerView recyclerView;
    String keyword;
    Query query;
    DAOVaccinedetails dao;
    FirebaseRecyclerAdapter adapter;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        progress = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        dao = new DAOVaccinedetails();
        if (!isNetworkAvailable(RecyclerActivity.this)) {
            Toast.makeText(RecyclerActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }
        LoadData();
    }

    public void LoadData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
        query = reference.orderByChild("role").equalTo(getIntent().getStringExtra("role"));

        FirebaseRecyclerOptions<Vaccinedetails> option =
                new FirebaseRecyclerOptions.Builder<Vaccinedetails>()
                .setQuery(query, new SnapshotParser<Vaccinedetails>() {
                    @NonNull
                    @Override
                    public Vaccinedetails parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Vaccinedetails vd = snapshot.getValue(Vaccinedetails.class);
                        vd.setKey(snapshot.getKey());
                        progress.setVisibility(View.INVISIBLE);

                        return vd;
                    }
                }).build();

                    adapter  = new FirebaseRecyclerAdapter(option) {
                    @Override
                    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull Object o) {

                        Vaccinedetails vd = (Vaccinedetails) o;
                        VaccinedetailsVH vh = (VaccinedetailsVH) viewHolder;
                        vh.txt_name.setText(vd.getName());
                        vh.txt_status.setText(vd.getVstatus());
                        if (vd.getRole().equals("(Student)")) {
                            vh.txt_class.setText(vd.getClsdiv());
                            vh.txt_class.setVisibility(View.VISIBLE);
                        }
                        AlphaAnimation buttonClick1 = new AlphaAnimation(1F, 0.6F);
                        vh.txt_card.setOnClickListener(v -> {
                            if (isNetworkAvailable(RecyclerActivity.this)) {
                                v.startAnimation(buttonClick1);

                                Intent intent = new Intent(RecyclerActivity.this, AdminviewActivity.class);
                                intent.putExtra("value", "+91" + vd.getMobile());
                                startActivity(intent);
                            }else{
                                Toast.makeText(RecyclerActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                            }
                        });


                        vh.txt_option.setOnClickListener(v -> {
                            if (isNetworkAvailable(RecyclerActivity.this)) {
                                v.startAnimation(buttonClick1);
                                new AlertDialog.Builder(RecyclerActivity.this)
                                        .setTitle("Are you sure you want to delete record?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DAOVaccinedetails dao = new DAOVaccinedetails();
                                                dao.remove("+91" + vd.getMobile()).addOnSuccessListener(suc ->
                                                {
                                                    Toast.makeText(RecyclerActivity.this, "Record is removed", Toast.LENGTH_SHORT).show();
                                                }).addOnFailureListener(er ->
                                                {
                                                    Toast.makeText(RecyclerActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                            }
                                        })
                                        .setNegativeButton("No", null).show();
                            }else{
                                Toast.makeText(RecyclerActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(RecyclerActivity.this).inflate(R.layout.layout_item, parent, false);
                        return new VaccinedetailsVH(view);
                    }

                    @Override
                    public void onDataChanged() {
                    }
                };
                recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false; }



}
