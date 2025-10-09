package com.example.noxnews

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.userProfileChangeRequest

object AuthRepository {

    private val auth = FirebaseService.auth
    private val db = FirebaseService.db

    fun signUp(name: String, email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                val user = auth.currentUser

                user?.updateProfile(
                    userProfileChangeRequest {
                        displayName = name
                    })

                val userData = hashMapOf(
                    "uid" to user?.uid,
                    "name" to name,
                    "email" to email,
                    "createdAt" to System.currentTimeMillis()
                )

                user?.uid?.let { uid ->
                    db.collection("users").document(uid).set(userData)
                }
            }
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun logout() {
        auth.signOut()
    }

    fun getUserData(uid: String): Task<com.google.firebase.firestore.DocumentSnapshot> {
        return db.collection("users").document(uid).get()
    }

    fun deleteAccount(): Task<Void>? {
        val user = auth.currentUser
        val uid = user?.uid ?: return null

        db.collection("users").document(uid).delete()

        return user.delete()
    }
}