package com.example.medictime.Adapters

import android.content.Context
import android.content.SharedPreferences
import com.example.medictime.Memorys.Calendar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesHelper {
    private const val PREF_NAME = "medictime_prefs"
    private const val CALENDAR_KEY = "calendars"

    fun saveCalendars(context: Context, calendars: List<Calendar>) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(calendars)
        editor.putString(CALENDAR_KEY, json)
        editor.apply()
    }

    fun getCalendars(context: Context): List<Calendar> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(CALENDAR_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Calendar>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}