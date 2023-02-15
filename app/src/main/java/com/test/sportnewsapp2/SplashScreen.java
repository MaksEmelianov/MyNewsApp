package com.test.sportnewsapp2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }).start();
    }
}