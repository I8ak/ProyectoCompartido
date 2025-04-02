package com.example.proyectocompartido;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("getUsuarios.php")
    Call<List<Usuario>> obtenerUsuarios();

    @POST("login.php")
    Call<ResponseBody> verificarUsuario(@Query("user") String user, @Query("pass") String pass);
}
