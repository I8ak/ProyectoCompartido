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

public class Servidor{
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(12345);
            System.out.println("Servidor esperando conexiones...");

            while (true) {
                Socket cliente = server.accept();
                System.out.println("Nuevo cliente conectado");

                PrintWriter pw = new PrintWriter(cliente.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

                String linea = br.readLine();
                if (linea == null) {
                    System.out.println("Cliente envió null");
                    continue;
                }

                // Limpiar caracteres especiales como VT, FS y CR si es necesario
                linea = linea.replaceAll("[\\u000B\\u001C\\u000D]", "").trim();
                System.out.println("Recibido: " + linea);

                JSONObject json = new JSONObject(linea);
                String mensaje;

                if (json.has("user") && json.has("password")) {
                    // Primer mensaje: login
                    String user = json.getString("user");
                    String password = json.getString("password");

                    if (user.equals("1") && password.equals("1")) {
                        File file = new File("./prueba.json");
                        String contenidoJson = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                        mensaje = contenidoJson.replaceAll("\\s+", "");
                    } else {
                        mensaje = "{\"error\":\"error\"}";
                    }

                    pw.println(mensaje);
                    pw.flush();

                } else if (json.has("codBarPaciente")) {
                    // Segundo mensaje: código de barras
                    String codBarPaciente = json.getString("codBarPaciente");
                    if (codBarPaciente.equals("76060406")) {
                        mensaje = "{\"error\":\"null\"}";
                    } else {
                        mensaje = "{\"error\":\"No existe este paciente\"}";
                    }
                    pw.println(mensaje);
                    pw.flush();
                } else {
                    mensaje = "{\"error\":\"formato desconocido\"}";
                    pw.println(mensaje);
                    pw.flush();
                }
                pw.close();
                br.close();
                cliente.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
