package com.alaskalinuxuser.justchess;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    RatingBar engineStrengthBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Define my question bars.
        engineStrengthBar = (RatingBar) findViewById(R.id.eSRB);
        engineStrengthBar.setRating(MainActivity.engineStrength+1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.engineStrength = Math.round(engineStrengthBar.getRating()-1);
                goBack();
            }
        });
    }

    // To kill this activity and go back to Activity Main.
    public void goBack () {
        finish();
    }

}
