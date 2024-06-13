package com.example.medictime

// MainActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCalendarios: Button = findViewById(R.id.btnCalendarios)
        val btnPerfil: Button = findViewById(R.id.btnPerfil)

        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val isAuthenticated = prefs.getBoolean("is_authenticated", false)

        if (!isAuthenticated) {
            // Deshabilitar el botón btnCalendarios ya que el usuario no está autenticado
            btnCalendarios.isEnabled = false
            // Redirigir a AuthActivity para autenticación
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Habilitar el botón btnCalendarios si el usuario está autenticado
            btnCalendarios.isEnabled = true

            btnCalendarios.setOnClickListener {
                val intent = Intent(this, CalendariosActivity::class.java)
                startActivity(intent)
            }

            btnPerfil.setOnClickListener {
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
            }
        }


        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
    }
}