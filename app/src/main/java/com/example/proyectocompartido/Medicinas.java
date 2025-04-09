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

    private static String barcodePaciente;
    private static String usuario;
    private String barcodeMedicina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicinas);
        usuario =getIntent().getStringExtra("usuario");
        // Recoger código escaneado del paciente una sola vez
        if (barcodePaciente == null) {
            barcodePaciente = getIntent().getStringExtra("codEscaneado");
            Log.i("mensaje",barcodePaciente+" uduario "+usuario);
        }else {
            barcodeMedicina = getIntent().getStringExtra("codEscaneado");
            Log.i("mensaje",barcodeMedicina);
            validarMedicina();
        }

        Button button = findViewById(R.id.butonMedicina);
        button.setOnClickListener(v -> {
                Intent intent = new Intent(Medicinas.this, Scanner.class);
                startActivity(intent);

            // Lanzar la actividad Scanner para escanear la medicina

        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_SCAN && resultCode == RESULT_OK && data != null) {
//            // Aquí recibimos el código escaneado de Scanner
//            barcodeMedicina = data.getStringExtra("codEscaneado");
//            Log.i("mensaje", barcodeMedicina);
//            validarMedicina();
//        }
//    }

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
            json.put("fechaCad", fechaCaducidad != null ? fechaCaducidad : JSONObject.NULL);
            json.put("fechaEnvio", fechaFormateada);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String linea = json.toString().replaceAll("\\s+", "");
        linea = VT + linea + FS + CR;

        new Lanzar(linea,33334, respuestaServidor -> runOnUiThread(() -> {
            manejarRespuestaServidor(respuestaServidor);
        })).start();
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
