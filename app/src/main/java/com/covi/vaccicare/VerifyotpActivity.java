package com.covi.vaccicare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class VerifyotpActivity extends AppCompatActivity {

    private EditText inputcode1,inputcode2,inputcode3,inputcode4,inputcode5,inputcode6;
    private String verificationId;
    private FirebaseAuth mAuth;
    public  int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);

        mAuth = FirebaseAuth.getInstance();

        final TextView counttime=findViewById(R.id.counttime);
        TextView textMobile = findViewById(R.id.textMobile);
        TextView question = findViewById(R.id.question);
        TextView textresendOTP = findViewById(R.id.textResendOTP);
        textMobile.setText(String.format("enter the OTP sent to +91%s", getIntent().getStringExtra("mobile")));

        inputcode1 = findViewById(R.id.inputcode1);
        inputcode2 = findViewById(R.id.inputcode2);
        inputcode3 = findViewById(R.id.inputcode3);
        inputcode4 = findViewById(R.id.inputcode4);
        inputcode5 = findViewById(R.id.inputcode5);
        inputcode6 = findViewById(R.id.inputcode6);

        setupOTPinputs();

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button buttonVerify = findViewById(R.id.buttonVerify);

        verificationId = getIntent().getStringExtra("verificationId");

        if (!isNetworkAvailable(VerifyotpActivity.this)) {
            Toast.makeText(VerifyotpActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                counttime.setText(String.valueOf(counter)+" s");
                counter++;

            }

            @Override
            public void onFinish() {
                question.setVisibility(View.VISIBLE);
                textresendOTP.setVisibility(View.VISIBLE);

            }
        }.start();

        buttonVerify.setOnClickListener(new View.OnClickListener() {
                private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
                @Override
                public void onClick (View v){
                    if (isNetworkAvailable(VerifyotpActivity.this)) {
                v.startAnimation(buttonClick);

                if (inputcode1.getText().toString().trim().isEmpty()
                        || inputcode2.getText().toString().trim().isEmpty()
                        || inputcode3.getText().toString().trim().isEmpty()
                        || inputcode4.getText().toString().trim().isEmpty()
                        || inputcode5.getText().toString().trim().isEmpty()
                        || inputcode6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }

                String code =
                        inputcode1.getText().toString() +
                                inputcode2.getText().toString() +
                                inputcode3.getText().toString() +
                                inputcode4.getText().toString() +
                                inputcode5.getText().toString() +
                                inputcode6.getText().toString();

                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonVerify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String mobile = getIntent().getStringExtra("mobile");
                                        String key = getIntent().getStringExtra("key");
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
                                        Query checkmobile = reference.orderByChild("mobile").equalTo(mobile);
                                        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                    intent.putExtra("mobile", mobile);
                                                    intent.putExtra("key", key);
                                                    progressBar.setVisibility(View.GONE);
                                                    buttonVerify.setVisibility(View.VISIBLE);
                                                    finish();
                                                    startActivity(intent);
                                                } else {
                                                    new AlertDialog.Builder(VerifyotpActivity.this)
                                                            .setTitle("Your mobile number is not registered yet").setCancelable(false)
                                                            .setPositiveButton("Register now", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
//                                                                   finish();
                                                                    String mobile = getIntent().getStringExtra("mobile");
                                                                    String key = getIntent().getStringExtra("key");
                                                                    Intent intent = new Intent(getApplicationContext(), DesignationActivity.class);
                                                                    intent.putExtra("mobile", mobile);
                                                                    intent.putExtra("key", key);
                                                                    progressBar.setVisibility(View.GONE);
                                                                    buttonVerify.setVisibility(View.VISIBLE);
                                                                    finish();
                                                                    startActivity(intent);
                                                                }
                                                            }).show();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        buttonVerify.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }else{
                        Toast.makeText(VerifyotpActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            }
        });

        textresendOTP.setOnClickListener(new View.OnClickListener() {
            private AlphaAnimation buttonClick= new AlphaAnimation(5F,0.6F);
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                new CountDownTimer(61000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        counttime.setText(String.valueOf(counter)+" s");
                        counter++;

                    }

                    @Override
                    public void onFinish() {
//                counttime.setVisibility(View.INVISIBLE);
                        question.setVisibility(View.VISIBLE);
                        textresendOTP.setVisibility(View.VISIBLE);

                    }
                }.start();

                String phone ="+91" + getIntent().getStringExtra("mobile");
                sendVerificationCode(phone);
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
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            verificationId = newVerificationId;
            Toast.makeText(getApplicationContext(),"OTP sent successfully",Toast.LENGTH_SHORT).show();


        }
    };

    private  void setupOTPinputs(){
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 1){
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) {
                    inputcode1.requestFocus(View.FOCUS_DOWN);
                }
                    if (s.length() == 1) {
                        inputcode3.requestFocus(View.FOCUS_DOWN);
                    }
                }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) {
                    inputcode2.requestFocus(View.FOCUS_DOWN);
                }
                    if (s.length() == 1) {
                        inputcode4.requestFocus(View.FOCUS_DOWN);
                    }
                }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) {
                    inputcode3.requestFocus(View.FOCUS_DOWN);
                }
                    if (s.length() == 1) {
                        inputcode5.requestFocus(View.FOCUS_DOWN);
                    }
                }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) {
                    inputcode4.requestFocus(View.FOCUS_DOWN);
                }
                    if (s.length() == 1) {
                        inputcode6.requestFocus(View.FOCUS_DOWN);
                    }
                }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) {
                    inputcode5.requestFocus(View.FOCUS_DOWN);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

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