package com.example.databaseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplachScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=3000;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splach_screen);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(pref.getBoolean("registered",false)&& pref.getBoolean("login",false)){
                    Intent i=new Intent(SplachScreenActivity.this,
                            HomeScreen.class);
                    startActivity(i);
                    finish();
                }

                /* else if(pref.getBoolean("login",false)){
                    Intent i=new Intent(SplachScreenActivity.this,
                            HomeScreen.class);
                    startActivity(i);
                    finish();
                }*/
                 else{
                     Intent i=new Intent(SplachScreenActivity.this,
                             LoginActivity.class);
                     startActivity(i);
                     finish();
                 }

            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}