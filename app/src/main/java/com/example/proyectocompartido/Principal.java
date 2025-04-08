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

public class Principal extends AppCompatActivity {
    private BarcodeScanner scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal2);
        Button button=findViewById(R.id.butonCamara);


        button.setOnClickListener(v ->{
            Intent intent=new Intent(Principal.this,Scanner.class);
            startActivity(intent);
        });
    }
//    private void abrirCamara() {
//        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//
//        cameraProviderFuture.addListener(() -> {
//            try {
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//
//                Preview preview = new Preview.Builder().build();
//                preview.setSurfaceProvider(previewView.getSurfaceProvider());
//
//
//                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//                        .build();
//
//                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
//                    @SuppressLint("UnsafeOptInUsageError")
//                    Image mediaImage = imageProxy.getImage();
//                    if (mediaImage != null) {
//                        InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
//
//                        scanner.process(image)
//                                .addOnSuccessListener(barcodes -> {
//                                    for (Barcode barcode : barcodes) {
//                                        String value = barcode.getRawValue();
//                                        if (value != null) {
////                                            JSONObject jsonsCodBar=new JSONObject();
//                                            Toast.makeText(Principal.this, "Codigo paciente: "+value, Toast.LENGTH_LONG).show();
////                                            try {
////                                                jsonsCodBar.put("codBarPaciente",value);
////                                            } catch (JSONException e) {
////                                                throw new RuntimeException(e);
////                                            }
////                                            String linea = jsonsCodBar.toString().replaceAll("\\s+","");
////                                          new Lanzar(linea, exito -> runOnUiThread(() -> {
////                                              if (exito) {
//                                                Intent intent = new Intent(Principal.this, Scanner.class);
//                                                intent.putExtra("codBarPaciente",value);
//                                                startActivity(intent);
////                                                Toast.makeText(Principal.this, "Paciente correcto", Toast.LENGTH_LONG).show();
//                                //                } else {
//                                //                    Toast.makeText(Principal.this, "PAciente incorrecto", Toast.LENGTH_LONG).show();
//                                //                }
//                            //               })).start();
////                                            Toast.makeText(this, "QR: " + value, Toast.LENGTH_SHORT).show();
//                                            // Puedes cerrar la actividad, abrir otra, etc.
//                                            // finish(); o navegar a otro intent
//                                        }
//                                    }
//                                })
//                                .addOnFailureListener(e -> Log.e("Scanner", "Error al escanear", e))
//                                .addOnCompleteListener(task -> imageProxy.close());
//                    } else {
//                        imageProxy.close();
//                    }
//                });
//
//                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
//
//                cameraProvider.unbindAll();
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
//
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }, ContextCompat.getMainExecutor(this));
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            abrirCamara();
//        } else {
//            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
//        }
//    }



}
