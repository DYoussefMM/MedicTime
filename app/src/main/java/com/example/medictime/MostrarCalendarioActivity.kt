package com.example.medictime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.medictime.Helpers.FirestoreHelper
import com.example.medictime.Memorys.Calendar
import com.google.firebase.auth.FirebaseAuth
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.Calendar as JavaCalendar

@Suppress("DEPRECATION")
class MostrarCalendarioActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvUserName: TextView
    private lateinit var collapsibleCalendar: CollapsibleCalendar
    private lateinit var btnAddEvents: Button
    private lateinit var btnShareCalendar: Button
    private lateinit var selectedCalendar: Calendar
    private lateinit var selectedDate: JavaCalendar
    private lateinit var selectedTime: JavaCalendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_calendario)

        auth = FirebaseAuth.getInstance()
        tvUserName = findViewById(R.id.tvUserName)
        collapsibleCalendar = findViewById(R.id.collapsibleCalendar)
        btnAddEvents = findViewById(R.id.btnAddEvents)
        btnShareCalendar = findViewById(R.id.btnShareCalendar)

        selectedCalendar = intent.getParcelableExtra(CalendariosActivity.EXTRA_CALENDAR)
            ?: throw IllegalArgumentException("Calendar data is required")

        val currentUser = auth.currentUser
        tvUserName.text = currentUser?.displayName ?: "Usuario"

        loadCalendarEvents()

        btnAddEvents.setOnClickListener {
            showAddEventDialog()
        }

        btnShareCalendar.setOnClickListener {
            showShareCalendarDialog()
        }

        collapsibleCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDaySelect() {
                val selectedDay = collapsibleCalendar.selectedDay ?: return
                val selectedDayCalendar = JavaCalendar.getInstance().apply {
                    set(selectedDay.year, selectedDay.month, selectedDay.day)
                }
                val eventDate = selectedDayCalendar.timeInMillis

                val eventsForDay = selectedCalendar.events.filter { event ->
                    isSameDay(event.date, eventDate)
                }

                if (eventsForDay.isNotEmpty()) {
                    val eventsStringBuilder = StringBuilder()
                    eventsForDay.forEachIndexed { index, event ->
                        eventsStringBuilder.append("${event.title}: ${event.description}")
                        if (index < eventsForDay.size - 1) {
                            eventsStringBuilder.append("\n")
                        }
                    }
                    Toast.makeText(this@MostrarCalendarioActivity, eventsStringBuilder.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MostrarCalendarioActivity, "No hay eventos para este día", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDataUpdate() {
                // Optional implementation
            }

            override fun onDayChanged() {
                // Implement your logic here when the day changes
            }

            override fun onMonthChange() {
                // Optional implementation
            }

            override fun onWeekChange(position: Int) {
                // Optional implementation
            }

            override fun onItemClick(v: View) {
                // Implement your logic here when an item is clicked
            }

            override fun onClickListener() {
                // Implement onClickListener method
                // This method will be invoked when an element of the calendar is clicked
            }
        })
    }

    private fun loadCalendarEvents() {
        // Implement how to load calendar events, if necessary
        // No need to add events to CollapsibleCalendar directly, as it is handled internally
    }

    private fun showAddEventDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Agregar Evento")

        val view = layoutInflater.inflate(R.layout.dialog_add_event, null)
        builder.setView(view)

        val etEventTitle: EditText = view.findViewById(R.id.etEventTitle)
        val etEventDescription: EditText = view.findViewById(R.id.etEventDescription)
        val btnDatePicker: Button = view.findViewById(R.id.btnDatePicker)
        val btnTimePicker: Button = view.findViewById(R.id.btnTimePicker)

        btnDatePicker.setOnClickListener {
            val calendar = JavaCalendar.getInstance()
            val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                selectedDate = JavaCalendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }
            }, calendar.get(JavaCalendar.YEAR), calendar.get(JavaCalendar.MONTH), calendar.get(JavaCalendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        btnTimePicker.setOnClickListener {
            val calendar = JavaCalendar.getInstance()
            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                selectedTime = JavaCalendar.getInstance().apply {
                    set(JavaCalendar.HOUR_OF_DAY, hourOfDay)
                    set(JavaCalendar.MINUTE, minute)
                }
            }, calendar.get(JavaCalendar.HOUR_OF_DAY), calendar.get(JavaCalendar.MINUTE), true)
            timePickerDialog.show()
        }

        builder.setPositiveButton("Guardar") { _, _ ->
            val eventTitle = etEventTitle.text.toString()
            val eventDescription = etEventDescription.text.toString()
            if (eventTitle.isNotEmpty() && ::selectedDate.isInitialized && ::selectedTime.isInitialized) {
                val eventDate = JavaCalendar.getInstance().apply {
                    set(
                        selectedDate.get(JavaCalendar.YEAR),
                        selectedDate.get(JavaCalendar.MONTH),
                        selectedDate.get(JavaCalendar.DAY_OF_MONTH),
                        selectedTime.get(JavaCalendar.HOUR_OF_DAY),
                        selectedTime.get(JavaCalendar.MINUTE)
                    )
                }.timeInMillis
                val event = Calendar.Event(eventTitle, eventDescription, eventDate)
                selectedCalendar.events.add(event)
                FirestoreHelper.saveCalendar(selectedCalendar, {
                    Toast.makeText(this, "Evento agregado", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(this, "Error al agregar evento", Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun showShareCalendarDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Compartir Calendario")

        val view = layoutInflater.inflate(R.layout.dialog_share_calendar, null)
        builder.setView(view)

        val etRecipientEmail: EditText = view.findViewById(R.id.etRecipientEmail)

        builder.setPositiveButton("Compartir") { _, _ ->
            val recipientEmail = etRecipientEmail.text.toString()
            if (recipientEmail.isNotEmpty()) {
                FirestoreHelper.shareCalendar(selectedCalendar, recipientEmail, {
                    Toast.makeText(this, "Calendario compartido", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(this, "Error al compartir calendario", Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "Complete el campo de correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal1 = JavaCalendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = JavaCalendar.getInstance().apply { timeInMillis = time2 }
        return cal1.get(JavaCalendar.YEAR) == cal2.get(JavaCalendar.YEAR) &&
                cal1.get(JavaCalendar.MONTH) == cal2.get(JavaCalendar.MONTH) &&
                cal1.get(JavaCalendar.DAY_OF_MONTH) == cal2.get(JavaCalendar.DAY_OF_MONTH)
    }
}
