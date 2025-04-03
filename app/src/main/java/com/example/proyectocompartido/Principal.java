package com.example.proyectocompartido;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.io.IOException;
import java.util.List;

public class Principal extends AppCompatActivity {
    private ImageView imageView;
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Log.i("MENSAJE","Ha entrado al getresultcode");
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data"); // Obtener imagen en baja calidad
                        imageView.setImageBitmap(imageBitmap); // Mostrar imagen en ImageView
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal2);

        Button button=findViewById(R.id.butonCamara);
        imageView = findViewById(R.id.imageView);
        button.setOnClickListener(v ->{
                    Log.i("MENSAJE","ha entrado a la camara");
                    abrirCamara();
                }
                );


    }
    private void abrirCamara() {
        Intent intent = new Intent(Principal.this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }


@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK){
        String barcode = data.getStringExtra(Intents.Scan.RESULT);
        Toast.makeText(this,"Encontrado: " + barcode,Toast.LENGTH_LONG).show();
    }
}
}
