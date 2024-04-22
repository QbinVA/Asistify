package com.example.asistify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loginmaestro extends AppCompatActivity {

    Button buttonlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginmaestro);
        buttonlogin = findViewById(R.id.button3);

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginmaestro.this, menumaestro.class);
                startActivity(intent);
            }
        });
    }
}