package com.example.asistify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class logosplash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1300; // 1.3 segundos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logosplash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(logosplash.this, loginalumno.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}