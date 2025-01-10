package com.covi.vaccicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {
    TextView text_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        DAOVaccinedetails dao = new DAOVaccinedetails();

        String value = getIntent().getStringExtra("key");
        String mobile = getIntent().getStringExtra("mobile");

        final ProgressBar progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        text_welcome = findViewById(R.id.text_welcome);


        if (!isNetworkAvailable(DashboardActivity.this)) {
            Toast.makeText(DashboardActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(value).child("name").getValue().toString();
                text_welcome.setText(name);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button btn_edit = findViewById(R.id.btn_edit);
        Button btn_view = findViewById(R.id.btn_view);

        AlphaAnimation buttonClick= new AlphaAnimation(1F,0.6F);
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(DashboardActivity.this)) {
                    v.startAnimation(buttonClick);

                    progressBar.setVisibility(View.VISIBLE);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String db_aadhar = snapshot.child(value).child("aadhar").getValue().toString();
                            String db_mobile = snapshot.child(value).child("mobile").getValue().toString();
                            String db_name = snapshot.child(value).child("name").getValue().toString();
                            String db_role = snapshot.child(value).child("role").getValue().toString();
                            String db_vaccine1 = snapshot.child(value).child("vaccine1").getValue().toString();
                            String db_vdate1 = snapshot.child(value).child("vdate1").getValue().toString();
                            String db_vaccine2 = snapshot.child(value).child("vaccine2").getValue().toString();
                            String db_vdate2 = snapshot.child(value).child("vdate2").getValue().toString();
                            String db_vstatus = snapshot.child(value).child("vstatus").getValue().toString();
                            String db_clsdiv = snapshot.child(value).child("clsdiv").getValue().toString();
                            String db_dist = snapshot.child(value).child("dist").getValue().toString();
                            String db_cmp = snapshot.child(value).child("cmp").getValue().toString();
                            String db_cmpname = snapshot.child(value).child("cmpname").getValue().toString();
                            String db_ward = snapshot.child(value).child("ward").getValue().toString();

                            Intent intent = new Intent(DashboardActivity.this, ViewdetailsActivity.class);
                            intent.putExtra("db_aadhar", db_aadhar);
                            intent.putExtra("db_mobile", db_mobile);
                            intent.putExtra("db_name", db_name);
                            intent.putExtra("db_role", db_role);
                            intent.putExtra("db_vaccine1", db_vaccine1);
                            intent.putExtra("db_vdate1", db_vdate1);
                            intent.putExtra("db_vaccine2", db_vaccine2);
                            intent.putExtra("db_vdate2", db_vdate2);
                            intent.putExtra("db_vstatus", db_vstatus);
                            intent.putExtra("db_clsdiv", db_clsdiv);
                            intent.putExtra("db_dist", db_dist);
                            intent.putExtra("db_cmp", db_cmp);
                            intent.putExtra("db_cmpname", db_cmpname);
                            intent.putExtra("db_ward", db_ward);
                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                        Toast.makeText(DashboardActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlphaAnimation buttonClick2= new AlphaAnimation(1F,0.6F);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(DashboardActivity.this)) {
                    v.startAnimation(buttonClick2);

                    progressBar.setVisibility(View.VISIBLE);


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String db_aadhar = snapshot.child(value).child("aadhar").getValue().toString();
                            String db_mobile = snapshot.child(value).child("mobile").getValue().toString();
                            String db_name = snapshot.child(value).child("name").getValue().toString();
                            String db_role = snapshot.child(value).child("role").getValue().toString();
                            String db_vaccine1 = snapshot.child(value).child("vaccine1").getValue().toString();
                            String db_vdate1 = snapshot.child(value).child("vdate1").getValue().toString();
                            String db_vaccine2 = snapshot.child(value).child("vaccine2").getValue().toString();
                            String db_vdate2 = snapshot.child(value).child("vdate2").getValue().toString();
                            String db_vstatus = snapshot.child(value).child("vstatus").getValue().toString();
                            String db_clsdiv = snapshot.child(value).child("clsdiv").getValue().toString();
                            String db_dist = snapshot.child(value).child("dist").getValue().toString();
                            String db_cmp = snapshot.child(value).child("cmp").getValue().toString();
                            String db_cmpname = snapshot.child(value).child("cmpname").getValue().toString();
                            String db_ward = snapshot.child(value).child("ward").getValue().toString();

                            Intent intent = new Intent(DashboardActivity.this, EditActivity.class);
                            intent.putExtra("db_aadhar", db_aadhar);
                            intent.putExtra("db_mobile", db_mobile);
                            intent.putExtra("db_name", db_name);
                            intent.putExtra("db_role", db_role);
                            intent.putExtra("db_vaccine1", db_vaccine1);
                            intent.putExtra("db_vdate1", db_vdate1);
                            intent.putExtra("db_vaccine2", db_vaccine2);
                            intent.putExtra("db_vdate2", db_vdate2);
                            intent.putExtra("db_vstatus", db_vstatus);
                            intent.putExtra("db_clsdiv", db_clsdiv);
                            intent.putExtra("db_dist", db_dist);
                            intent.putExtra("db_cmp", db_cmp);
                            intent.putExtra("db_cmpname", db_cmpname);
                            intent.putExtra("db_ward", db_ward);
                            intent.putExtra("key", value);

                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    Toast.makeText(DashboardActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(DashboardActivity.this)
                .setTitle("Are you sure you want to logout?").setCancelable(false)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("Cancel",null).show();
    }
    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false; }
}


