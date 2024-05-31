package com.example.medictime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.Adapters.CalendarAdapter
import com.example.medictime.Adapters.SharedPreferencesHelper
import com.example.medictime.Memorys.Calendar

class CalendariosActivity : AppCompatActivity() {

    private lateinit var calendars: MutableList<Calendar>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendarios)

        val btnCrearCalendario: Button = findViewById(R.id.btnCrearCalendario)
        val lvCalendarios: ListView = findViewById(R.id.lvCalendarios)

        calendars = SharedPreferencesHelper.getCalendars(this).toMutableList()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val bundle = data?.extras
                val calendarList = bundle?.getParcelableArrayList<Calendar>(EXTRA_CALENDARS)
                calendarList?.let { calendars.addAll(it) }
                SharedPreferencesHelper.saveCalendars(this, calendars)
                (lvCalendarios.adapter as CalendarAdapter).notifyDataSetChanged()
            }
        }

        btnCrearCalendario.setOnClickListener {
            val intent = Intent(this, CrearCalendarioActivity::class.java)
            resultLauncher.launch(intent)
        }

        lvCalendarios.adapter = CalendarAdapter(calendars, this)

        lvCalendarios.setOnItemLongClickListener { _, _, position, _ ->
            deleteCalendar(position)
            true
        }
    }

    private fun deleteCalendar(position: Int) {
        calendars.removeAt(position)
        SharedPreferencesHelper.saveCalendars(this, calendars)
        (findViewById<ListView>(R.id.lvCalendarios).adapter as CalendarAdapter).notifyDataSetChanged()
    }

    companion object {
        const val EXTRA_CALENDARS = "extra_calendars"
    }
}