package com.example.proyectocompartido;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Scanner extends AppCompatActivity {
    private BarcodeScanner scanner;
    private static String usuario;
    private PreviewView previewView;
    private static String barcodePaciente;
    private boolean escaneoHecho = false;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scanner);
        previewView = findViewById(R.id.previewView);

        usuario=getIntent().getStringExtra("usuario");
        scanner = BarcodeScanning.getClient();
            Log.i("MENSAJE","ha entrado a la camara");
            abrirCamara();
    }
    private void abrirCamara() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());


                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                    @SuppressLint("UnsafeOptInUsageError")
                    Image mediaImage = imageProxy.getImage();
                    if (mediaImage != null) {
                        InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

                        scanner.process(image)
                                .addOnSuccessListener(barcodes -> {
                                    for (Barcode barcode : barcodes) {
                                        if (!escaneoHecho) {
                                            escaneoHecho = true;
                                            String value = barcode.getRawValue();
                                            if (value != null) {
                                                Intent intent = new Intent(Scanner.this, Medicinas.class);
                                                intent.putExtra("codEscaneado", value);
                                                if (usuario!=null){

                                                    intent.putExtra("usuario",usuario);
                                                }
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> Log.e("Scanner", "Error al escanear", e))
                                .addOnCompleteListener(task -> imageProxy.close());
                    } else {
                        imageProxy.close();
                    }
                });

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(this));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            if (!isFinishing() && !isDestroyed()) {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }

        }
    }



}
