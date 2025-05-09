package com.example.proyectocompartido;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Medicinas extends AppCompatActivity {

    private String barcodePaciente;
    private  String usuario;
    private String barcodeMedicina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicinas);
         usuario=getIntent().getStringExtra("usuario");


        String tipo = getIntent().getStringExtra("tipo");
        String codigo = getIntent().getStringExtra("codEscaneado");

        if ("paciente".equals(tipo)) {
            barcodePaciente = codigo;
            Log.i("mensaje", "Paciente escaneado: " + barcodePaciente);
        } else if ("medicina".equals(tipo)) {
            barcodeMedicina = codigo;
            Log.i("mensaje", "Medicina escaneada: " + barcodeMedicina);
            validarMedicina();
        }

        Button button = findViewById(R.id.butonMedicina);
        button.setOnClickListener(v -> {
                Intent intent = new Intent(Medicinas.this, Scanner.class);
                intent.putExtra("accion",Medicinas.class);
                intent.putExtra("tipo", "medicina");
                intent.putExtra("usuario", usuario);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
        });
    }



    private void validarMedicina() {
        LocalDateTime fechaEnvio = LocalDateTime.now();
        String fechaFormateada = fechaEnvio.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fechaCaducidad=null;
        char VT = 11;
        char FS = 28;
        char CR = 13;

        JSONObject json = new JSONObject();
        try {
            json.put("usuario", usuario);
            json.put("operacion", "validar");
            json.put("nhc", barcodePaciente);
            json.put("codBarMedicina1", barcodeMedicina);
            json.put("codBarMedicina2", JSONObject.NULL);
            json.put("fechaCad", JSONObject.NULL);
            json.put("fechaEnvio", fechaFormateada);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String linea = json.toString().replaceAll("\\s+", "");
        linea = VT + linea + FS + CR;

        Thread hilo = new Lanzar(
                linea,
                33335,
                respuestaServidor -> runOnUiThread(() -> manejarRespuestaServidor(respuestaServidor)),
                error -> runOnUiThread(() -> mostrarPopupError(error))
        );
        hilo.start();
    }

    private void manejarRespuestaServidor(String respuesta) {
        try {
            JSONObject json = new JSONObject(respuesta);
            String resultado = json.optString("resultado", null);
            String error = json.optString("error", null);

            if (error != null && !error.equals("null") && (resultado == null || !resultado.equalsIgnoreCase("OK"))) {
                mostrarPopupError(error);
            } else {
                Toast.makeText(this, "Medicina correcta", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Medicinas.this,AdministrarMedicina.class);
                intent.putExtra("respuesta",respuesta);
                startActivity(intent);
            }

        } catch (JSONException e) {
            Toast.makeText(this, "Error procesando la respuesta del servidor", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void mostrarPopupError(String mensajeError) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error de validación")
                .setMessage(mensajeError)
                .setPositiveButton("Aceptar", null)
                .setCancelable(true)
                .show();
    }
}
