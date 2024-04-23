package com.example.asistify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import android.widget.ImageButton;

public class menualumno extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menualumno);

        ImageButton imageButton = findViewById(R.id.imgbtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea y muestra un diálogo de alerta
                AlertDialog.Builder builder = new AlertDialog.Builder(menualumno.this);
                builder.setMessage("Haz hecho clic en el botón de imagen")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Cierra el diálogo si se hace clic en Aceptar
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button btnUnirse = findViewById(R.id.btnUnirse);

        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crear y mostrar el menú emergente
                PopupMenu popupMenu = new PopupMenu(menualumno.this, btnUnirse);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Manejar los clics en las opciones del menu
                        if (item.getItemId() == R.id.option_dialog) {//Mostrar el dialogo emergente cuando se hace clic en esta opcion
                            showAlertDialog();
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    //Metodo para mostrar el dialogo emergente
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta")
                .setMessage("Esta funcion no ha sido terminada.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Accion cuando hace click en Aceptar
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Acciom cuando se hace clic en Cancelar
                    }
                })
                .show();
    }

}