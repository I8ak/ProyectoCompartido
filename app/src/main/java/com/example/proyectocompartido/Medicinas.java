package com.example.proyectocompartido;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Medicinas extends AppCompatActivity {
    private static String barcodeMedicina;
    private static String barcodePaciente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicinas);

        barcodePaciente=getIntent().getStringExtra("codBarPaciente");
        Button button=findViewById(R.id.butonMedicina);
        button.setOnClickListener(v ->{
                    Log.i("MENSAJE","ha entrado a la camara");
                    abrirCamara();
                }
        );
    }

    private void abrirCamara() {
        Intent intent = new Intent(Medicinas.this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            barcodeMedicina = data.getStringExtra(Intents.Scan.RESULT);
            String codigoMedicamento=barcodeMedicina;
            codigoMedicamento = codigoMedicamento.replaceAll("^\u001d+", "");
            String[] codigoPartido=codigoMedicamento.split("\u001d");
            String fechaCad=null,codNacional3=null,codNacional2=null;
            if(codigoPartido.length==3){
                fechaCad=codigoPartido[2].substring(2, 8);
                codNacional3=codigoPartido[2].substring(11);
            }else if(codigoPartido.length==2){
                fechaCad=codigoPartido[1].substring(2, 8);
                codNacional2=codigoPartido[0].substring(9,16);
            }
            LocalDateTime fechaEnvio=LocalDateTime.now();
            String fechaFormateada=fechaEnvio.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            char VT = 11;
            char FS = 28;
            char CR = 13;
            JSONObject jsonsCodBar=new JSONObject();
            try {

                jsonsCodBar.put("codBarPaciente",barcodePaciente);
                jsonsCodBar.put("codBarMedicina2", codNacional2 != null ? codNacional2 : JSONObject.NULL);
                jsonsCodBar.put("codBarMedicina3", codNacional3 != null ? codNacional3 : JSONObject.NULL);
                jsonsCodBar.put("fechaCad",fechaCad);
                jsonsCodBar.put("fechaEnvio",fechaFormateada);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String linea = jsonsCodBar.toString().replaceAll("\\s+","");
            linea=VT+linea+FS+CR;
          new Lanzar(linea, exito -> runOnUiThread(() -> {

                    Toast.makeText(Medicinas.this, exito?"Medicina correcta":"Medicina incorrecta", Toast.LENGTH_LONG).show();

            })).start();
        }
    }

    public static String getBarcodeMedicina() {
        return barcodeMedicina;
    }

    public static void setBarcodeMedicina(String barcodeMedicina) {
        Medicinas.barcodeMedicina = barcodeMedicina;
    }

    public static String getBarcodePaciente() {
        return barcodePaciente;
    }

    public static void setBarcodePaciente(String barcodePaciente) {
        Medicinas.barcodePaciente = barcodePaciente;
    }
}