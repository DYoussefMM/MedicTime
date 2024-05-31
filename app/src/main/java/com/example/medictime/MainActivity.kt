package com.example.medictime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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