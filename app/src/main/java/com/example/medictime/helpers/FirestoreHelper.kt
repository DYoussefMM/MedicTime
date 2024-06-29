package com.example.medictime.helpers

import com.example.medictime.memorys.Calendar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreInstance {
    fun getInstance(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

object AuthInstance {
    private var instance: FirebaseAuth? = null

    fun getInstance(): FirebaseAuth {
        if (instance == null) {
            instance = FirebaseAuth.getInstance()
        }
        return instance!!
    }
}

object FirestoreHelper {

    fun saveCalendar(calendar: Calendar, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val firestore = FirestoreInstance.getInstance()
        val auth = AuthInstance.getInstance()

        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        val calendarData = hashMapOf(
            "title" to calendar.title,
            "descripcion" to calendar.descripcion,
            "events" to calendar.events
        )

        firestore.collection("users").document(userId).collection("calendars")
            .document(calendar.title)
            .set(calendarData)
            .addOnSuccessListener {
                addGlobalHistory("saveCalendar", calendar, onSuccess, onFailure)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun loadCalendars(onSuccess: (List<Calendar>) -> Unit, onFailure: (Exception) -> Unit) {
        val firestore = FirestoreInstance.getInstance()
        val auth = AuthInstance.getInstance()

        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        firestore.collection("users").document(userId).collection("calendars")
            .get()
            .addOnSuccessListener { result ->
                val calendars = result.toObjects(Calendar::class.java)
                onSuccess(calendars)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun shareCalendar(calendar: Calendar, recipientEmail: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val firestore = FirestoreInstance.getInstance()
        val auth = AuthInstance.getInstance()

        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        val calendarData = hashMapOf(
            "title" to calendar.title,
            "descripcion" to calendar.descripcion,
            "events" to calendar.events
        )

        firestore.collection("sharedCalendars").document(recipientEmail).collection("calendars")
            .document(calendar.title)
            .set(calendarData)
            .addOnSuccessListener {
                addGlobalHistory("shareCalendar", calendar, onSuccess, onFailure)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun loadSharedCalendars(recipientEmail: String, onSuccess: (List<Calendar>) -> Unit, onFailure: (Exception) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("sharedCalendars").document(recipientEmail).collection("calendars")
            .get()
            .addOnSuccessListener { result ->
                val sharedCalendars = result.toObjects(Calendar::class.java)
                sharedCalendars.forEach { it.isShared = true }
                onSuccess(sharedCalendars)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteCalendar(calendar: Calendar, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val firestore = FirestoreInstance.getInstance()
        val auth = AuthInstance.getInstance()

        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        firestore.collection("users").document(userId).collection("calendars")
            .document(calendar.title)
            .delete()
            .addOnSuccessListener {
                addGlobalHistory("deleteCalendar", calendar, onSuccess, onFailure)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteSharedCalendar(calendar: Calendar, recipientEmail: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val firestore = FirestoreInstance.getInstance()

        firestore.collection("sharedCalendars").document(recipientEmail).collection("calendars")
            .document(calendar.title)
            .delete()
            .addOnSuccessListener {
                addGlobalHistory("deleteSharedCalendar", calendar, onSuccess, onFailure)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    private fun addGlobalHistory(action: String, calendar: Calendar, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val firestore = FirestoreInstance.getInstance()
        val historyData = hashMapOf(
            "action" to action,
            "calendarTitle" to calendar.title,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("globalHistory")
            .add(historyData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}
