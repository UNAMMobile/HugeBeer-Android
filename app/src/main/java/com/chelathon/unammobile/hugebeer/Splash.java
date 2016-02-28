package com.chelathon.unammobile.hugebeer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {


    AnimationDrawable rocketAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Context c = this;
        getSupportActionBar().hide();

        //ImageView rocketImage = (ImageView) findViewById(R.id.imageview_splash_anim);
        //((AnimationDrawable) rocketImage.getBackground()).start();

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // finish the splash activity so it can't be returned to
                Splash.this.finish();
                // create an Intent that will start the second activity
                Intent mainIntent = new Intent(Splash.this, PreLoginActivity.class);
                Splash.this.startActivity(mainIntent);
            }
        }, 3000); // 3000 milliseconds


    }
}
