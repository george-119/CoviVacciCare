package com.covi.vaccicare;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.AudioAttributes;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AdminviewActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String CHANNEL_ID = "CH1";
    String db_aadhar,db_mobile,db_name,db_role,db_vaccine1,db_vdate1,db_vaccine2,db_vdate2,db_vstatus,db_clsdiv,db_dist,db_cmp,db_cmpname,db_ward;
    String rem,newdate,newdate2;
    Bitmap bmp, scaledbmp;
    int pageWidth = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminview);
        String value = getIntent().getStringExtra("value");

        CardView card1 = findViewById(R.id.cardView2);
        CardView card2 = findViewById(R.id.cardView7);
        card1.setVisibility(View.GONE);
        card2.setVisibility(View.GONE);
        ProgressBar progress = findViewById(R.id.progressview);
        final TextView text_aadhar = findViewById(R.id.text_aadhar1);
        final TextView text_mobile = findViewById(R.id.text_mobile1);
        final TextView text_name = findViewById(R.id.text_name1);
        final TextView text_role = findViewById(R.id.text_role1);
        final TextView text_vaccine1 = findViewById(R.id.text_vaccine11);
        final TextView text_vdate1 = findViewById(R.id.text_vdate11);
        final TextView text_vaccine2 = findViewById(R.id.text_vaccine21);
        final TextView text_vdate2 = findViewById(R.id.text_vdate21);
        final TextView text_vstatus = findViewById(R.id.text_vstatus1);
        final TextView text_clsdiv = findViewById(R.id.text_cls1);
        final TextView text_dist = findViewById(R.id.text_dist1);
        final TextView text_cmp = findViewById(R.id.text_cmp1);
        final TextView text_cmpname = findViewById(R.id.text_cmpname1);
        final TextView text_ward = findViewById(R.id.text_ward1);
        final TextView clsT = findViewById(R.id.clsT2);
        final TextView Rem = findViewById(R.id.areminder);
        final TextView Rem2 = findViewById(R.id.areminder2);

        if (!isNetworkAvailable(AdminviewActivity.this)) {
            Toast.makeText(AdminviewActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        Button print = findViewById(R.id.btn_print1);
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp,800,250,false);
        print.setOnClickListener(v -> {
            if (isNetworkAvailable(AdminviewActivity.this)) {
                if (db_role.equals("(Student)")) {
                    createPDF();
                } else {
                    createPDFS();
                }
            }else{
                Toast.makeText(AdminviewActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccinedetails");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_aadhar = snapshot.child(value).child("aadhar").getValue().toString();
                db_mobile = snapshot.child(value).child("mobile").getValue().toString();
                db_name = snapshot.child(value).child("name").getValue().toString();
                db_role = snapshot.child(value).child("role").getValue().toString();
                db_vaccine1 = snapshot.child(value).child("vaccine1").getValue().toString();
                db_vdate1 = snapshot.child(value).child("vdate1").getValue().toString();
                db_vaccine2 = snapshot.child(value).child("vaccine2").getValue().toString();
                db_vdate2 = snapshot.child(value).child("vdate2").getValue().toString();
                db_vstatus = snapshot.child(value).child("vstatus").getValue().toString();
                db_clsdiv = snapshot.child(value).child("clsdiv").getValue().toString();
                db_dist = snapshot.child(value).child("dist").getValue().toString();
                db_cmp = snapshot.child(value).child("cmp").getValue().toString();
                db_cmpname = snapshot.child(value).child("cmpname").getValue().toString();
                db_ward = snapshot.child(value).child("ward").getValue().toString();

                if(db_vaccine1.equals("---")  && db_vaccine2.equals("---") ){
                    rem = "";
                }else if(!db_vaccine1.equals("---") && !db_vaccine2.equals("---")){
                    Rem2.setText("Booster Dose");
                    Rem2.setVisibility(View.VISIBLE);
                    newdate = addDay(db_vdate2,277);
                    rem = "Due date "+newdate+" !";
                }else if(!db_vaccine1.equals("---") && db_vaccine2.equals("---")) {
                    Rem2.setText("Second Dose");
                    Rem2.setVisibility(View.VISIBLE);
                    if(db_vaccine1.equals("Covaxin")){
                        newdate2 = addDay(db_vdate1,28);
                    }else if(db_vaccine1.equals("Covishield")){
                        newdate2 = addDay(db_vdate1,84);
                    }
                    rem = "Due date "+newdate2+" !";
                }

                text_aadhar.setText(db_aadhar);
                text_mobile.setText(db_mobile);
                text_name.setText(db_name);
                text_role.setText(db_role);
                text_vaccine1.setText(db_vaccine1);
                text_vdate1.setText(db_vdate1);
                text_vaccine2.setText(db_vaccine2);
                text_vdate2.setText(db_vdate2);
                text_vstatus.setText(db_vstatus);
                text_clsdiv.setText(db_clsdiv);
                text_dist.setText(db_dist);
                text_cmp.setText(db_cmp+" :");
                text_cmpname.setText(db_cmpname);
                text_ward.setText(db_ward);
                Rem.setText(rem);
                if(db_role.equals("(Student)")){
                    clsT.setVisibility(View.VISIBLE);
                    text_clsdiv.setVisibility(View.VISIBLE);
                }
                card1.setVisibility(View.VISIBLE);
                card2.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void createPDFS() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PdfDocument myPdfDocument = new PdfDocument();
            Paint myPaint = new Paint();
            Paint titlePaint = new Paint();
            Paint line11 = new Paint();
            Paint line1 = new Paint();
            Paint line2 = new Paint();
            Paint line3 = new Paint();
            Paint line5 = new Paint();
            Paint line6 = new Paint();
            Paint line7 = new Paint();
            Paint line8 = new Paint();
            Paint line9 = new Paint();
            Paint line10 = new Paint();
            Paint line12 = new Paint();
            Paint name = new Paint();
            Paint mob = new Paint();
            Paint adr = new Paint();
            Paint dist = new Paint();
            Paint locb = new Paint();
            Paint ward = new Paint();
            Paint vac1 = new Paint();
            Paint vac2 = new Paint();
            Paint tms = new Paint();
            Paint vsts = new Paint();
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date);

            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
            PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
            Canvas canvas = myPage1.getCanvas();

            canvas.drawBitmap(scaledbmp,200,0 ,myPaint);

            line1.setColor(Color.rgb(150,150,150));
            line1.setTextAlign(Paint.Align.CENTER);
            line1.setTextSize(45);
            canvas.drawText("________________________________________________" +
                    "_____________________________________________________________" +
                    "________________________________________________",0,260,line1);

            line11.setColor(Color.rgb(150,150,150));
            line11.setTextAlign(Paint.Align.CENTER);
            line11.setTextSize(45);
            canvas.drawText("________________________________________________" +
                    "_____________________________________________________________" +
                    "________________________________________________",0,270,line11);

            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setColor(Color.rgb(0,4,122));
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(55);
            canvas.drawText("Vaccination Details",pageWidth/2,360,titlePaint);

            myPaint.setColor(Color.rgb(222,8,8));
            myPaint.setTextAlign(Paint.Align.CENTER);
            myPaint.setTextSize(45);
            canvas.drawText(db_role,pageWidth/2,450,myPaint);

            line2.setColor(Color.rgb(150,150,150));
            line2.setTextAlign(Paint.Align.CENTER);
            line2.setTextSize(45);
            canvas.drawText("_________________________________________________",pageWidth/2,600,line2);

            name.setColor(Color.rgb(0,0,0));
            name.setTextAlign(Paint.Align.LEFT);
            name.setTextSize(35);
            canvas.drawText("Name              :    "+db_name,pageWidth/5,665,name);

            line3.setColor(Color.rgb(150,150,150));
            line3.setTextAlign(Paint.Align.CENTER);
            line3.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,700,line3);

            mob.setColor(Color.rgb(0,0,0));
            mob.setTextAlign(Paint.Align.LEFT);
            mob.setTextSize(35);
            canvas.drawText("Mobile            :    "+db_mobile,pageWidth/5,765,mob);

            line5.setColor(Color.rgb(150,150,150));
            line5.setTextAlign(Paint.Align.CENTER);
            line5.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,800,line5);

            adr.setColor(Color.rgb(0,0,0));
            adr.setTextAlign(Paint.Align.LEFT);
            adr.setTextSize(35);
            canvas.drawText("Aadhar            :    "+db_aadhar,pageWidth/5,865,adr);

            line6.setColor(Color.rgb(150,150,150));
            line6.setTextAlign(Paint.Align.CENTER);
            line6.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,900,line6);

            dist.setColor(Color.rgb(0,0,0));
            dist.setTextAlign(Paint.Align.LEFT);
            dist.setTextSize(35);
            canvas.drawText("District           :    "+db_dist,pageWidth/5,965,dist);

            line7.setColor(Color.rgb(150,150,150));
            line7.setTextAlign(Paint.Align.CENTER);
            line7.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1000,line7);

            locb.setColor(Color.rgb(0,0,0));
            locb.setTextAlign(Paint.Align.LEFT);
            locb.setTextSize(35);
            canvas.drawText(db_cmp+"   :    "+db_cmpname,pageWidth/5,1065,locb);

            line8.setColor(Color.rgb(150,150,150));
            line8.setTextAlign(Paint.Align.CENTER);
            line8.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1100,line8);

            ward.setColor(Color.rgb(0,0,0));
            ward.setTextAlign(Paint.Align.LEFT);
            ward.setTextSize(35);
            canvas.drawText("Ward               :    "+db_ward,pageWidth/5,1165,ward);

            line9.setColor(Color.rgb(150,150,150));
            line9.setTextAlign(Paint.Align.CENTER);
            line9.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1200,line9);

            vac1.setColor(Color.rgb(0,0,0));
            vac1.setTextAlign(Paint.Align.LEFT);
            vac1.setTextSize(35);
            canvas.drawText("Dose 1            :    "+db_vaccine1+" - "+db_vdate1,pageWidth/5,1265,vac1);

            line10.setColor(Color.rgb(150,150,150));
            line10.setTextAlign(Paint.Align.CENTER);
            line10.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1300,line10);

            vac2.setColor(Color.rgb(0,0,0));
            vac2.setTextAlign(Paint.Align.LEFT);
            vac2.setTextSize(35);
            canvas.drawText("Dose 2            :    "+db_vaccine2+" - "+db_vdate2,pageWidth/5,1365,vac2);

            line12.setColor(Color.rgb(150,150,150));
            line12.setTextAlign(Paint.Align.CENTER);
            line12.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1400,line12);

            vsts.setColor(Color.rgb(222,8,8));
            vsts.setTextAlign(Paint.Align.CENTER);
            vsts.setTextSize(30);
            canvas.drawText("Status : "+db_vstatus,pageWidth/2,1700,vsts);

            tms.setColor(Color.rgb(128,128,128));
            tms.setTextAlign(Paint.Align.RIGHT);
            tms.setTextSize(25);
            canvas.drawText("Printed on "+timeStamp,1100,1950,tms);

            myPdfDocument.finishPage(myPage1);

            savePDF(myPdfDocument);

        }
    }

    private void createPDF(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PdfDocument myPdfDocument = new PdfDocument();
            Paint myPaint = new Paint();
            Paint titlePaint = new Paint();
            Paint line11 = new Paint();
            Paint line1 = new Paint();
            Paint line2 = new Paint();
            Paint line3 = new Paint();
            Paint line4 = new Paint();
            Paint line5 = new Paint();
            Paint line6 = new Paint();
            Paint line7 = new Paint();
            Paint line8 = new Paint();
            Paint line9 = new Paint();
            Paint line10 = new Paint();
            Paint line12 = new Paint();
            Paint name = new Paint();
            Paint mob = new Paint();
            Paint cls = new Paint();
            Paint adr = new Paint();
            Paint dist = new Paint();
            Paint locb = new Paint();
            Paint ward = new Paint();
            Paint vac1 = new Paint();
            Paint vac2 = new Paint();
            Paint tms = new Paint();
            Paint vsts = new Paint();
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date);

            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
            PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
            Canvas canvas = myPage1.getCanvas();

            canvas.drawBitmap(scaledbmp,200,0 ,myPaint);

            line1.setColor(Color.rgb(150,150,150));
            line1.setTextAlign(Paint.Align.CENTER);
            line1.setTextSize(45);
            canvas.drawText("________________________________________________" +
                    "_____________________________________________________________" +
                    "________________________________________________",0,260,line1);

            line11.setColor(Color.rgb(150,150,150));
            line11.setTextAlign(Paint.Align.CENTER);
            line11.setTextSize(45);
            canvas.drawText("________________________________________________" +
                    "_____________________________________________________________" +
                    "________________________________________________",0,270,line11);

            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setColor(Color.rgb(0,4,122));
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(55);
            canvas.drawText("Vaccination Details",pageWidth/2,360,titlePaint);

            myPaint.setColor(Color.rgb(222,8,8));
            myPaint.setTextAlign(Paint.Align.CENTER);
            myPaint.setTextSize(45);
            canvas.drawText(db_role,pageWidth/2,450,myPaint);

            line2.setColor(Color.rgb(150,150,150));
            line2.setTextAlign(Paint.Align.CENTER);
            line2.setTextSize(45);
            canvas.drawText("_________________________________________________",pageWidth/2,600,line2);

            name.setColor(Color.rgb(0,0,0));
            name.setTextAlign(Paint.Align.LEFT);
            name.setTextSize(35);
            canvas.drawText("Name              :    "+db_name,pageWidth/5,665,name);

            line3.setColor(Color.rgb(150,150,150));
            line3.setTextAlign(Paint.Align.CENTER);
            line3.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,700,line3);

            cls.setColor(Color.rgb(0,0,0));
            cls.setTextAlign(Paint.Align.LEFT);
            cls.setTextSize(35);
            canvas.drawText("Class              :    "+db_clsdiv,pageWidth/5,765,cls);

            line4.setColor(Color.rgb(150,150,150));
            line4.setTextAlign(Paint.Align.CENTER);
            line4.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,800,line4);

            mob.setColor(Color.rgb(0,0,0));
            mob.setTextAlign(Paint.Align.LEFT);
            mob.setTextSize(35);
            canvas.drawText("Mobile            :    "+db_mobile,pageWidth/5,865,mob);

            line5.setColor(Color.rgb(150,150,150));
            line5.setTextAlign(Paint.Align.CENTER);
            line5.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,900,line5);

            adr.setColor(Color.rgb(0,0,0));
            adr.setTextAlign(Paint.Align.LEFT);
            adr.setTextSize(35);
            canvas.drawText("Aadhar            :    "+db_aadhar,pageWidth/5,965,adr);

            line6.setColor(Color.rgb(150,150,150));
            line6.setTextAlign(Paint.Align.CENTER);
            line6.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1000,line6);

            dist.setColor(Color.rgb(0,0,0));
            dist.setTextAlign(Paint.Align.LEFT);
            dist.setTextSize(35);
            canvas.drawText("District           :    "+db_dist,pageWidth/5,1065,dist);

            line7.setColor(Color.rgb(150,150,150));
            line7.setTextAlign(Paint.Align.CENTER);
            line7.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1100,line7);

            locb.setColor(Color.rgb(0,0,0));
            locb.setTextAlign(Paint.Align.LEFT);
            locb.setTextSize(35);
            canvas.drawText(db_cmp+"   :    "+db_cmpname,pageWidth/5,1165,locb);

            line8.setColor(Color.rgb(150,150,150));
            line8.setTextAlign(Paint.Align.CENTER);
            line8.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1200,line8);

            ward.setColor(Color.rgb(0,0,0));
            ward.setTextAlign(Paint.Align.LEFT);
            ward.setTextSize(35);
            canvas.drawText("Ward               :    "+db_ward,pageWidth/5,1265,ward);

            line9.setColor(Color.rgb(150,150,150));
            line9.setTextAlign(Paint.Align.CENTER);
            line9.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1300,line9);

            vac1.setColor(Color.rgb(0,0,0));
            vac1.setTextAlign(Paint.Align.LEFT);
            vac1.setTextSize(35);
            canvas.drawText("Dose 1            :    "+db_vaccine1+" - "+db_vdate1,pageWidth/5,1365,vac1);

            line10.setColor(Color.rgb(150,150,150));
            line10.setTextAlign(Paint.Align.CENTER);
            line10.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1400,line10);

            vac2.setColor(Color.rgb(0,0,0));
            vac2.setTextAlign(Paint.Align.LEFT);
            vac2.setTextSize(35);
            canvas.drawText("Dose 2            :    "+db_vaccine2+" - "+db_vdate2,pageWidth/5,1465,vac2);

            line12.setColor(Color.rgb(150,150,150));
            line12.setTextAlign(Paint.Align.CENTER);
            line12.setTextSize(45);
            canvas.drawText("|_________________________________________________|",pageWidth/2,1500,line12);

            vsts.setColor(Color.rgb(222,8,8));
            vsts.setTextAlign(Paint.Align.CENTER);
            vsts.setTextSize(30);
            canvas.drawText("Status : "+db_vstatus,pageWidth/2,1700,vsts);

            tms.setColor(Color.rgb(128,128,128));
            tms.setTextAlign(Paint.Align.RIGHT);
            tms.setTextSize(25);
            canvas.drawText("Printed on "+timeStamp,1100,1950,tms);

            myPdfDocument.finishPage(myPage1);

            savePDF(myPdfDocument);

        }
    }

    public void savePDF(PdfDocument myPdfDocument){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),db_name +"_"+db_clsdiv+ ".pdf");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                myPdfDocument.writeTo(new FileOutputStream(file));
            }
            Toast.makeText(AdminviewActivity.this, "Downloading to system/emulated/0/Downloads/",Toast.LENGTH_SHORT).show();
            createNotificationChannel();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(AdminviewActivity.this,""+e,Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(AdminviewActivity.this,""+e,Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            myPdfDocument.close();
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

    private void createNotificationChannel() {

        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Download Complete")
                .setContentText("Downloaded "+db_name+db_role+".pdf to Downloads")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            CharSequence channel_name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( CHANNEL_ID , channel_name , importance) ;
            notificationChannel.setDescription(description);
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            builder.setChannelId(CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis (), builder.build()) ;

    }

//    private boolean checkPermission() {
//        // checking of permissions.
//
//
//        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
//        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
//        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermission() {
//        // requesting permissions if not provided.
//        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0) {
//
//                // after requesting permissions we are showing
//                // users a toast message of permission granted.
//                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//
//                if (writeStorage && readStorage) {
//                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        }
//    }
}