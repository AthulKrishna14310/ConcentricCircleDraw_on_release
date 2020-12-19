 package com.example.concentriccircledraw;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;

public class MainActivity extends AppCompatActivity {
    private Button finishButton;
    private PaintView paintView;
    private Button refreshButton;
    private Chronometer chronometer;
    private long base;
    private boolean start = true;
    private MediaPlayer ErrorMediaPlayer;
    private Button TryYourselfBtn;
    private String uid;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ErrorMediaPlayer = MediaPlayer.create(this, R.raw.error);
        if(ErrorMediaPlayer !=null)
        {
            ErrorMediaPlayer.setLooping(true);
        }
        finishButton = (Button) findViewById(R.id.submitButton);
        paintView = (PaintView) findViewById(R.id.paintView);
        refreshButton =  findViewById(R.id.refreshButton);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        TryYourselfBtn = findViewById(R.id.skipButton);
        textView=findViewById(R.id.resultText);

//        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
//                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
//                .setTitle("Instructions.")
//                .setMessage("1.Trace the concentric circles one by one.\n" +
//                            "2.Complete the concentric circles one by one with minimum time and less traced out \n" +
//                            "3.At last click above button to get score.")
//                .setIcon(R.drawable.ic_info_black_24dp)
//                .addButton("Ok, I understand", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
//                        CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            }
//                        });
//
//        builder.show();




        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result();
            }
        });

        TryYourselfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                skip();


            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        paintView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (start) {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        start = false;
                    } else {
                        chronometer.setBase(base);
                        chronometer.start();

                    }

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (paintView.isOutIndex() == true) {
                        if(ErrorMediaPlayer != null)
                        {
                            ErrorMediaPlayer.start();
                        }
                        textView.setTextColor(Color.RED);
                        textView.setText("Traced out..");
                       } else {
                        if(ErrorMediaPlayer != null && ErrorMediaPlayer.isPlaying())
                        {
                            ErrorMediaPlayer.pause();
                        }
                        textView.setTextColor(Color.BLACK);
                        textView.setText("Continue...");
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ErrorMediaPlayer.pause();
                    textView.setTextColor(Color.BLACK);
                    textView.setText("Click 'SUBMIT' if finished");
                    base = chronometer.getBase();
                    chronometer.setBase(base);
                    chronometer.stop();
                }


                return false;
            }
        });

       findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finishAffinity();

           }
       });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You are at beginning", Toast.LENGTH_SHORT).show();
            }
        });
       uid=getIntent().getStringExtra("UID");

    }

    private void skip() {
        startActivity(new Intent(MainActivity.this, TryUrSelfActivity.class).putExtra("UID", uid));
    }

    private void result() {
        if (paintView.greenEnded && paintView.blueEnded && paintView.magentaEnded && paintView.yellowEnded) {
            long time = SystemClock.elapsedRealtime() - chronometer.getBase();
            float x = paintView.getTouched() / (time / 1000);
            int marks = (int) x;
            showfinalDialogue(marks, paintView);
        } else {
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
    private void refresh(){
        paintView.clear();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.circle, options);
        Paint b = paintView.getBrush();
        Path p = paintView.getPath();
        Canvas canvas = new Canvas(mBackground);
        b.setColor(Color.WHITE);
        for (int i = 0; i < paintView.totalPixelX.size(); i++) {
            p.moveTo(paintView.totalPixelX.get(i), paintView.totalPixelY.get(i));
            canvas.drawPath(p, b);
        }
        paintView.setImageBitmap(mBackground);
        paintView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.circle));
        paintView.clear();
    }
    private void showfinalDialogue(int marks, final PaintView paintView) {
        // Create Alert using Builder
        if (marks >= paintView.getSCORE()) {

            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("Well done .You are able to get trace curves, now you  can proceed " +
                            "to" +
                            " try " +
                            "concentric circles  " +
                            "yourself . Score :: " + marks)
                    .setIcon(R.drawable.ic_check_circle_black_24dp)
                    .addButton("Try your self", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                            CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MainActivity.this, TryUrSelfActivity.class).putExtra("UID",uid));
                                }
                            });

            builder.show();
        } else {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("Failed. Please draw the circles with minimum time and less traced out.")
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ErrorMediaPlayer !=null)
        {
            ErrorMediaPlayer.release();
            ErrorMediaPlayer = null;
        }
    }
}
