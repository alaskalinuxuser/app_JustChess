package com.alaskalinuxuser.justchess;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class aboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabGit = (FloatingActionButton) findViewById(R.id.fabGithub);
        FloatingActionButton fabWord = (FloatingActionButton) findViewById(R.id.fabWord);
        FloatingActionButton fabBack = (FloatingActionButton) findViewById(R.id.fabReturn);

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goBack();

            }
        });

        fabGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myHubGit();

            }
        });

        fabWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myWebsite();

            }
        });
    }

    // When they click on the github icon.
    public void myHubGit () {

        goToUrl ( "https://github.com/alaskalinuxuser");

    }

    // When they click on the wordpress icon.
    public void myWebsite () {

        goToUrl ( "https://thealaskalinuxuser.wordpress.com");

    }

    // When they click on the how to text.
    public void howL (View view) {

        goToUrl ( "http://www.apache.org/licenses/LICENSE-2.0");

    }

    // To launch one of the above URL's.
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    // To kill this activity and go back to Activity Main.
    public void goBack () {
        finish();
    }

}
