package com.example.proyectocompartido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final List<String> listaMedicamentos;
    private int selectedPosition = -1;
    public MyAdapter(List<String> list) {
        listaMedicamentos = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String valor = listaMedicamentos.get(position);
        holder.textViewItem.setText(valor);
        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listaMedicamentos != null ? listaMedicamentos.size() : 0;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public String getSelectedValue() {
        return selectedPosition != -1 ? listaMedicamentos.get(selectedPosition) : null;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewItem;
        public RadioButton radioButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.itemTextView);
            radioButton = itemView.findViewById(R.id.itemRadioButton);
        }
    }
}
