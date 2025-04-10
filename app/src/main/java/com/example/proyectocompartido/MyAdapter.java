package com.example.proyectocompartido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Ejemplo, importa tus vistas

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

     private List<String> listaMedicamentos;

     public MyAdapter(List<String> list) {
         listaMedicamentos = list;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position < listaMedicamentos.size()) {
            holder.textViewItem.setText(listaMedicamentos.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listaMedicamentos != null ? listaMedicamentos.size() : 0;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             textViewItem = itemView.findViewById(R.id.itemTextView);
        }
    }
}