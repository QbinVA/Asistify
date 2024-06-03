package com.example.asistify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import java.io.IOException;
import java.util.Objects;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.asistify.R;

public class EditPerfil extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText editname, editemail, editpassword;
    Button savebtn, selectImageBtn;
    ImageView profileImage;
    Uri imageUri;
    String nameuser, emailuser, passworduser;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    StorageReference storageReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editEmail);
        editpassword = findViewById(R.id.editPassword);
        profileImage = findViewById(R.id.perfilusu);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        savebtn = findViewById(R.id.savebtn);


        loadProfileImage();
        checkPermissions();





        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });



        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged()) {
                    Toast.makeText(EditPerfil.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                    saveUserData(); // Guardar datos del usuario en Firebase
                    returnToMenuAlumno(); // Volver a la actividad de menú
                } else {
                    savebtn.setEnabled(true);


                    AlertDialog.Builder builder = new AlertDialog.Builder(EditPerfil.this);
                    builder.setTitle("No hay cambios");
                    builder.setMessage("No se realizaron cambios");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Cierra la alerta cuando se hace clic en "Aceptar"
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            // Guardar los datos del usuario en Firebase
            private void saveUserData() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String newName = editname.getText().toString();
                    String newEmail = editemail.getText().toString();
                    String newPassword = editpassword.getText().toString();

                    // Actualizar los datos del usuario en la base de datos
                    databaseReference.child(user.getUid()).child("name").setValue(newName);
                    databaseReference.child(user.getUid()).child("email").setValue(newEmail);
                    databaseReference.child(user.getUid()).child("password").setValue(newPassword);

                    // Guardar los nuevos datos localmente si es necesario
                    saveUserDataLocally(newName, newEmail, newPassword);


                }
            }

            // Guardar los datos del usuario localmente en SharedPreferences
            private void saveUserDataLocally(String name, String email, String password) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", name);
                editor.putString("email", email);
                editor.putString("password", password);
                editor.apply();
            }
        });

        showData();
    }

    private void saveUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", editname.getText().toString());
        editor.putString("email", editemail.getText().toString());
        editor.putString("password", editpassword.getText().toString());
        editor.apply();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Habilitar el botón si hay algún cambio en los EditText
            if (editname.getText().toString().equals(nameuser) &&
                    editemail.getText().toString().equals(emailuser) &&
                    editpassword.getText().toString().equals(passworduser)) {
                savebtn.setEnabled(false); // No hay cambios, deshabilitar el botón
            } else {
                savebtn.setEnabled(true); // Hay cambios, habilitar el botón
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void loadProfileImage() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String imageURL = sharedPreferences.getString("imageURL", "");
        if (!imageURL.isEmpty()) {
            Picasso.get().load(imageURL).into(profileImage); // Asegúrate de tener Picasso configurado en tu proyecto
        }
    }

    private String getImageURLFromLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("imageURL", "");
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Recupera los datos del usuario desde Firebase al iniciar la actividad
    private void retrieveUserDataFromFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = databaseReference.child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String password = dataSnapshot.child("password").getValue(String.class);
                        String imageURL = dataSnapshot.child("profileImage").getValue(String.class);

                        // Actualiza las vistas con los datos recuperados
                        editname.setText(name);
                        editemail.setText(email);
                        editpassword.setText(password);

                        // Carga la imagen utilizando la URL recuperada
                        assert imageURL != null;
                        if (!imageURL.isEmpty()) {
                            Picasso.get().load(imageURL).into(profileImage);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error al recuperar datos del usuario", databaseError.toException());
                }
            });
        }
    }


    @SuppressLint("CheckResult")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Glide.with(this)
                        .load(imageUri)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                Log.d("URI", "Image URI: " + imageUri.toString());
                uploadImageToFirebase();

                // Devuelve la URL de la imagen seleccionada a MenuUser
                Intent resultIntent = new Intent();
                resultIntent.putExtra("imageURL", imageUri.toString());
                setResult(RESULT_OK, resultIntent);


            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) {
                Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                return;
            }

            StorageReference fileReference = storageReference.child(user.getUid() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Obtener la URL de descarga
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    databaseReference.child(user.getUid()).child("profileImage").setValue(downloadUrl);
                                    Toast.makeText(EditPerfil.this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show();

                                    // Guardar la URL de la imagen en SharedPreferences
                                    saveImageURLLocally(downloadUrl);
                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Firebase", "Error subiendo la imagen", e);
                                    Toast.makeText(EditPerfil.this, "Fallo al subir imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        } else {
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageURLLocally(String imageURL) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imageURL", imageURL);
        editor.apply();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    private void returnToMenuAlumno() {
        Intent intent = new Intent(EditPerfil.this, menualumno.class);
        intent.putExtra("name", editname.getText().toString());
        intent.putExtra("email", editemail.getText().toString());
        intent.putExtra("password", editpassword.getText().toString());
        startActivity(intent);
        finish();
    }

    private void updateNameInFirebase(String newName) {
        databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("name").setValue(newName);
    }

    private void updateEmailInFirebase(String newEmail) {
        databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("email").setValue(newEmail);
    }

    private void showData() {
        Intent intent = getIntent();

        nameuser = intent.getStringExtra("name");
        emailuser = intent.getStringExtra("email");
        passworduser = intent.getStringExtra("password");

        editname.setText(nameuser);
        editemail.setText(emailuser);
        editpassword.setText(passworduser);
    }

    public boolean isNameChanged() {
        if (!nameuser.equals(editname.getText().toString())) {
            databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("name").setValue(editname.getText().toString());
            nameuser = editname.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmailChanged() {
        if (!emailuser.equals(editemail.getText().toString())) {
            databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("email").setValue(editemail.getText().toString());
            emailuser = editemail.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isPasswordChanged() {
        if (!passworduser.equals(editpassword.getText().toString())) {
            databaseReference.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("password").setValue(editpassword.getText().toString());
            passworduser = editpassword.getText().toString();
            return true;
        } else {
            return false;
        }
    }
}

