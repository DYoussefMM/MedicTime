package com.example.medictime

// MainActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        if (!prefs.getBoolean("is_authenticated", false)) {
            // El usuario no está autenticado, llevarlo a la AuthActivity
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnCalendarios: Button = findViewById(R.id.btnCalendarios)
        val btnPerfil: Button = findViewById(R.id.btnPerfil)

        btnCalendarios.setOnClickListener {
            val intent = Intent(this, CalendariosActivity::class.java)
            startActivity(intent)
        }

        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
    }
}