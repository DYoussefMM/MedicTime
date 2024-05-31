package com.example.medictime

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.Memorys.Perfil
import com.google.gson.Gson

class PerfilActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etEdad: EditText

    private val PREF_NAME = "medictime_prefs"
    private val PERFIL_KEY = "perfil"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        etEdad = findViewById(R.id.etEdad)

        // Cargar los datos del perfil guardados previamente
        cargarPerfilGuardado()

        val btnGuardarPerfil: Button = findViewById(R.id.btnGuardarPerfil)
        val btnEliminarPerfil: Button = findViewById(R.id.
        btnEliminarPerfil)

        btnGuardarPerfil.setOnClickListener {
            val nombre = etNombre.text.toString()
            val correo = etCorreo.text.toString()
            val edad = etEdad.text.toString().toIntOrNull()

            if (nombre.isNotEmpty() && correo.isNotEmpty() && edad != null) {
                // Crear un nuevo objeto de perfil
                val perfil = Perfil(nombre, correo, edad)

                // Guardar el perfil en SharedPreferences
                savePerfil(perfil)

                Toast.makeText(this, "Perfil guardado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminarPerfil.setOnClickListener {
            deletePerfil()
        }
    }

    private fun cargarPerfilGuardado() {
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(PERFIL_KEY, null)
        if (json != null) {
            val perfil = gson.fromJson(json, Perfil::class.java)
            // Mostrar los datos del perfil en los EditText
            etNombre.setText(perfil.nombre)
            etCorreo.setText(perfil.correo)
            etEdad.setText(perfil.edad.toString())
        }
    }

    private fun savePerfil(perfil: Perfil) {
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(perfil)
        editor.putString(PERFIL_KEY, json)
        editor.apply()
    }

    private fun deletePerfil() {
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(PERFIL_KEY)
        editor.apply()
        finish()
    }
}