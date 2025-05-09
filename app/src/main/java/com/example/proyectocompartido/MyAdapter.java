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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final List<String> listaMedicamentos;
    private final HashMap<String, String> medicamentoMap;

    private int selectedPosition = -1;
    private String selectedKey = null;
    public MyAdapter(HashMap<String, String> map) {
        this.medicamentoMap = map;
        this.listaMedicamentos = new ArrayList<>(map.values());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final String valor = listaMedicamentos.get(position);
        holder.textViewItem.setText(valor);
        holder.radioButton.setChecked(position == selectedPosition);
        final int currentPosition = position;

        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = currentPosition;
            selectedKey = getKeyFromValue(medicamentoMap, valor);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listaMedicamentos != null ? listaMedicamentos.size() : 0;
    }
    private String getKeyFromValue(HashMap<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
    public int getSelectedPosition() {
        return selectedPosition;
    }

    public String getSelectedKey() {
        return selectedKey;
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

//