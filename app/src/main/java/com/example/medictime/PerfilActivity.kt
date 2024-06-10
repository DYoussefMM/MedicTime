package com.example.medictime

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.content.Intent

class PerfilActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        auth = FirebaseAuth.getInstance()

        tvUserName = findViewById(R.id.tvUserName)
        tvUserEmail = findViewById(R.id.tvUserEmail)

        val btnLogout: Button = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            auth.signOut()
            val logoutIntent = Intent(this, AuthActivity::class.java)
            startActivity(logoutIntent)
            finish() // Cierra la actividad actual para evitar que el usuario regrese a ella
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            tvUserName.text = currentUser.displayName
            tvUserEmail.text = currentUser.email
        } else {
            Toast.makeText(this, "No estás autenticado", Toast.LENGTH_SHORT).show()
            val loginIntent = Intent(this, AuthActivity::class.java)
            startActivity(loginIntent)
            finish() // Cierra la actividad actual para evitar que el usuario regrese a ella
        }
    }
}
