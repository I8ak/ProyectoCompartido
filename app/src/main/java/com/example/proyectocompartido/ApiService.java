package com.example.proyectocompartido;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("getUsuarios.php")
    Call<List<Usuario>> obtenerUsuarios();
}
