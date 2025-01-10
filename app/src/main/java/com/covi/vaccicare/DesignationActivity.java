package com.covi.vaccicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

public class DesignationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designation);

        Button btn_student = findViewById(R.id.btn_student);
        Button btn_staff = findViewById(R.id.btn_staff);

        String student="student";
        String staff="staff";

        String mobile= getIntent().getStringExtra("mobile");
        String key= getIntent().getStringExtra("key");
        String name = getIntent().getStringExtra("name");

        AlphaAnimation buttonClick= new AlphaAnimation(1F,0.6F);
        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                String role="(Student)";
                Intent intent = new Intent(DesignationActivity.this,RegisterActivity.class);
                intent.putExtra("role",role);
                intent.putExtra("name",name);
                intent.putExtra("mobile", mobile);
                intent.putExtra("key", key);
                finish();
                startActivity(intent);
            }
        });

        btn_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                String role="(Staff)";
                Intent intent = new Intent(DesignationActivity.this,RegisterActivity.class);
                intent.putExtra("role",role);
                intent.putExtra("name", name);
                intent.putExtra("mobile", mobile);
                intent.putExtra("key", key);
                finish();
                startActivity(intent);
            }
        });
    }
}