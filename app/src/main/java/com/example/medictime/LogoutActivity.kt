package com.example.medictime

// LogoutActivity.kt
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LogoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        prefs.edit().putBoolean("is_authenticated", false).apply()

        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}