package com.example.concentriccircledraw;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.crowdfire.cfalertdialog.CFAlertDialog;

public class MainActivity extends AppCompatActivity {
    private Button finishButton;
    private PaintView paintView;
    private Button    refreshButton;
    private Chronometer chronometer;
    private long base;
    private boolean start=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        finishButton=(Button)findViewById(R.id.finishButton);
        paintView=(PaintView)findViewById(R.id.paintView);
        refreshButton=(Button)findViewById(R.id.refreshButton);
        chronometer=(Chronometer)findViewById(R.id.chronometer);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(paintView.greenEnded&&paintView.blueEnded&&paintView.magentaEnded&&paintView.yellowEnded) {
                    long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    float x = paintView.getTouched() / (time / 1000);
                    int marks = (int) x;
                    showfinalDialogue(marks, paintView);
                }else {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(MainActivity.this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                            .setTitle(("Circles are incomplete, please start and end each circle " +
                                    "on its respective coloured dots."))
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .addButton("Redo", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                                    CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            recreate();
                                        }
                                    });

                    builder.show();

                }
                 }
        });


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        paintView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                   if(start) {
                       chronometer.setBase(SystemClock.elapsedRealtime());
                       chronometer.start();
                       start=false;
                   }else{
                       chronometer.setBase(base);
                       chronometer.start();

                   }

                }
                else if(event.getAction()==MotionEvent.ACTION_MOVE){
                   if(paintView.isOutIndex()==true){
                       finishButton.setText("TRACED OUT");
                       finishButton.setBackgroundColor(Color.RED);
                   } else{
                       finishButton.setBackgroundColor(Color.parseColor("#D81B60"));
                       finishButton.setText("CONTINUE");
                   }
                }
                else if(event.getAction()==MotionEvent.ACTION_UP){
                        finishButton.setText("CLICK HERE IF FINISHED");
                        base=chronometer.getBase();
                        chronometer.setBase(base);
                        chronometer.stop();
                }


                return false;
            }
        });


    }

    private void showfinalDialogue(int marks, final PaintView paintView) {
        // Create Alert using Builder
        if(marks>=paintView.getSCORE()) {

            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("Well done .You are able to get trace curves, now you  can proceed " +
                            "to" +
                            " try " +
                            "concentric circles  " +
                            "yourself.")
                    .setIcon(R.drawable.ic_check_circle_black_24dp)
                    .addButton("Try your self", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                            CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainActivity.this,TryUrSelfActivity.class));
                                }
                            });

            builder.show();
        }else{
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("Failed. Score not satisfactory, please try again.")
                    .setIcon(R.drawable.ic_cancel_black_24dp)
                    .addButton("Redo", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                            CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    recreate();
                                }
                            });

            builder.show();

        }
    }

}
