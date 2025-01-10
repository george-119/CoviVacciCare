package com.covi.vaccicare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AdmindashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard);

        Button btn_student = findViewById(R.id.btn_student);
        Button btn_staff = findViewById(R.id.btn_staff);
        TextView btn_cgp= findViewById(R.id.btn_passchnge);
        Button btn_sch= findViewById(R.id.btn_search);
        ProgressBar progressBar = findViewById(R.id.progressBarr2);

        if (!isNetworkAvailable(AdmindashboardActivity.this)) {
            Toast.makeText(AdmindashboardActivity.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        AlphaAnimation buttonClick= new AlphaAnimation(1F,0.6F);
        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(AdmindashboardActivity.this)) {
                    v.startAnimation(buttonClick);
                    progressBar.setVisibility(View.VISIBLE);
                    btn_student.setVisibility(View.GONE);
                    String role = "(Student)";

                    Intent intent = new Intent(getApplicationContext(), RecyclerActivity.class);
                    intent.putExtra("role", "(Student)");
                    progressBar.setVisibility(View.GONE);
                    btn_student.setVisibility(View.VISIBLE);
                    startActivity(intent);
                }else{
                    Toast.makeText(AdmindashboardActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(AdmindashboardActivity.this)) {
                v.startAnimation(buttonClick);
                progressBar.setVisibility(View.VISIBLE);
                btn_staff.setVisibility(View.GONE);

                Intent intent = new Intent(getApplicationContext(), RecyclerActivity.class);
                intent.putExtra("role", "(Staff)");
                progressBar.setVisibility(View.GONE);
                btn_staff.setVisibility(View.VISIBLE);
//                finish();
                startActivity(intent);
            }else{
                Toast.makeText(AdmindashboardActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }
        });
        btn_cgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(AdmindashboardActivity.this)) {
                    v.startAnimation(buttonClick);

                    progressBar.setVisibility(View.VISIBLE);
                    btn_staff.setVisibility(View.GONE);

                    Intent intent = new Intent(getApplicationContext(), PasschngActivity.class);
                    progressBar.setVisibility(View.GONE);
                    btn_staff.setVisibility(View.VISIBLE);
//                finish();
                    startActivity(intent);

                }else {
                    Toast.makeText(AdmindashboardActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(AdmindashboardActivity.this)) {
                v.startAnimation(buttonClick);
                progressBar.setVisibility(View.VISIBLE);
                btn_sch.setVisibility(View.GONE);

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                progressBar.setVisibility(View.GONE);
                btn_sch.setVisibility(View.VISIBLE);
//                intent.putExtra("role", "(Student)");
//                finish();
                startActivity(intent);
            }else{
                Toast.makeText(AdmindashboardActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(AdmindashboardActivity.this)
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