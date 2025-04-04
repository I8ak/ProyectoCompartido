package com.example.proyectocompartido;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Servidor {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(12345);
            Socket cliente = server.accept();
            System.out.println("Ishak conectado");
            PrintWriter pw = new PrintWriter(cliente.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            JSONObject json = new JSONObject(br.readLine().toString());
            String user = json.getString("user");
            String password = json.getString("password");
            String mensaje;
            if(user.equals("1") && password.equals("1")){
                File file = new File("./prueba.json");
                String contenidoJson = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                mensaje = contenidoJson.replaceAll("\\s+", "");
                pw.println(mensaje);
                pw.flush();
                String mensaje2 = br.readLine();
                System.out.println(mensaje2);
                JSONObject json2 = new JSONObject(mensaje2);
                String codBarPaciente = json2.getString("codBarPaciente");
                if (codBarPaciente.equals("76060406")) {
                    mensaje = "{\"error\":\"null\"}";
                } else {
                    mensaje = "{\"error\":\"No existe este paciente\"}";
                }
                pw.println(mensaje);
                pw.flush();
            }else {
                mensaje = "{\"error\":\"error\"}";
                pw.println(mensaje);
                pw.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
