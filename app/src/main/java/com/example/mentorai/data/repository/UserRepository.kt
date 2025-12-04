package com.example.mentorai.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase


class UserRepository {
    val database = FirebaseDatabase.getInstance()

    fun saveUserToDatabase(uid: String, name: String?, email: String) {
        val ref = database.getReference("users")
        Log.d("UserRepository", "Saving user: $uid, $name, $email")

        val userData = mapOf(
            "name" to (name ?: "NoName"),
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        ref.child(uid).setValue(userData)
            .addOnSuccessListener {
                Log.d("Database", "User saved successfully at uid: $uid")
            }
            .addOnFailureListener { e ->
                Log.e("Database", "Failed to save user: ${e.message}")
            }
    }
}