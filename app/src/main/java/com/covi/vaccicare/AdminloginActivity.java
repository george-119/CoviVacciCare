package com.covi.vaccicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminloginActivity extends AppCompatActivity {
    String db_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        EditText inputpass = findViewById(R.id.inputpass);
        TextInputLayout input = findViewById(R.id.inputpassTI);
        Button btn_pass = findViewById(R.id.buttonpass);

        ProgressBar progressBar = findViewById(R.id.progressBarr);

        if (!isNetworkAvailable(AdminloginActivity.this)) {
            Toast.makeText(AdminloginActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_password = snapshot.child("admin").child("password").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlphaAnimation buttonClick= new AlphaAnimation(1F,0.6F);
        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(AdminloginActivity.this)) {
                    v.startAnimation(buttonClick);

                    progressBar.setVisibility(View.VISIBLE);
                    btn_pass.setVisibility(View.GONE);

                    if (!inputpass.getText().toString().isEmpty()) {
                        if (inputpass.getText().toString().equals(db_password)) {
                            Intent intent = new Intent(getApplicationContext(), AdmindashboardActivity.class);
                            progressBar.setVisibility(View.GONE);
                            btn_pass.setVisibility(View.VISIBLE);
                            finish();
                            startActivity(intent);
                        } else {
                            input.setError("Incorrect Password");
                            progressBar.setVisibility(View.GONE);
                            btn_pass.setVisibility(View.VISIBLE);
                        }
                    } else {
                        input.setError("Enter Password");
                        progressBar.setVisibility(View.GONE);
                        btn_pass.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(AdminloginActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false; }
}