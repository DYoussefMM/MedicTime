package com.example.medictime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        etUsername = findViewById(R.id.etUsuario)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener { loginUser() }
        btnRegister.setOnClickListener { registerUser() }
    }

    private fun loginUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        if (currentUser!= null) {
                            val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
                            prefs.edit().putBoolean("is_authenticated", true).apply()

                            val intent = Intent(this, PerfilActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Error al obtener el usuario actual", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Fallo en el inicio de sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val username = etUsername.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { createUserTask ->
                    if (createUserTask.isSuccessful) {
                        // Actualizar el nombre de usuario del usuario recién registrado
                        val currentUser = auth.currentUser
                        currentUser?.updateProfile(UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build())
                            ?.addOnCompleteListener { updateProfileTask ->
                                if (updateProfileTask.isSuccessful) {
                                    val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
                                    prefs.edit().putBoolean("is_authenticated", true).apply()

                                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Error al actualizar el nombre de usuario: ${updateProfileTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Fallo en el registro: ${createUserTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}