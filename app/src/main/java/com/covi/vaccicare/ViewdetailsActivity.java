package com.covi.vaccicare;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ViewdetailsActivity extends AppCompatActivity {

    String rem,newdate,newdate2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdetails);

        final TextView text_aadhar = findViewById(R.id.text_aadhar);
        final TextView text_mobile = findViewById(R.id.text_mobile);
        final TextView text_name = findViewById(R.id.text_name);
        final TextView text_role = findViewById(R.id.text_role);
        final TextView text_vaccine1 = findViewById(R.id.text_vaccine1);
        final TextView text_vdate1 = findViewById(R.id.text_vdate1);
        final TextView text_vaccine2 = findViewById(R.id.text_vaccine2);
        final TextView text_vdate2 = findViewById(R.id.text_vdate2);
        final TextView text_vstatus = findViewById(R.id.text_vstatus);
        final TextView text_clsdiv = findViewById(R.id.text_cls);
        final TextView text_dist = findViewById(R.id.text_dist);
        final TextView text_cmp = findViewById(R.id.text_cmp);
        final TextView text_cmpname = findViewById(R.id.text_cmpname);
        final TextView text_ward = findViewById(R.id.text_ward);
        final TextView clsT = findViewById(R.id.clsT);
        final TextView Rem = findViewById(R.id.reminder);
        final TextView Rem2 = findViewById(R.id.reminder2);


        if (!isNetworkAvailable(ViewdetailsActivity.this)) {
            Toast.makeText(ViewdetailsActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        String aadhar = getIntent().getStringExtra("db_aadhar");
        String mobile = getIntent().getStringExtra("db_mobile");
        String name = getIntent().getStringExtra("db_name");
        String role = getIntent().getStringExtra("db_role");
        String vaccine1 = getIntent().getStringExtra("db_vaccine1");
        String vdate1 = getIntent().getStringExtra("db_vdate1");
        String vaccine2 = getIntent().getStringExtra("db_vaccine2");
        String vdate2 = getIntent().getStringExtra("db_vdate2");
        String vstatus = getIntent().getStringExtra("db_vstatus");
        String clsdiv = getIntent().getStringExtra("db_clsdiv");
        String dist = getIntent().getStringExtra("db_dist");
        String cmp = getIntent().getStringExtra("db_cmp");
        String cmpname = getIntent().getStringExtra("db_cmpname");
        String ward = getIntent().getStringExtra("db_ward");

        if(vaccine1.equals("---")  && vaccine2.equals("---") ){
           rem = "Get Vaccinated Soon!";
        }else if(!vaccine1.equals("---") && !vaccine2.equals("---")){
            Rem2.setText("Booster Dose");
            Rem2.setVisibility(View.VISIBLE);
            newdate = addDay(vdate2,277);
            rem = "Due date "+newdate+" !";
        }else if(!vaccine1.equals("---") && vaccine2.equals("---")) {
            Rem2.setText("Second Dose");
            Rem2.setVisibility(View.VISIBLE);
            if(vaccine1.equals("Covaxin")){
                newdate2 = addDay(vdate1,28);
            }else if(vaccine1.equals("Covishield")){
                newdate2 = addDay(vdate1,84);
            }
            rem = "Due date "+newdate2+" !";
        }


        text_aadhar.setText(aadhar);
        text_mobile.setText(mobile);
        text_name.setText(name);
        text_role.setText(role);
        text_vaccine1.setText(vaccine1);
        text_vdate1.setText(vdate1);
        text_vaccine2.setText(vaccine2);
        text_vdate2.setText(vdate2);
        text_vstatus.setText(vstatus);
        text_clsdiv.setText(clsdiv);
        text_dist.setText(dist);
        text_cmp.setText(cmp+" :");
        text_cmpname.setText(cmpname);
        text_ward.setText(ward);
        Rem.setText(rem);
        if(role.equals("(Student)")){
            clsT.setVisibility(View.VISIBLE);
            text_clsdiv.setVisibility(View.VISIBLE);
        }
    }

    public static String addDay(String oldDate, int numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_YEAR,numberOfDays);
        dateFormat=new SimpleDateFormat("dd-MMM-yyyy");
        Date newDate=new Date(c.getTimeInMillis());
        String resultDate=dateFormat.format(newDate);
        return resultDate;
    }

    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false; }
}

