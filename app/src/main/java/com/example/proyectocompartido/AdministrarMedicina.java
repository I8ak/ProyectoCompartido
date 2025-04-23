package com.example.proyectocompartido;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdministrarMedicina extends AppCompatActivity {

    private MyAdapter adapter;
    private HashMap<String, String> medicamentoMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrar_medicina);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String respuesta = getIntent().getStringExtra("respuesta");
        try {
            JSONObject data = new JSONObject(respuesta);
            JSONArray administr = data.getJSONArray("administr");
            for (int i = 0; i < administr.length(); i++) {
                JSONObject a = administr.getJSONObject(i);
                String id = a.getString("id");
                String fechaAdministracion = a.getString("fechaAdministracion");
                medicamentoMap.put(id, fechaAdministracion);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        for (Map.Entry<String, String> s : medicamentoMap.entrySet()) {
            Log.i("Clave", s.getKey());
            Log.i("Valor", s.getValue());
        }

        adapter = new MyAdapter(medicamentoMap);
        recyclerView.setAdapter(adapter);

        MaterialButton fabValidar = findViewById(R.id.fabValidar);
        fabValidar.setOnClickListener(v -> {
            String seleccionado = adapter.getSelectedKey();
            if (seleccionado != null) {
                new Lanzar(seleccionado, 33335, respuestaServidor -> runOnUiThread(() -> {
                    manejarRespuestaServidor(respuestaServidor);
                    Log.i("Mensaje", seleccionado);
                })).start();
            } else {
                Toast.makeText(this, "Por favor, selecciona un elemento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manejarRespuestaServidor(String respuesta) {
        try {
            JSONObject json = new JSONObject(respuesta);
            String error = json.optString("error", null);
            if (error != null && !error.equals("null")) {
                Toast.makeText(this, "Usuario correcto", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Usuario correcto", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error procesando la respuesta del servidor", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}