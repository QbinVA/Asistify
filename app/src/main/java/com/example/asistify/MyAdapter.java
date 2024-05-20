package com.example.asistify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<InfoClases> dataList;

    public MyAdapter(Context context, List<InfoClases> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_itrem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recMateria.setText(dataList.get(position).getMateria());
        holder.recDocente.setText(dataList.get(position).getDocente());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetallesClase.class);
                intent.putExtra("Materia", dataList.get(holder.getAdapterPosition()).getMateria());
                intent.putExtra("Docente", dataList.get(holder.getAdapterPosition()).getDocente());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView recMateria, recDocente;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recMateria = itemView.findViewById(R.id.recMateria);
        recDocente = itemView.findViewById(R.id.recDocente);
        recCard = itemView.findViewById(R.id.recCard);

    }
}