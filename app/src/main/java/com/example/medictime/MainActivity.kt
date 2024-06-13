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

        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val isAuthenticated = prefs.getBoolean("is_authenticated", false)

        if (!isAuthenticated) {
            // El usuario no está autenticado, llevarlo a la AuthActivity
            onStop()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnCalendarios: Button = findViewById(R.id.btnCalendarios)
        val btnPerfil: Button = findViewById(R.id.btnPerfil)

        btnCalendarios.isEnabled = isAuthenticated

        btnCalendarios.setOnClickListener {
            if (isAuthenticated) {
                val intent = Intent(this, CalendariosActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Debes iniciar sesion para acceder a los calendarios", Toast.LENGTH_SHORT).show()
            }
        }


        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
    }
}