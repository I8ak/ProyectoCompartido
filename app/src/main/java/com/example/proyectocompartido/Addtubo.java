package com.example.proyectocompartido;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Addtubo extends AppCompatActivity {
    private  String barcodePaciente;
    private  String usuario;
    private  String barcodTubo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addtubo);
         usuario=getIntent().getStringExtra("usuario");

        String tipo = getIntent().getStringExtra("tipo");
        String codigo = getIntent().getStringExtra("codEscaneado");

        if ("paciente".equals(tipo)) {
            barcodePaciente = codigo;
            Log.i("mensaje", "Paciente escaneado: " + barcodePaciente);
        } else if ("tubo".equals(tipo)) {
            barcodTubo = codigo;
            Log.i("mensaje", "Tubo escaneada: " + barcodTubo);
            validarTubo();
        }

        Button button = findViewById(R.id.buttonTubo);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(Addtubo.this, Scanner.class);
            intent.putExtra("accion",Addtubo.class);
            intent.putExtra("tipo", "tubo");
            intent.putExtra("usuario", usuario);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }
    private void validarTubo() {
        LocalDateTime fechaEnvio = LocalDateTime.now();
        String fechaFormateada = fechaEnvio.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        char VT = 11;
        char FS = 28;
        char CR = 13;

        JSONObject json = new JSONObject();
        try {
            json.put("usuario", usuario);
            json.put("operacion", "añadir");
            json.put("nhc", barcodePaciente);
            json.put("codBarTubo", barcodTubo);
            json.put("fechaEnvio", fechaFormateada);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String linea = json.toString().replaceAll("\\s+", "");
        linea = VT + linea + FS + CR;

        Thread hilo = new Lanzar(
                linea,
                33336,
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
                Toast.makeText(this, "Tubo correcta", Toast.LENGTH_LONG).show();
//                Intent intent=new Intent(Addtubo.this,AdministrarMedicina.class);
//                intent.putExtra("respuesta",respuesta);
//                startActivity(intent);
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