package com.example.proyectocompartido;


import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private EditText textUser;
    private EditText textPass;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String codbarPaciente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textUser= findViewById(R.id.usuario);
        textPass= findViewById(R.id.password);
        Button button = findViewById(R.id.boton);

//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (nfcAdapter == null) {
//            Log.e("NFC", "El dispositivo no soporta NFC");
//            return;
//        }
//
//        pendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);

        button.setOnClickListener(v -> {
            validarLogin();
        });
    }
    public void validarLogin(){
        JSONObject json=new JSONObject();
        char VT = 11;
        char FS = 28;
        char CR = 13;
        String user = textUser.getText().toString().trim();
        String password = textPass.getText().toString().trim();
        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Usuario o contraseña vacíos", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            json.put("user", user);
            json.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String linea = VT+json.toString().replaceAll("\\s+","")+FS+CR;
        Log.i("MENSAJE",linea);
        new Lanzar(linea,33333, respuestaServidor -> runOnUiThread(() -> {
            manejarRespuestaServidor(respuestaServidor);
        })).start();
    }
    private void manejarRespuestaServidor(String respuesta) {
        try {
            JSONObject json = new JSONObject(respuesta);
            String error = json.optString("error", null);

            if (error != null && !error.equals("null") ) {
                mostrarPopupError(error);
            } else {
                Toast.makeText(this, "Usuario correcto", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(MainActivity.this,Principal.class);
                intent.putExtra("usuario",textUser.getText().toString().trim());
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (nfcAdapter!=null && pendingIntent!=null){
//            nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
//        }
//        if (nfcAdapter == null) {
//            Log.e("NFC", "El dispositivo no soporta NFC o está desactivado");
//            runOnUiThread(() -> texto.setText("Error: NFC no disponible"));
//        }
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (nfcAdapter!=null){
//            nfcAdapter.disableForegroundDispatch(this);
//        }
//        Log.i("Mensaje","Esta en el osPause");
//    }
//
//    @Override
//    public void onNewIntent(@NonNull Intent intent) {
//        super.onNewIntent(intent);
//        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()) ||
//                NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()) ||
//                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//            JSONObject json=new JSONObject();
//            char VT = 11;
//            char FS = 28;
//            char CR = 13;
//            try {
//                json.put("user",textUser.getText());
//                json.put("password",textPass.getText());
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//            String linea = VT+json.toString().replaceAll("\\s+","")+FS+CR;
//            Log.i("MENSAJE",linea);
//            new Lanzar(linea).start();
//            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            if (tag != null && tag.getId() != null) {
//                String cardID = bytesToHex(tag.getId());
//                textUser.setText(cardID);
//                Log.i("NFC", "Tarjeta detectada: " + cardID);
//            } else {
//                Log.i("NFC", "Error: No se pudo leer la tarjeta NFC");
//            }
//
//        }
//    }
//
//    private String bytesToHex(byte[] bytes) {
//        Log.i("Mensaje","Esta en el bytesToHex");
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            hexString.append(String.format("%02X", b));
//        }
//        return hexString.toString();
//    }

}