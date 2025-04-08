package com.example.proyectocompartido;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class Principal extends AppCompatActivity {
    private static String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal2);
        Button button=findViewById(R.id.butonCamara);
        button.setOnClickListener(v ->{
            Log.i("MENSAJE","ha entrado a la camara");
            abrirCamara();
        });
    }
    private void abrirCamara() {
        Intent intent = new Intent(Principal.this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            barcode = data.getStringExtra(Intents.Scan.RESULT);
            JSONObject jsonsCodBar=new JSONObject();
            Toast.makeText(Principal.this, "Codigo paciente: "+barcode, Toast.LENGTH_LONG).show();
            try {
                jsonsCodBar.put("codBarPaciente",barcode);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String linea = jsonsCodBar.toString().replaceAll("\\s+","");
//            new Lanzar(linea, exito -> runOnUiThread(() -> {
//                if (exito) {
                    Intent intent = new Intent(Principal.this, Medicinas.class);
                    intent.putExtra("codBarPaciente",barcode);
                    startActivity(intent);
                    Toast.makeText(Principal.this, "Paciente correcto", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(Principal.this, "PAciente incorrecto", Toast.LENGTH_LONG).show();
//                }
//            })).start();
        }
    }

    public static String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        Principal.barcode = barcode;
    }


}
