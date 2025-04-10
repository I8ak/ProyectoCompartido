package com.example.proyectocompartido;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class Lanzar extends Thread{
    private String mensaje;
    private Consumer<String>  callback;
    private int puerto;


    public Lanzar(String mensaje,int puerto , Consumer<String> callback) {
        this.mensaje = mensaje;
        this.callback = callback;
        this.puerto=puerto;
    }


    @Override
    public void run() {
        String respuesta="";
        String ipalvar="10.35.50.32";
        String ipAlexis="10.245.91.142";
        String iplocal="10.0.2.2";
        Log.i("MENSAJE",mensaje);
        try(Socket socket=new Socket(ipalvar,puerto);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Log.i("mensaje", String.valueOf(socket.getLocalPort()));
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
                if (callback != null) {
                    callback.accept(respuesta);
                }
                Log.i("MensjaeEnviado",mensaje);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
