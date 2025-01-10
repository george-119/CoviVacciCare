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
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity{


    String keyword;
    RecyclerView recyclerView;
    EditText searchView;
    DAOVaccinedetails dao;
    FirebaseRecyclerAdapter adapter;
    ProgressBar progress;
    List<String> ds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progress = findViewById(R.id.progress1);
        searchView = findViewById(R.id.search1);
        recyclerView = findViewById(R.id.rv1);
        Button print = findViewById(R.id.btn_printall1);
        TextView hint = findViewById(R.id.hint);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        dao = new DAOVaccinedetails();
        ds = new ArrayList<>();
        if (!isNetworkAvailable(SearchActivity.this)) {
            Toast.makeText(SearchActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        LoadData();

        print.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this)
                    .setTitle("We are working on it, this feature will be available soon...").setCancelable(false).setIcon(R.drawable.ic_baseline_sentiment_satisfied_alt_24)
                    .setPositiveButton("close", null).show();
        });

        if (isNetworkAvailable(SearchActivity.this)) {
            searchView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>0) {
                        hint.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.VISIBLE);
                        firebaseUserSearch(s.toString());
                        keyword = s.toString();
                        progress.setVisibility(View.INVISIBLE);

                    }else{
                        hint.setVisibility(View.VISIBLE);
                        return;
                    }
                    return;
                }
            });
            return;
        }else{
            Toast.makeText(SearchActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void createPDF() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
        Query q;
        q = reference.orderByChild("name").startAt(keyword).endAt(keyword+"\uf8ff");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(SearchActivity.this,"sp exists",Toast.LENGTH_LONG).show();
                    ds.clear();
                    for (DataSnapshot dss: snapshot.getChildren()){
                        String name = dss.getValue(String.class);
                        ds.add(name);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i=0;i<ds.size();i++){
                        stringBuilder.append(ds.get(i)+",");
                    }
                    Toast.makeText(SearchActivity.this,stringBuilder,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
        Query query = reference.orderByChild("role").equalTo(getIntent().getStringExtra("role"));

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
        progress.setVisibility(View.INVISIBLE);


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
                    v.startAnimation(buttonClick1);

                    Intent intent = new Intent(SearchActivity.this, AdminviewActivity.class);
                    intent.putExtra("value", "+91" + vd.getMobile());
                    startActivity(intent);
                });

                vh.txt_option.setOnClickListener(v -> {
                    v.startAnimation(buttonClick1);
                    new AlertDialog.Builder(SearchActivity.this)
                            .setTitle("Are you sure you want to delete record?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DAOVaccinedetails dao = new DAOVaccinedetails();
                                    dao.remove("+91" + vd.getMobile()).addOnSuccessListener(suc ->
                                    {
                                        Toast.makeText(SearchActivity.this, "Record is removed", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(er ->
                                    {
                                        Toast.makeText(SearchActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                                }
                            })
                            .setNegativeButton("No", null).show();
                });
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.layout_item, parent, false);
                return new VaccinedetailsVH(view);
            }

            @Override
            public void onDataChanged() {
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void firebaseUserSearch(String searchText) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
        Query firebaseSearchQuery;
        if(searchText.charAt(0) == '#'){
            searchText = searchText.substring(1);
            firebaseSearchQuery = reference.orderByChild("dose").startAt(searchText).endAt(searchText + "\uf8ff");
        }else if(searchText.charAt(0) == '@') {
            searchText = searchText.substring(1);
            firebaseSearchQuery = reference.orderByChild("clsdiv").startAt(searchText).endAt(searchText + "\uf8ff");
        }else if(searchText.charAt(0) == '*') {
            searchText = searchText.substring(1);
            firebaseSearchQuery = reference.orderByChild("dist").startAt(searchText).endAt(searchText + "\uf8ff");
        }else if(searchText.charAt(0) == '/') {
            searchText = searchText.substring(1);
            firebaseSearchQuery = reference.orderByChild("cmpname").startAt(searchText).endAt(searchText + "\uf8ff");
        }else{
            firebaseSearchQuery = reference.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        }

        //set Options
        FirebaseRecyclerOptions<Vaccinedetails> options =
                new FirebaseRecyclerOptions.Builder<Vaccinedetails>()
                        .setQuery(firebaseSearchQuery, Vaccinedetails.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter  = new FirebaseRecyclerAdapter(options) {
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
                    v.startAnimation(buttonClick1);

                    Intent intent = new Intent(SearchActivity.this, AdminviewActivity.class);
                    intent.putExtra("value", "+91" + vd.getMobile());
                    startActivity(intent);
                });


                vh.txt_option.setOnClickListener(v -> {
                    v.startAnimation(buttonClick1);
                    new AlertDialog.Builder(SearchActivity.this)
                            .setTitle("Are you sure you want to delete record?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DAOVaccinedetails dao = new DAOVaccinedetails();
                                    dao.remove("+91" + vd.getMobile()).addOnSuccessListener(suc ->
                                    {
                                        Toast.makeText(SearchActivity.this, "Record is removed", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(er ->
                                    {
                                        Toast.makeText(SearchActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                                }
                            })
                            .setNegativeButton("No", null).show();
                });
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.layout_item, parent, false);
                return new VaccinedetailsVH(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
            }
        }
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
