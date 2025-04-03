package com.example.proyectocompartido;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText textUser;
    private EditText textPass;
    private TextView texto;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textUser= findViewById(R.id.usuario);
        textPass= findViewById(R.id.password);
        texto = findViewById(R.id.mensaje);
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
            Intent intent = new Intent(MainActivity.this, Principal.class);
            startActivity(intent);
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

        });

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
//
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
    private class Lanzar extends  Thread{
        private String mensaje;
        public Lanzar(String mensaje){
            this.mensaje=mensaje;
        }

        @Override
        public void run() {
            String respuesta="";
            try(Socket socket=new Socket("10.35.50.32",33333);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                out.println(mensaje);
                out.flush();

                respuesta=in.readLine();
                respuesta = respuesta.replaceAll("[\\u000B\\u001C\\u000D]", "").trim();
                Log.i("MENSAJE",respuesta);
                final String finalRespuesta =respuesta ;
                JSONObject jsonRespuesta=new JSONObject(respuesta);
                String error = jsonRespuesta.optString("error", null);

//                if (error.equalsIgnoreCase("null")) {
//                    Intent intent = new Intent(MainActivity.this, Principal.class);
//                    startActivity(intent);
//                } else {
//                    runOnUiThread(() -> texto.setText(error));
//                }

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}