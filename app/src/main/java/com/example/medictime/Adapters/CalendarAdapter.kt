package com.example.medictime.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.medictime.memorys.Calendar
import com.example.medictime.R

class CalendarAdapter(private val calendars: List<Calendar>, private val context: Context) : BaseAdapter() {

    override fun getCount(): Int = calendars.size

    override fun getItem(position: Int): Calendar = calendars[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false)
        val calendar = getItem(position)

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)

        tvTitle.text = calendar.title
        tvDescription.text = calendar.descripcion

        return view
    }
}
