package com.example.medictime.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.medictime.R
import com.example.medictime.CalendariosActivity
import com.example.medictime.Memorys.Calendar

class CalendarAdapter(private val calendars: List<Calendar>, private val context: CalendariosActivity) : BaseAdapter() {

    override fun getCount(): Int = calendars.size

    override fun getItem(position: Int): Any = calendars[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val calendar = calendars[position]
        viewHolder.titleTextView.text = calendar.title
        viewHolder.dateTextView.text = calendar.date

        return view
    }

    private class ViewHolder(view: View) {
        val titleTextView: TextView = view.findViewById(R.id.title)
        val dateTextView: TextView = view.findViewById(R.id.date)
    }
}