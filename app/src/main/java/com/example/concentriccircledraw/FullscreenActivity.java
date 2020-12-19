package com.example.concentriccircledraw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FullscreenActivity extends AppCompatActivity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private View mContentView;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        getSupportActionBar().hide();
        mContentView = findViewById(R.id.fullscreen_content);
        uid=getIntent().getStringExtra("UID");
        if(uid!=null)
            Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(getApplicationContext(),"Opened Separately",Toast.LENGTH_SHORT).show();
        }

        closeThis(AUTO_HIDE_DELAY_MILLIS);
    }

    private void closeThis(final int delayMillis) {
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences=getSharedPreferences("data.pref",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putInt("PATTERN_NUMBER",1);
                    editor.putInt("INDEX_NUMBER",1);
                    editor.commit();

                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(new Intent(FullscreenActivity.this,MainActivity.class).putExtra("uid",uid));

            }
        });
        t.start();
    }


}
