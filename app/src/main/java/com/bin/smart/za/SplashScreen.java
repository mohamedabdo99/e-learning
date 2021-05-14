package com.bin.smart.za;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bin.smart.za.ui.LoginTypeActivity;

public class SplashScreen extends AppCompatActivity {
   Animation topAnim,bottomAnim;
   ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //animation

//        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
//
//        img.setAnimation(topAnim);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginTypeActivity.class);
                startActivity(intent);
                finish();

            }
        },3000);



    }
}