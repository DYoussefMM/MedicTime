package com.example.medictime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.Memorys.Calendar
import com.google.firebase.auth.FirebaseAuth

class CrearCalendarioActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnCreate: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_calendarios)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnCreate = findViewById(R.id.btnCreate)
        auth = FirebaseAuth.getInstance()

        btnCreate.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()
            val creator = auth.currentUser?.email ?: "Desconocido"
            val calendar = Calendar(title, description, creator)
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra(CalendariosActivity.EXTRA_CALENDARS, arrayListOf(calendar))
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}


