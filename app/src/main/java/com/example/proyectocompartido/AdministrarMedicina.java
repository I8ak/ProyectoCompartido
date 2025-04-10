package com.example.proyectocompartido;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AdministrarMedicina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrar_medicina);

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String respuesta=getIntent().getStringExtra("respuesta");
        HashMap<String,String> list= new HashMap<>();
        try {
            JSONObject data=new JSONObject(respuesta);
            JSONArray administr=data.getJSONArray("administr");
            for (int i = 0; i < administr.length(); i++) {
                JSONObject a=administr.getJSONObject(i);
                String id=a.getString("id");
                String fechaAdministracion=a.getString("fechaAdministracion");
                list.put(id,fechaAdministracion);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        List<String> listaValores = new ArrayList<>(list.values());

        MyAdapter adapter=new MyAdapter(listaValores);
        recyclerView.setAdapter(adapter);


    }
}