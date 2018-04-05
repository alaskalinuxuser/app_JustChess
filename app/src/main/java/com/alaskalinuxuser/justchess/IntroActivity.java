package com.alaskalinuxuser.justchess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;

public class IntroActivity extends AppCompatActivity {

    static ImageView singleButton, doubleButton;
    static Boolean pBlack, pPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        singleButton = (ImageView) findViewById(R.id.playSingleButton);
        doubleButton = (ImageView) findViewById(R.id.playDoubleButton);
        pBlack = false;
        pPass = false;

    } // End on create.

    public void playSingle (View singleButton) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Black or White?")
                .setMessage(
                        "Please choose to play as black or white.")
                .setPositiveButton("Black", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Testing only //
                        Log.i("WJH", "Clicked Black.");

                        //what to do.
                        pBlack = true;
                        pPass = false;

                        // First you define it.
                        Intent myintent = new Intent(IntroActivity.this, MainActivity.class);
                        myintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        // Now you call it.
                        startActivity(myintent);

                    }
                })
                .setNegativeButton("White", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Log.i("WJH", "Clicked White.");

                        pBlack = false;
                        pPass = false;

                        // First you define it.
                        Intent myintent = new Intent(IntroActivity.this, MainActivity.class);
                        myintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        // Now you call it.
                        startActivity(myintent);

                    }
                })
                .show(); // Make sure you show your popup or it wont work very well!
    } // End playSingle

    public void playDouble (View doubleButton) {

        pBlack = false;
        pPass = true;
        // First you define it.
        Intent myintent = new Intent(IntroActivity.this, MainActivity.class);
        myintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        // Now you call it.
        startActivity(myintent);
    } // End playDouble

}
