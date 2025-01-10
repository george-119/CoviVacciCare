package com.covi.vaccicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.database.ValueEventListener;

public class PasschngActivity extends AppCompatActivity {
    String db_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passchng);

        EditText oldpass = findViewById(R.id.oldpass);
        TextInputLayout old = findViewById(R.id.oldpassTI);
        EditText newpass = findViewById(R.id.newpass);
        TextInputLayout newp = findViewById(R.id.newpassTI);
        EditText cnfrmpass = findViewById(R.id.cnfrmnewpass);
        TextInputLayout cfm = findViewById(R.id.cnfrmnewpassTI);

        Button btn = findViewById(R.id.btnchgpass);
        ProgressBar progressBar = findViewById(R.id.prog);

        if (!isNetworkAvailable(PasschngActivity.this)) {
            Toast.makeText(PasschngActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        DAOAdmin dao = new DAOAdmin();

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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(PasschngActivity.this)) {
                    v.startAnimation(buttonClick);

                    progressBar.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.GONE);

                    if (newpass.getText().toString().length()>=6 ){
                        if (newpass.getText().toString().equals(cnfrmpass.getText().toString())) {
                            if (oldpass.getText().toString().equals(db_password)) {
                                Admin ad = new Admin(newpass.getText().toString());
                                dao.add("admin", ad).addOnSuccessListener(suc ->
                                {
                                    Toast.makeText(PasschngActivity.this, "Password Changed", Toast.LENGTH_SHORT)
                                            .show();
                                    old.setError("");
                                    cfm.setError("");
                                    newp.setError("");
                                    progressBar.setVisibility(View.GONE);
                                    btn.setVisibility(View.VISIBLE);
                                    finish();
                                }).addOnFailureListener(er ->
                                {
                                    Toast.makeText(PasschngActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT)
                                            .show();
                                    progressBar.setVisibility(View.GONE);
                                    btn.setVisibility(View.VISIBLE);
                                });

                            } else {
                                old.setError("Incorrect Password");
                                cfm.setError("");
                                progressBar.setVisibility(View.GONE);
                                btn.setVisibility(View.VISIBLE);
                            }

                        } else {
                            cfm.setError("Passwords didn't match");
                            newp.setError("");
                            progressBar.setVisibility(View.GONE);
                            btn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        newp.setError("Enter password of minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                        btn.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(PasschngActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
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