package com.example.proyectocompartido;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        Button buttonMedicina = findViewById(R.id.buttonMedicina);
        String usuario=getIntent().getStringExtra("usuario");
        Button buttonTubo = findViewById(R.id.buttonTubo);
        buttonMedicina.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, ScanPaciente.class);
            intent.putExtra("usuario",usuario);
            intent.putExtra("accion",Medicinas.class);
            startActivity(intent);
        });
        buttonTubo.setOnClickListener(v -> {
            Intent intent = new Intent(Principal.this, ScanPaciente.class);
            intent.putExtra("usuario",usuario);
            intent.putExtra("accion",Addtubo.class);
            startActivity(intent);
        });
    }
}