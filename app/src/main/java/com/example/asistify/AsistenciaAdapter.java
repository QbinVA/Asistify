package com.example.asistify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AsistenciaAdapter extends RecyclerView.Adapter<AsistenciaAdapter.AsistenciaViewHolder> {

    private List<Asistencia> asistenciaList;

    public AsistenciaAdapter(List<Asistencia> asistenciaList) {
        this.asistenciaList = asistenciaList;
    }

    @NonNull
    @Override
    public AsistenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asistencia, parent, false);
        return new AsistenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsistenciaViewHolder holder, int position) {
        Asistencia asistencia = asistenciaList.get(position);
        holder.textViewNombre.setText(asistencia.getNombre());
        holder.textViewCorreo.setText(asistencia.getCorreo());
        holder.textViewFecha.setText(asistencia.getFecha());
    }

    @Override
    public int getItemCount() {
        return asistenciaList.size();
    }

    public static class AsistenciaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewCorreo;
        TextView textViewFecha;

        public AsistenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewCorreo = itemView.findViewById(R.id.textViewCorreo);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
        }
    }
}
