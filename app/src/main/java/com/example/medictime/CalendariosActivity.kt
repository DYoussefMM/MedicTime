package com.example.medictime

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.Adapters.CalendarAdapter
import com.example.medictime.Helpers.FirestoreHelper
import com.example.medictime.Memorys.Calendar
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class CalendariosActivity : AppCompatActivity() {

    private lateinit var calendars: MutableList<Calendar>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendarios)

        val btnCrearCalendario: Button = findViewById(R.id.btnCrearCalendario)
        val lvCalendarios: ListView = findViewById(R.id.lvCalendarios)

        auth = FirebaseAuth.getInstance()
        calendars = mutableListOf()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val bundle = data?.extras
                val calendarList = bundle?.getParcelableArrayList<Calendar>(EXTRA_CALENDARS)
                if (calendarList != null) {
                    calendars.addAll(calendarList)
                    saveCalendars()
                    (lvCalendarios.adapter as CalendarAdapter).notifyDataSetChanged()
                }
            } else {
                Toast.makeText(this, "Error al crear el calendario", Toast.LENGTH_SHORT).show()
            }
        }

        btnCrearCalendario.setOnClickListener {
            val intent = Intent(this, CrearCalendarioActivity::class.java)
            resultLauncher.launch(intent)
        }

        lvCalendarios.adapter = CalendarAdapter(calendars, this)

        lvCalendarios.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, MostrarCalendarioActivity::class.java)
            intent.putExtra(EXTRA_CALENDAR, calendars[position])
            startActivity(intent)
        }

        lvCalendarios.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            val calendar = calendars[position]
            if (calendar.isShared) {
                val recipientEmail = auth.currentUser?.email ?: ""
                deleteSharedCalendar(calendar, recipientEmail)
            } else {
                deleteCalendar(calendar)
            }
            true
        }

        calendars.clear()

        // Cargar calendarios personales del usuario actual
        loadCalendars()

        // Cargar calendarios compartidos
        loadSharedCalendars()
    }

    private fun saveCalendars() {
        calendars.forEach { calendar ->
            FirestoreHelper.saveCalendar(calendar,
                onSuccess = {
                    // Success handling
                    Toast.makeText(this, "Calendario guardado", Toast.LENGTH_SHORT).show()
                },
                onFailure = { _ ->
                    // Failure handling
                    Toast.makeText(this, "Error al guardar el calendario", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun loadCalendars() {
        FirestoreHelper.loadCalendars({ loadedCalendars ->
            calendars.addAll(loadedCalendars)
            (findViewById<ListView>(R.id.lvCalendarios).adapter as CalendarAdapter).notifyDataSetChanged()
        }, {
            // Failure handling
            Toast.makeText(this, "Error al cargar los calendarios personales", Toast.LENGTH_SHORT).show()
        })
    }

    private fun loadSharedCalendars() {
        val recipientEmail = auth.currentUser?.email ?: ""
        FirestoreHelper.loadSharedCalendars(recipientEmail,
            onSuccess = { sharedCalendars ->
                calendars.addAll(sharedCalendars)
                (findViewById<ListView>(R.id.lvCalendarios).adapter as CalendarAdapter).notifyDataSetChanged()
            },
            onFailure = { exception ->
                Toast.makeText(this, "Error al cargar los calendarios compartidos: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun deleteCalendar(calendar: Calendar) {
        calendars.remove(calendar)
        FirestoreHelper.deleteCalendar(calendar,
            onSuccess = {
                // Success handling
                Toast.makeText(this, "Calendario eliminado", Toast.LENGTH_SHORT).show()
            },
            onFailure = { _ ->
                // Failure handling
                Toast.makeText(this, "Error al eliminar el calendario", Toast.LENGTH_SHORT).show()
            }
        )
        (findViewById<ListView>(R.id.lvCalendarios).adapter as CalendarAdapter).notifyDataSetChanged()
    }

    private fun deleteSharedCalendar(calendar: Calendar, recipientEmail: String) {
        calendars.remove(calendar)
        FirestoreHelper.deleteSharedCalendar(calendar, recipientEmail,
            onSuccess = {
                // Success handling
                Toast.makeText(this, "Calendario compartido eliminado", Toast.LENGTH_SHORT).show()
            },
            onFailure = { _ ->
                // Failure handling
                Toast.makeText(this, "Error al eliminar el calendario compartido", Toast.LENGTH_SHORT).show()
            }
        )
        (findViewById<ListView>(R.id.lvCalendarios).adapter as CalendarAdapter).notifyDataSetChanged()
    }

    companion object {
        const val EXTRA_CALENDARS = "extra_calendars"
        const val EXTRA_CALENDAR = "extra_calendar"
    }
}
