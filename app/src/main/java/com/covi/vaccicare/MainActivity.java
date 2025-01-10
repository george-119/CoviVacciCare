package com.covi.vaccicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    private static int SPLASH_SCREEN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = findViewById(R.id.progressBar);

        progressBar.getProgress();



    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),SendotpActivity.class);
            startActivity(intent);
            finish();
        }
    }
                , SPLASH_SCREEN);
}
}