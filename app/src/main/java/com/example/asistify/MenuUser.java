package com.example.asistify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import com.example.asistify.EditPerfil;
import com.example.asistify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.app.Dialog;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import java.util.Objects;

public class MenuUser extends AppCompatActivity {
    ShapeableImageView profileImage;
    private static final int EDIT_PROFILE_REQUEST = 2;

    TextView profileName, profileEmail, profilePassword;
    Button editbtn;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        profileImage = findViewById(R.id.perfilusu);

        profileName = findViewById(R.id.profilename);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        editbtn = findViewById(R.id.editbtn);

        showUserDB();

        loadImage();

        // Configurar el OnClickListener para mostrar la imagen ampliada
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullImageDialog();
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passUserData();
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserDB();

        // Cargar la imagen en el ImageView
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String imageURL = preferences.getString("imageURL", "");
        if (!imageURL.isEmpty()) {
            Picasso.get().load(imageURL).into(profileImage); // Asegúrate de tener Picasso configurado en tu proyecto
        }
    }

    public void showUserDB() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);
                    String imageURL = snapshot.child("profileImage").getValue(String.class);

                    profileName.setText(name);
                    profileEmail.setText(email);
                    profilePassword.setText(password);

                    // Guardar datos en SharedPreferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuUser.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", name);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.putString("imageURL", imageURL); // Guardar la URL de la imagen
                    editor.apply();
                } else {
                    Toast.makeText(MenuUser.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenuUser.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadImage() {
        // Suponiendo que tienes el URI de la imagen guardado localmente o en la base de datos
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("imageURL", "");

        // Usar Glide para cargar la imagen en el perfil
        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .into(profileImage);
    }

    private void showFullImageDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image);
        ImageView fullImageView = dialog.findViewById(R.id.dialogImageView);

        // Usar Glide para cargar la imagen en el Dialog
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("imageURL", "");
        Glide.with(this)
                .load(imageUrl)
                .fitCenter()
                .into(fullImageView);

        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            String imageURL = data.getStringExtra("imageURL");
            if (imageURL != null && !imageURL.isEmpty()) {
                // Cargar la imagen en el ImageView
                Picasso.get().load(imageURL).into(profileImage);
            }
        }
    }

    private void passUserData() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);

                    Intent intent = new Intent(MenuUser.this, EditPerfil.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivityForResult(intent, EDIT_PROFILE_REQUEST); // Aquí usamos startActivityForResult()
                } else {
                    Toast.makeText(MenuUser.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenuUser.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
