package com.covi.vaccicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SendotpActivity extends AppCompatActivity{

    private static final int STORAGE_PERMISSION_CODE = 101;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendotp);

        mAuth = FirebaseAuth.getInstance();

        final EditText inputMobile = findViewById(R.id.inputMobile);
        Button buttonGetOTP = findViewById(R.id.buttonGetOTP);
        TextView adminlogin = findViewById(R.id.adminlogin);

        final ProgressBar progressBar = findViewById(R.id.progressBar);
//        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
//        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.POST_NOTIFICATIONS,STORAGE_PERMISSION_CODE);

        if (!isNetworkAvailable(SendotpActivity.this)) {
            Toast.makeText(SendotpActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }


        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            private AlphaAnimation buttonClick= new AlphaAnimation(1F,0.6F);
            @Override
            public void onClick(View v){
                if (isNetworkAvailable(SendotpActivity.this)) {
                    v.startAnimation(buttonClick);
                    if (TextUtils.isEmpty(inputMobile.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                    } else {
                        String phone = "+91" + inputMobile.getText().toString();
                        sendVerificationCode(phone);
                        progressBar.setVisibility(View.VISIBLE);
                        buttonGetOTP.setVisibility(View.INVISIBLE);
                    }
                }else{
                    Toast.makeText(SendotpActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlphaAnimation buttonClick= new AlphaAnimation(1F,0.6F);
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent = new Intent(SendotpActivity.this,AdminloginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Button buttonGetOTP = findViewById(R.id.buttonGetOTP);
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            buttonGetOTP.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            final EditText inputMobile = findViewById(R.id.inputMobile);
            verificationId = s;
            Button buttonGetOTP = findViewById(R.id.buttonGetOTP);
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            buttonGetOTP.setVisibility(View.VISIBLE);
            String key="+91"+inputMobile.getText().toString();
            Intent inten = new Intent(getApplicationContext(), VerifyotpActivity.class);
            inten.putExtra("verificationId", verificationId);
            inten.putExtra("mobile", inputMobile.getText().toString());
            inten.putExtra("key", key);
            inputMobile.setText("");
            startActivity(inten);

        }
    };



    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(SendotpActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SendotpActivity.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(SendotpActivity.this,"Notification Permission Granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SendotpActivity.this,"Notification Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == STORAGE_PERMISSION_CODE){
//            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(SendotpActivity.this,"Storage Permission Granted",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(SendotpActivity.this,"Storage Permission Denied",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false; }


}