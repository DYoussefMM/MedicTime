package com.example.medictime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.Memorys.Calendar

class CrearCalendarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_calendarios)

        val etTitle = findViewById<EditText>(R.id.etTitulo)
        val etDescription = findViewById<EditText>(R.id.etFecha)

        // Create a button to save the new calendar
        val btnSave = findViewById<Button>(R.id.btnGuardar)
        btnSave.setOnClickListener {
            // Create a new Calendar object with the user's input
            val calendar = Calendar(etTitle.text.toString(), etDescription.text.toString())

            // Create a bundle to pass the calendar list
            val bundle = Bundle()
            val calendarList = arrayListOf(calendar)
            bundle.putParcelableArrayList(CalendariosActivity.EXTRA_CALENDARS, calendarList)

            // Create an intent to pass the bundle
            val intent = Intent()
            intent.putExtras(bundle)

            // Set the result and finish the activity
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}