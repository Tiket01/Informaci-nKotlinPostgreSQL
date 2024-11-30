package com.example.appkotlinpostgresql

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkotlinpostgresql.adapter.DatosAdapter
import com.example.appkotlinpostgresql.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Variables para los elementos de la interfaz
    private lateinit var etNombres: EditText
    private lateinit var etPrimerApellido: EditText
    private lateinit var etSegundoApellido: EditText
    private lateinit var etEdad: EditText
    private lateinit var btnEnviar: Button
    private lateinit var rvDatos: RecyclerView

    // Lista para almacenar los datos recuperados
    private val datosList = mutableListOf<Map<String, Any>>()
    private lateinit var adapter: DatosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular elementos de la interfaz
        etNombres = findViewById(R.id.etNombres)
        etPrimerApellido = findViewById(R.id.etPrimerApellido)
        etSegundoApellido = findViewById(R.id.etSegundoApellido)
        etEdad = findViewById(R.id.etEdad)
        btnEnviar = findViewById(R.id.btnEnviar)
        rvDatos = findViewById(R.id.rvDatos)

        // Configurar el RecyclerView
        configurarRecyclerView()

        // Acción del botón para enviar los datos
        btnEnviar.setOnClickListener {
            val nombres = etNombres.text.toString()
            val primerApellido = etPrimerApellido.text.toString()
            val segundoApellido = etSegundoApellido.text.toString()
            val edad = etEdad.text.toString().toIntOrNull()

            // Validar campos
            if (nombres.isEmpty() || primerApellido.isEmpty() || segundoApellido.isEmpty() || edad == null) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                enviarDatos(nombres, primerApellido, segundoApellido, edad)
            }
        }

        // Cargar datos al iniciar la actividad
        obtenerDatos()
    }

    // Configurar el RecyclerView
    private fun configurarRecyclerView() {
        rvDatos.layoutManager = LinearLayoutManager(this)
        adapter = DatosAdapter(datosList)
        rvDatos.adapter = adapter
    }

    // Función para enviar datos al servidor
    private fun enviarDatos(nombres: String, primerApellido: String, segundoApellido: String, edad: Int) {
        val fechaNacimiento = "1990-01-01" // Cambia esto si es necesario
        val hora = "12:00:00" // Cambia esto si necesitas obtener la hora actual
        val fechaRegistro = "2024-01-01" // Fecha actual (puedes calcularla automáticamente)
        val estado = 1

        // Mapa con los datos a enviar
        val datos = mapOf(
            "nombres" to nombres,
            "primer_apellido" to primerApellido,
            "segundo_apellido" to segundoApellido,
            "edad" to edad.toString(),
            "fecha_nacimiento" to fechaNacimiento,
            "hora" to hora,
            "fecha_registro" to fechaRegistro,
            "estado" to estado.toString()
        )

        // Realizar la solicitud al servidor
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.insertarDatos(datos)
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Datos enviados correctamente", Toast.LENGTH_SHORT).show()
                        obtenerDatos() // Actualizar la lista de datos
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error al enviar los datos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Función para obtener datos del servidor
    private fun obtenerDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Realizar la solicitud al servidor
                val response = RetrofitClient.apiService.obtenerDatos()

                // Verificar si la respuesta es exitosa
                if (response.isSuccessful) {
                    val datos = response.body() ?: emptyList()

                    // Imprime la respuesta para depurar
                    Log.d("RespuestaServidor", datos.toString())

                    // Actualizar la lista de datos en la interfaz de usuario
                    runOnUiThread {
                        datosList.clear()
                        datosList.addAll(datos)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    // Manejar errores de la respuesta
                    Log.e("ErrorServidor", "Error al obtener los datos: ${response.message()}")
                }
            } catch (e: Exception) {
                // Manejar excepciones
                e.printStackTrace()
                Log.e("ErrorExcepción", "Excepción al obtener los datos: ${e.message}")
            }
        }
    }
}
