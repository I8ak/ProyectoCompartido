package com.example.proyectocompartido;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.barcode.BarcodeScanner;

public class ScanPaciente extends AppCompatActivity {
    private BarcodeScanner scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addmedicina);
        Button button = findViewById(R.id.butonCamara);
        String usuario=getIntent().getStringExtra("usuario");
        Class<?> accion = (Class<?>) getIntent().getSerializableExtra("accion");
        Log.i("Mensaje",usuario);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(ScanPaciente.this, Scanner.class);
            intent.putExtra("usuario",usuario);
            intent.putExtra("accion",accion);
            startActivity(intent);
        });
    }
}
