package com.example.asistify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class logosplash extends AppCompatActivity {


    protected void onCreate(Bundle saveInstancesState){
        super.onCreate(saveInstancesState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logosplash);

        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.despla_arriba);

        ImageView buho = findViewById(R.id.imgview3);

        buho.setAnimation(animacion1);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(logosplash.this, menualumno.class);
                startActivity(intent);
                finish();
            }
        }, 900);


    }

}