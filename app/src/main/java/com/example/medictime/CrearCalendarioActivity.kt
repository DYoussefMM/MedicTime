package com.example.medictime

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.Memorys.Calendar

class CrearCalendarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_calendarios)

        // Create a new Calendar object
        val calendar = Calendar("New Calendar", "Description")

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