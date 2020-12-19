package com.example.concentriccircledraw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;


public class UploadDialogue {
    private Activity activity;
    private Context context;
    private String Message;

    private AlertDialog alertDialog;

    public UploadDialogue(Activity activity, Context context, String message) {
        this.activity = activity;
        this.context = context;
        Message = message;
        alertDialog= ProgressDialog.show(activity, "Data upload",
                message, true);
    }
    public void display(){
        alertDialog.show();
    }
    public void hide(){

        alertDialog.hide();
        Toast.makeText(context,"Data uploaded succesfully",Toast.LENGTH_LONG).show();
    }
}
