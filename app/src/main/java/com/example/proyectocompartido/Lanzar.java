package com.example.proyectocompartido;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Lanzar extends  Thread{
    private String mensaje;
    private Callback callback;

    public interface Callback {
        void onResult(boolean exito);
    }

    public Lanzar(String mensaje, Callback callback) {
        this.mensaje = mensaje;
        this.callback = callback;
    }


    @Override
    public void run() {
        String respuesta="";
        String ipalvar="10.35.50.32";
        String ipAlexis="10.245.91.142";
        String iplocal="10.0.2.2";
        Log.i("MENSAJE",mensaje);
        try(Socket socket=new Socket(ipalvar,33334);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            if (mensaje == null) {
                Log.i("MENSAJE", "Mensaje es null antes de enviarlo");
            } else {
                Log.i("MENSAJE", "Enviando: " + mensaje);
                out.println(mensaje);
                out.flush();
            }
                respuesta=in.readLine();
                respuesta = respuesta.replaceAll("[\\u000B\\u001C\\u000D]", "").trim();
                Log.i("MENSAJE","Respuesta: "+respuesta);
                JSONObject jsonRespuesta=new JSONObject(respuesta);
                String error = jsonRespuesta.optString("error");

                boolean exito = (error.equalsIgnoreCase("ok"));

                if (callback != null) {
                    callback.onResult(exito);
                }
                Log.i("MensjaeEnviado",mensaje);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
