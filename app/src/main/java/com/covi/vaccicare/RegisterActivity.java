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

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog
        .OnDateSetListener {

    String Vaccine1, Vaccine2, Vstatus,Clsdiv,Cmp,Dist,Dose;
    EditText vdate1,vdate2;
    private boolean startDateOrEndDAte = true;
    ArrayAdapter<String> arrayAdapter1,arrayAdapter2,arrayAdapter3,arrayAdapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText aadhar1 = findViewById(R.id.edit_aadhar1);
        final EditText aadhar2 = findViewById(R.id.edit_aadhar2);
        final EditText aadhar3 = findViewById(R.id.edit_aadhar3);
        final EditText edit_name = findViewById(R.id.edit_name);
        final TextView clsT = findViewById(R.id.clsT);
        final EditText cls = findViewById(R.id.edit_cls);
        final TextView divT = findViewById(R.id.divT);
        final EditText div = findViewById(R.id.edit_div);
        final EditText cmpname = findViewById(R.id.edit_cmpname);
        final EditText ward = findViewById(R.id.edit_ward);
        final Spinner cmp = findViewById(R.id.spinner_cmp);
        final Spinner dist = findViewById(R.id.spinner_dist);
        final Spinner spinner1 = findViewById(R.id.spinner_vaccine1);
        spinner1.setEnabled(false);
        final Spinner spinner2 = findViewById(R.id.spinner_vaccine2);
        spinner2.setEnabled(false);
        vdate1 = findViewById(R.id.v1_date);
        vdate1.setEnabled(false);
        vdate1.setText("---");
        vdate2 = findViewById(R.id.v2_date);
        vdate2.setEnabled(false);
        vdate2.setText("---");
        Switch switch1 = findViewById(R.id.switch1);
        Switch switch2 = findViewById(R.id.switch2);
        String mobile = getIntent().getStringExtra("mobile");
        String key = getIntent().getStringExtra("key");
        String role = getIntent().getStringExtra("role");
        if(role.equals("(Student)")){
            clsT.setVisibility(View.VISIBLE);
            cls.setVisibility(View.VISIBLE);
            divT.setVisibility(View.VISIBLE);
            div.setVisibility(View.VISIBLE);
        }
        Button btn = findViewById(R.id.btn_reg);
        DAOVaccinedetails dao = new DAOVaccinedetails();

        if (!isNetworkAvailable(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

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
                startDateOrEndDAte=true;
            }
        });
        vdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.covi.vaccicare.DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new com.covi.vaccicare.DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
                startDateOrEndDAte=false;
            }
        });

        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("---");
        arrayList1.add("Covaxin");
        arrayList1.add("Covishield");
        arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Vaccine1 = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("---");
        arrayList2.add("Covaxin");
        arrayList2.add("Covishield");
        arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Vaccine2 = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add("Alappuzha");
        arrayList3.add("Ernakulam");
        arrayList3.add("Idukki");
        arrayList3.add("Kannur");
        arrayList3.add("Kasargod");
        arrayList3.add("Kollam");
        arrayList3.add("Kottayam");
        arrayList3.add("Kozhikode");
        arrayList3.add("Malappuram");
        arrayList3.add("Palakkad");
        arrayList3.add("Pathanamthitta");
        arrayList3.add("Thiruvananthapuram");
        arrayList3.add("Trissur");
        arrayList3.add("Wayanad");
        arrayAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                arrayList3);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dist.setAdapter(arrayAdapter3);
        dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dist = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        ArrayList<String> arrayList4 = new ArrayList<>();
        arrayList4.add("Corporation");
        arrayList4.add("Muncipality");
        arrayList4.add("Panchayath");
        arrayAdapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                arrayList4);
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmp.setAdapter(arrayAdapter4);
        cmp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cmp = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        aadhar1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (aadhar1.getText().length() == 4){
                if (!s.toString().trim().isEmpty()) {
                    aadhar2.requestFocus();
                }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aadhar2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 3) {
                    aadhar1.requestFocus(View.FOCUS_DOWN);
                }
                if (aadhar2.getText().length() == 4){
                if (!s.toString().trim().isEmpty()) {
                    aadhar3.requestFocus();
                }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aadhar3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 3) {
                    aadhar2.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn.setOnClickListener(v ->
        {
            if (isNetworkAvailable(RegisterActivity.this)) {
            if(edit_name.getText().toString().isEmpty() ){
                Toast.makeText(RegisterActivity.this, "Fill the Mandatory!",
                        Toast.LENGTH_SHORT).show();
            }
            else if( role.equals("(Student)") && cls.getText().toString().isEmpty() ) {
                Toast.makeText(RegisterActivity.this, "Class field is empty!",
                        Toast.LENGTH_SHORT).show();
            }else if( role.equals("(Student)") && div.getText().toString().isEmpty() ) {
                Toast.makeText(RegisterActivity.this, "Division field is empty!",
                        Toast.LENGTH_SHORT).show();
            } else if( aadhar1.getText().toString().isEmpty() || aadhar2.getText().toString()
                    .isEmpty() || aadhar3.getText().toString().isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Ensure Aadhar No. is correct",
                        Toast.LENGTH_SHORT).show();
            }else if(cmpname.getText().toString().isEmpty() || ward.getText().toString().isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Local Body or Ward is missing",
                        Toast.LENGTH_SHORT).show();
            } else
                {
                if (Vaccine1.equals("---") && !Vaccine2.equals("---")) {
                    Toast.makeText(RegisterActivity.this, "Please register First Dose",
                            Toast.LENGTH_LONG).show();
                } else {
                    String aadhar = aadhar1.getText().toString() + " " + aadhar2.getText()
                            .toString() + " " + aadhar3.getText().toString();
                    if (Vaccine1.equals("---") && Vaccine2.equals("---")) {
                        Vstatus = "Not Vaccinated Yet";
                        Dose = "0";

                    } else if (!Vaccine1.equals("---") && Vaccine2.equals("---")) {
                        Vstatus = "First Dose Vaccination Completed," +
                                " Second Dose Vaccination Pending";
                        Dose = "1";
                    } else if (!Vaccine1.equals("---") && !Vaccine2.equals("---")) {
                        Vstatus = "First Dose & Second Dose Vaccination Completed";
                        Dose = "" +
                                "2";
                    }


                    //code for insertion
                    Clsdiv = cls.getText().toString()+"-"+div.getText().toString();
                    Vaccinedetails vd = new Vaccinedetails(edit_name.getText().toString(), mobile,
                            role, Clsdiv, aadhar,
                            Vaccine1, vdate1.getText().toString(), Vaccine2, vdate2.getText()
                            .toString(), Vstatus,Dist,Cmp,cmpname.getText().toString(),
                            ward.getText().toString(),Dose);
                    dao.add(key,vd).addOnSuccessListener(suc ->
                    {
                        Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT)
                                .show();
                    }).addOnFailureListener(er ->
                    {
                        Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    });

                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.putExtra("name", edit_name.getText().toString());
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("key", key);
                    finish();
                    startActivity(intent);
                }
            }
            }else{
                Toast.makeText(RegisterActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
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