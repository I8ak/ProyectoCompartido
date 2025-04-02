package com.example.proyectocompartido;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textUser= findViewById(R.id.usuario);
        textPass= findViewById(R.id.password);
        texto = findViewById(R.id.mensaje);
        button=findViewById(R.id.boton);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost/pruebaApp/")
                        .addConverterFactory(GsonConverterFactory.create())
                                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        button.setOnClickListener(v -> {
            JSONObject json=new JSONObject();
            char VT = 11;
            char FS = 28;
            char CR = 13;
            try {
                json.put("user",textUser.getText());
                json.put("password",textPass.getText());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String linea = VT+json.toString().replaceAll("\\s+","")+FS+CR;
            Log.i("MENSAJE",linea);
            new Lanzar(linea).start();

        });

    }
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
                Log.i("MENSAJE",respuesta);
                final String finalRespuesta =respuesta ;
                runOnUiThread(() -> texto.setText(finalRespuesta));

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}