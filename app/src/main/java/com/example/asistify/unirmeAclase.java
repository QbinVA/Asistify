package com.example.asistify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class unirmeAclase extends AppCompatActivity {

    FloatingActionButton back = findViewById(R.id.back);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirme_aclase);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(unirmeAclase.this, menualumno.class);
                startActivity(intent);
            }
        });
    }
}