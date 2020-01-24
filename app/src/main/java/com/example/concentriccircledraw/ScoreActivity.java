package com.example.concentriccircledraw;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;


import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreActivity extends AppCompatActivity {
    private int temp=0;
    private int total=0;
    private Button scoreButton;
    private boolean success=false;
    private String uid;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_score);

        temp=getIntent().getIntExtra("VALUE",0);
        total=getIntent().getIntExtra("TOTAL",0);
        time=getIntent().getStringExtra("TIME");
        scoreButton=findViewById(R.id.scoreButton);

        scoreButton.setText(" "+temp+"/ "+"100");

        if(temp<75){
            showfinalDialogue(false);
        }else {
            showfinalDialogue(true);

        }

        uid=getIntent().getStringExtra("UID");
    }
    private void showfinalDialogue(boolean success) {
        // Create Alert using Builder
        if(success){
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                    .setTitle("Success. Congrats you hit the score.")
                    .setIcon(R.drawable.ic_check_circle_black_24dp)
                    .addButton("Upload data", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                            CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(uid!=null) {


                                        String date_ = "No date";
                                        DateTimeFormatter dtf = null;

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd___HH:mm:ss");
                                            LocalDateTime now = LocalDateTime.now();
                                            date_ = dtf.format(now);
                                        } else {
                                            // Change this when moving to another tab
                                        }
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Users")
                                                .child(uid.replace(".","_"))
                                                .child("Concentric_Circle_Draw")
                                                .child(date_)
                                                .setValue("Accuracy Value "+temp+"/ "+"100"+" Time Taken "+time).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    finishAffinity();
                                                    System.exit(0);
                                                }
                                            }
                                        });

                                    }else{
                                        Toast.makeText(getApplicationContext(),"Data will not be stored if app opened seperately",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

            builder.show();
        }else{
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                    .setTitle("Failed. Not satisfactory please try again. The circle should " +
                            "have same radius in all quadrants as that of given circle")
                    .setIcon(R.drawable.ic_cancel_black_24dp)

                    .addButton("Redo", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                            CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

            builder.show();


        }
    }
}
