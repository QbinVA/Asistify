package com.example.asistify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<InfoClases> dataList;
    private FirebaseAuth mAuth;

    public MyAdapter(Context context, List<InfoClases> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_itrem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InfoClases infoClase = dataList.get(position);
        holder.recClase.setText(infoClase.getClase());
        holder.recDocente.setText(infoClase.getDocente());

        holder.recCard.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String currentUser = user.getUid();
                String claseCodigo = infoClase.getCodigo();
                DatabaseReference claseRef = FirebaseDatabase.getInstance().getReference("clases").child(claseCodigo);

                claseRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        InfoClases claseInfo = task.getResult().getValue(InfoClases.class);
                        if (claseInfo != null && currentUser.equals(claseInfo.getUserId())) {
                            Intent intent = new Intent(context, ListAsistencia.class);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, DetallesClase.class);
                            intent.putExtra("NombreClase", infoClase.getClase());
                            intent.putExtra("Codigo", infoClase.getCodigo());
                            context.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context, "Clase no encontrada", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDeleteClass.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar Clase")
                    .setMessage("¿Estás seguro de que deseas eliminar esta clase?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            String claseCodigo = infoClase.getCodigo();
                            DatabaseReference userClaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("clases").child(claseCodigo);

                            userClaseRef.removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dataList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, dataList.size());
                                    Toast.makeText(context, "Clase eliminada", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Error al eliminar la clase", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recClase, recDocente;
        CardView recCard;
        ImageButton btnDeleteClass;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recClase = itemView.findViewById(R.id.recClase);
            recDocente = itemView.findViewById(R.id.recDocente);
            recCard = itemView.findViewById(R.id.recCard);
            btnDeleteClass = itemView.findViewById(R.id.btnDeleteClass);
        }
    }
}
