package com.example.appkotlinpostgresql.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // Ruta para insertar datos en el servidor
    @POST("insert")
    suspend fun insertarDatos(@Body datos: Map<String, String>): Response<Any>

    // Ruta para obtener todos los datos desde el servidor
    @GET("data")
    suspend fun obtenerDatos(): Response<List<Map<String, Any>>>
}
