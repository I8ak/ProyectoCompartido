package com.example.proyectocompartido;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
            String usuario = textUser.getText().toString();
            String contrasenia = textPass.getText().toString();

            Call<Response> call = apiService.verificarUsuario(usuario, contrasenia);

            if (usuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, Response<Response> response) {
                    if (response.isSuccessful()) {
                        // Verificar si la respuesta fue exitosa
                        Response respuesta = response.body();
                        if (respuesta != null && respuesta.isSuccessful()) {
                            texto.setText("¡Usuario autenticado!");
                        } else {
                            texto.setText("Usuario o contraseña incorrectos");
                        }
                    } else {
                        texto.setText("Error al comunicarse con el servidor");
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    texto.setText("Fallo en la conexión");
                }
            });
        });
    }
}