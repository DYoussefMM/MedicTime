package com.example.medictime

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.Memorys.Calendar
import com.google.firebase.auth.FirebaseAuth
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import com.shrikanthravi.collapsiblecalendarview.data.Day
import java.util.Calendar as JavaCalendar

class MostrarCalendarioActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvUserName: TextView
    private lateinit var collapsibleCalendar: CollapsibleCalendar
    private lateinit var btnAddEvents: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_calendario)

        auth = FirebaseAuth.getInstance()
        tvUserName = findViewById(R.id.tvUserName)
        collapsibleCalendar = findViewById(R.id.collapsibleCalendar)
        btnAddEvents = findViewById(R.id.btnAddEvents)

        val calendar = intent.getParcelableExtra<Calendar>(CalendariosActivity.EXTRA_CALENDAR)
        val currentUser = auth.currentUser
        tvUserName.text = currentUser?.displayName ?: "Usuario"

        btnAddEvents.setOnClickListener {
            calendar?.let {
                addCommonEvents()
            }
        }
    }

    private fun addCommonEvents() {
        val today = JavaCalendar.getInstance()

        // Evento para tomar medicamento por la mañana
        addEventToCalendar(today, 8)

        // Evento para tomar medicamento por la tarde
        addEventToCalendar(today, 12)

        // Evento para tomar medicamento por la noche
        addEventToCalendar(today, 20)
    }

    private fun addEventToCalendar(calendar: JavaCalendar, hour: Int) {
        val eventDay = calendar.clone() as JavaCalendar
        eventDay.set(JavaCalendar.HOUR_OF_DAY, hour)
        eventDay.set(JavaCalendar.MINUTE, 0)
        eventDay.set(JavaCalendar.SECOND, 0)

        val day = Day(eventDay.get(JavaCalendar.YEAR), eventDay.get(JavaCalendar.MONTH), eventDay.get(JavaCalendar.DAY_OF_MONTH))
        collapsibleCalendar.addEventTag(day.year, day.month, day.day)
    }
}






