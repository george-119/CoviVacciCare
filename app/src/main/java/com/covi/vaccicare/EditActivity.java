package com.covi.vaccicare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String  Vstatus,Vaccine1,Vaccine2,Dist,Cmp,Cmpname,Ward,Dose;
    EditText vdate1,vdate2;
    private boolean startDateOrEndDAte = true;
    ArrayAdapter<String> arrayAdapter1,arrayAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        TextView clsT = findViewById(R.id.clsT1);
        final EditText clsdiv = findViewById(R.id.edit_clsdiv1);
        clsdiv.setText(getIntent().getStringExtra("db_clsdiv"));
        final EditText aadhar1 = findViewById(R.id.edit_aadhar1);
        aadhar1.setText(getIntent().getStringExtra("db_aadhar"));
        final EditText edit_name = findViewById(R.id.edit_name1);
        edit_name.setText(getIntent().getStringExtra("db_name"));
        final Spinner spinner1 = findViewById(R.id.spinner_vaccine11);
        final Spinner spinner2 = findViewById(R.id.spinner_vaccine21);
        vdate1 = findViewById(R.id.v1_date11);
        vdate1.setText(getIntent().getStringExtra("db_vdate1"));
        vdate2 = findViewById(R.id.v2_date21);
        vdate2.setText(getIntent().getStringExtra("db_vdate2"));
        Switch switch1 = findViewById(R.id.switch11);
        Switch switch2 = findViewById(R.id.switch21);

        if (!isNetworkAvailable(EditActivity.this)) {
            Toast.makeText(EditActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        Dist = getIntent().getStringExtra("db_dist");
        Cmp = getIntent().getStringExtra("db_cmp");
        Cmpname = getIntent().getStringExtra("db_cmpname");
        Ward = getIntent().getStringExtra("db_ward");

        spinner1.setEnabled(false);
        vdate1.setEnabled(false);
        spinner2.setEnabled(false);
        vdate2.setEnabled(false);

        if (!getIntent().getStringExtra("db_vaccine1").equals("---")
                && getIntent().getStringExtra("db_vaccine2").equals("---")){
            switch1.setChecked(true);
            spinner1.setEnabled(true);
            vdate1.setEnabled(true);
        }else if(!getIntent().getStringExtra("db_vaccine1").equals("---")
                && !getIntent().getStringExtra("db_vaccine2").equals("---")){
            switch1.setChecked(true);
            switch2.setChecked(true);
            spinner1.setEnabled(true);
            vdate1.setEnabled(true);
            spinner2.setEnabled(true);
            vdate2.setEnabled(true);
        }

        String mobile = getIntent().getStringExtra("db_mobile");
        String key = getIntent().getStringExtra("key");
        String role = getIntent().getStringExtra("db_role");
        if (role.equals("(Student)")) {
            clsdiv.setVisibility(View.VISIBLE);
            clsT.setVisibility(View.VISIBLE);
        }
        Button btn = findViewById(R.id.btn_reg1);
        DAOVaccinedetails dao = new DAOVaccinedetails();

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner1.setEnabled(true);
                    vdate1.setEnabled(true);
                } else {
                    spinner1.setEnabled(false);
                    spinner1.setSelection(arrayAdapter2.getPosition("---"));
                    vdate1.setEnabled(false);
                    vdate1.setText("---");
                }
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner2.setEnabled(true);
                    vdate2.setEnabled(true);

                } else {
                    spinner2.setEnabled(false);
                    spinner2.setSelection(arrayAdapter2.getPosition("---"));
                    vdate2.setEnabled(false);
                    vdate2.setText("---");
                }
            }
        });

        vdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.covi.vaccicare.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new com.covi.vaccicare.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
                startDateOrEndDAte = true;
            }
        });
        vdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.covi.vaccicare.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new com.covi.vaccicare.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
                startDateOrEndDAte = false;
            }
        });

        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("---");
        arrayList1.add("Covaxin");
        arrayList1.add("Covishield");

        arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setSelection(arrayAdapter1.getPosition(getIntent()
                .getStringExtra("db_vaccine1")));
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vaccine1 = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("---");
        arrayList2.add("Covaxin");
        arrayList2.add("Covishield");

        arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                arrayList1);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setSelection(arrayAdapter2.getPosition(getIntent()
                .getStringExtra("db_vaccine2")));
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Vaccine2 = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        AlphaAnimation buttonClick= new AlphaAnimation(1F,0.6F);
        btn.setOnClickListener(v ->

        {
            if (isNetworkAvailable(EditActivity.this)) {

                v.startAnimation(buttonClick);
                if (edit_name.getText().toString().isEmpty()) {
                    Toast.makeText(EditActivity.this, "Name field is empty",
                            Toast.LENGTH_SHORT).show();
                } else if (role.equals("(Student)") && clsdiv.getText().toString().isEmpty()) {
                    Toast.makeText(EditActivity.this, "Class field is empty!",
                            Toast.LENGTH_SHORT).show();
                } else if (aadhar1.getText().toString().isEmpty()) {
                    Toast.makeText(EditActivity.this, "Aadhar field is empty",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (
                            Vaccine1.equals("---") &&
                                    !Vaccine2.equals("---")) {
                        Toast.makeText(EditActivity.this, "Please register First Dose",
                                Toast.LENGTH_LONG).show();
                    } else {
                        String aadhar = aadhar1.getText().toString();
                        if (Vaccine1.equals("---")
                                &&
                                Vaccine2.equals("---")
                        ) {
                            Vstatus = "Not Vaccinated Yet";
                            Dose = "0";
                        } else if (
                                !Vaccine1.equals("---")
                                        &&
                                        Vaccine2.equals("---")
                                        ||
                                        Vaccine2.isEmpty()
                        ) {
                            Vstatus =
                                    "First Dose Vaccination Completed, Second Dose Vaccination Pending";
                            Dose = "1";
                        }
                        else if (!Vaccine1.equals("---")
                                &&
                                !Vaccine2.equals("---")
                        ) {
                            Vstatus = "First Dose & Second Dose Vaccination Completed";
                            Dose = "2";
                        }
                        if (vdate1.getText().toString().isEmpty()) {
                            vdate1.setText("---");
                        }
                        if (vdate2.getText().toString().isEmpty()) {
                            vdate2.setText("---");
                        }

                        //update
                        Vaccinedetails vd = new Vaccinedetails(edit_name.getText().toString(), mobile,
                                role, clsdiv.getText().toString(), aadhar, Vaccine1,
                                vdate1.getText().toString(), Vaccine2, vdate2.getText().toString(),
                                Vstatus, Dist, Cmp, Cmpname, Ward,Dose);
                        dao.add(key, vd).addOnSuccessListener(suc ->
                        {
                            Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                        }).addOnFailureListener(er ->
                        {
                            Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        });
                    }
                }
            }else{
                Toast.makeText(EditActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date newDate=new Date(mCalendar.getTimeInMillis());
        String selectedDate = dateFormat.getDateInstance().format(newDate);
        if(startDateOrEndDAte ){
            vdate1.setText(selectedDate);
        }
        else{
            vdate2.setText(selectedDate);
        }
    }
    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false; }
}
