package com.example.concentriccircledraw;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.ArrayList;
import java.util.Locale;

public class TryUrSelfActivity extends AppCompatActivity {
    private ArrayList<Integer> radiuses1;
    private ArrayList<Integer> radiuses2;
    private ArrayList<Integer> radiuses3;
    private ArrayList<Integer> radiuses4;


    private ArrayList<Integer> pixelXs;
    private ArrayList<Integer> pixelYs;

    private int circleCount = 0;

    private int CENTRE_X = 452;
    private int CENTRE_Y = 452;
    private int CIRCLE_ONE_X = 445;
    private int CIRCLE_ONE_Y = 355;
    private int CIRCLE_TWO_X = 455;
    private int CIRCLE_TWO_Y = 265;
    private int CIRCLE_THREE_X = 455;
    private int CIRCLE_THREE_Y = 155;
    private int CIRCLE_FOUR_X = 455;
    private int CIRCLE_FOUR_Y = 45;


    private CircleDrawLayout circleDrawLayout;
    private Button finishButton;
    private Chronometer chronometer;
    private boolean start = true;
    private long base = 0;

    private ArrayList<String> texts;
    private ArrayList<Integer> scores;
    private int temp = 0;

    private TextToSpeech Speaker ;
    private RelativeLayout RootTryYourself;
    private String uid;
    private TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_ur_self);
        getSupportActionBar().hide();
        radiuses1 = new ArrayList<>();
        radiuses2 = new ArrayList<>();
        radiuses3 = new ArrayList<>();
        radiuses4 = new ArrayList<>();
        texts = new ArrayList<>();
        scores = new ArrayList<>();
        pixelXs = new ArrayList<>();
        pixelYs = new ArrayList<>();
        texts.clear();
        texts.add("first");
        texts.add("second");
        texts.add("third");
        texts.add("fourth");
        texts.add("last");

        circleDrawLayout = (CircleDrawLayout) findViewById(R.id.circleDrawLayout);
        finishButton = (Button) findViewById(R.id.submitButton);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        resultText=findViewById(R.id.resultText);
        RootTryYourself = findViewById(R.id.root_try_yourself);
        Speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int result = Speaker.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Snackbar.make(RootTryYourself, "English language not supported by this device. Audio not available.", Snackbar.LENGTH_LONG).show();
                    }

                } else
                {
                    Snackbar.make(RootTryYourself, "Text-to-Speech initialization failed.", Snackbar.LENGTH_LONG).show();
                }
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
                  finish();
              }
          });
        findViewById(R.id.skipButton).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Toast.makeText(getApplicationContext(), "You are at end", Toast.LENGTH_SHORT).show();
              }
        });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
         uid=getIntent().getStringExtra("UID");
    }


    @Override
    protected void onStart() {
        super.onStart();

        circleDrawLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    /*
                    if(Speaker !=null)
                    {
                        if(Speaker.isSpeaking())
                        {
                            Speaker.stop();
                        }
                    }
                     */
                    resultText.setText("Continue tracing the  " + texts.get(circleCount) + " circle");


                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    resultText.setText("Continue drawing " + texts.get(circleCount) + " circle.");
                    pixelXs.add(circleDrawLayout.getXX());
                    pixelYs.add(circleDrawLayout.getYY());

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String Text = "Click SUBMIT if " + texts.get(circleCount) + " circle is finished.";
                    Speaker.speak(Text,TextToSpeech.QUEUE_FLUSH,null);
                    resultText.setText(Text);
                    base = chronometer.getBase();
                    chronometer.setBase(base);
                    chronometer.stop();
                }


                return false;
            }
        });


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (circleCount) {
                    case 0:
                        resultText.setText("Now draw the  " + texts.get(circleCount + 1) + " circle");
                       // finishButton.setBackgroundColor(Color.parseColor("#00574B"));
                        revealSCORE_ONE();
                        circleCount++;
                        break;
                    case 1:
                        resultText.setText("Now draw the  " + texts.get(circleCount + 1) + " circle");
                        revealSCORE_TWO();
                        circleCount++;
                        break;
                    case 2:
                        resultText.setText("Now draw the  " + texts.get(circleCount + 1) + " circle");
                        revealSCORE_THREE();
                        circleCount++;
                        break;
                    case 3:
                        String text = "Click again to get the score.";
                        Speaker.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                        resultText.setText(text);
                       // finishButton.setBackgroundColor(Color.parseColor("#00574B"));
                        revealSCORE_FOUR();
                        circleCount++;
                        break;
                    case 4:
                        float total = scores.get(0).intValue() +
                                scores.get(1).intValue() +
                                scores.get(2).intValue()
                                + scores.get(3).intValue();
                        float average = total / 4;
                        Intent intent = new Intent(TryUrSelfActivity.this, ScoreActivity.class);
                        intent.putExtra("VALUE", (int) average);
                        intent.putExtra("TOTAL", 100);
                        intent.putExtra("TIME",String.valueOf(SystemClock.elapsedRealtime() - chronometer.getBase()));
                        intent.putExtra("UID",uid);
                        startActivity(intent);
                        break;

                    default:
                       // finishButton.setText("FINISHED YOUR ACTIVITY");
                        resultText.setText("Finished your activity.");
                }
            }
        });

        findViewById(R.id.refreshButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               refresh();
            }
        });


    }
    private void refresh(){
        circleDrawLayout.clear();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.circle, options);
        Paint b = circleDrawLayout.getBrush();
        Path p = circleDrawLayout.getPath();
        Canvas canvas = new Canvas(mBackground);
        b.setColor(Color.WHITE);
        for (int i = 0; i < circleDrawLayout.totalPixelX.size(); i++) {
            p.moveTo(circleDrawLayout.totalPixelX.get(i), circleDrawLayout.totalPixelY.get(i));
            canvas.drawPath(p, b);
        }
        circleDrawLayout.setImageBitmap(mBackground);
        circleDrawLayout.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.circle));
        circleDrawLayout.clear();
    }
    private void revealSCORE_TWO() {
        try {
            temp = 0;
            float percent = 0;

            for (int i = 0; i < pixelXs.size(); i++) {
                radiuses2.add((int) Math.sqrt(((pixelXs.get(i).intValue()
                        - CENTRE_X) * (pixelXs.get(i).intValue() - CENTRE_X)) +
                        ((pixelYs.get(i).intValue() - CENTRE_Y)
                                * (pixelYs.get(i).intValue() - CENTRE_Y))));
            }
            int fixed = (int) Math.sqrt(((CIRCLE_TWO_X
                    - CENTRE_X) * (CIRCLE_TWO_X - CENTRE_X)) +
                    (CIRCLE_TWO_Y - CENTRE_Y)
                            * (CIRCLE_TWO_Y - CENTRE_Y));

            for (int i = 0; i < radiuses2.size(); i++) {
                if (radiuses2.get(i).intValue() <= (fixed + 20) && radiuses2.get(i).intValue() >= (fixed - 20)
                ) {
                    temp++;
                }
            }

            percent = (temp * 100) / radiuses2.size();
            scores.add((int) percent);

            pixelXs.clear();
            pixelYs.clear();


        } catch (ArithmeticException e) {
            e.printStackTrace();
            reportError();
        }

    }


    private void revealSCORE_THREE() {

        try {
            temp = 0;
            float percent = 0;

            for (int i = 0; i < pixelXs.size(); i++) {
                radiuses3.add((int) Math.sqrt(((pixelXs.get(i).intValue()
                        - CENTRE_X) * (pixelXs.get(i).intValue() - CENTRE_X)) +
                        ((pixelYs.get(i).intValue() - CENTRE_Y)
                                * (pixelYs.get(i).intValue() - CENTRE_Y))));
            }
            int fixed = (int) Math.sqrt(((CIRCLE_THREE_X
                    - CENTRE_X) * (CIRCLE_THREE_X - CENTRE_X)) +
                    (CIRCLE_THREE_Y - CENTRE_Y)
                            * (CIRCLE_THREE_Y - CENTRE_Y));

            for (int i = 0; i < radiuses3.size(); i++) {
                if (radiuses3.get(i).intValue() <= (fixed + 20) && radiuses3.get(i).intValue() >= (fixed - 20)
                ) {
                    temp++;
                }
            }

            percent = (temp * 100) / radiuses3.size();
            scores.add((int) percent);

            pixelXs.clear();
            pixelYs.clear();


        } catch (ArithmeticException e) {
            e.printStackTrace();
            reportError();
        }

    }

    private void revealSCORE_FOUR() {
        try {
            temp = 0;
            float percent = 0;

            for (int i = 0; i < pixelXs.size(); i++) {
                radiuses4.add((int) Math.sqrt(((pixelXs.get(i).intValue()
                        - CENTRE_X) * (pixelXs.get(i).intValue() - CENTRE_X)) +
                        ((pixelYs.get(i).intValue() - CENTRE_Y)
                                * (pixelYs.get(i).intValue() - CENTRE_Y))));
            }
            int fixed = (int) Math.sqrt(((CIRCLE_FOUR_X
                    - CENTRE_X) * (CIRCLE_FOUR_X - CENTRE_X)) +
                    (CIRCLE_FOUR_Y - CENTRE_Y)
                            * (CIRCLE_FOUR_Y - CENTRE_Y));

            for (int i = 0; i < radiuses4.size(); i++) {
                if (radiuses4.get(i).intValue() <= (fixed + 20) && radiuses4.get(i).intValue() >= (fixed - 20)
                ) {
                    temp++;
                }
            }

            percent = (temp * 100) / radiuses4.size();
            scores.add((int) percent);

            pixelXs.clear();
            pixelYs.clear();


        } catch (ArithmeticException e) {
            e.printStackTrace();
            reportError();
        }


    }

    private void revealSCORE_ONE() {

        try {
            temp = 0;
            float percent = 0;

            for (int i = 0; i < pixelXs.size(); i++) {
                radiuses1.add((int) Math.sqrt(((pixelXs.get(i).intValue()
                        - CENTRE_X) * (pixelXs.get(i).intValue() - CENTRE_X)) +
                        ((pixelYs.get(i).intValue() - CENTRE_Y)
                                * (pixelYs.get(i).intValue() - CENTRE_Y))));
            }
            int fixed = (int) Math.sqrt(((CIRCLE_ONE_X
                    - CENTRE_X) * (CIRCLE_ONE_X - CENTRE_X)) +
                    (CIRCLE_ONE_Y - CENTRE_Y)
                            * (CIRCLE_ONE_Y - CENTRE_Y));

            for (int i = 0; i < radiuses1.size(); i++) {
                if (radiuses1.get(i).intValue() <= (fixed + 20) && radiuses1.get(i).intValue() >= (fixed - 20)
                ) {
                    temp++;
                }
            }

            percent = (temp * 100) / radiuses1.size();
            scores.add((int) percent);

            pixelXs.clear();
            pixelYs.clear();


        } catch (ArithmeticException e) {
            e.printStackTrace();
            reportError();
        }


    }

    private void reportError() {
        if (radiuses1.isEmpty() || radiuses2.isEmpty()
                || radiuses3.isEmpty() || radiuses4.isEmpty()) {
            CFAlertDialog.Builder builder =
                    new CFAlertDialog.Builder(TryUrSelfActivity.this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                            .setTitle("Please draw the circles as instructed above one by" +
                                    " one.")
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
        if(Speaker != null)
        {
            Speaker.stop();
            Speaker.shutdown();
        }
        super.onDestroy();
    }
}

