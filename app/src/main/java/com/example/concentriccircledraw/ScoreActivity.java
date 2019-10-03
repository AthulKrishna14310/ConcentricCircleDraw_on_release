package com.example.concentriccircledraw;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.crowdfire.cfalertdialog.CFAlertDialog;

public class ScoreActivity extends AppCompatActivity {
    private int temp=0;
    private int total=0;
    private Button scoreButton;
    private boolean success=false;
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
        scoreButton=findViewById(R.id.scoreButton);

        scoreButton.setText(" "+temp+"/ "+"100");

        if(temp<75){
            showfinalDialogue(false);
        }else {
            showfinalDialogue(true);

        }
    }
    private void showfinalDialogue(boolean success) {
        // Create Alert using Builder
        if(success){
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
                    .setTitle("Success. Congrats you hit the score.")
                    .setIcon(R.drawable.ic_check_circle_black_24dp)
                    .addButton("Try your self", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                            CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
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
