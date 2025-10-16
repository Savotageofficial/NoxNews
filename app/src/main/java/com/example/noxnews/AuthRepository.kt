package com.example.noxnews

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

// Define your own User model
data class User(
    val name: String? = null,
    val username: String? = null,
    val password: String? = null
)

object AuthRepository {

    private val auth = FirebaseService.auth
    private val db: FirebaseFirestore = FirebaseService.db

    fun signUp(name: String, email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser

                // Set display name in Firebase Auth profile
                user?.updateProfile(
                    userProfileChangeRequest {
                        displayName = name
                    }
                )

                // Store additional data in Firestore
                val userData = hashMapOf(
                    "uid" to user?.uid,
                    "name" to name,
                    "username" to email.substringBefore('@'), // example username
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

    fun getUserData(uid: String): Task<User?> {
        val userRef = db.collection("users").document(uid)
        return userRef.get().continueWith { task ->
            if (task.isSuccessful) {
                val doc = task.result
                User(
                    name = doc?.getString("name"),
                    username = doc?.getString("username"),
                )
            } else null
        }
    }

    fun deleteAccount(): Task<Void>? {
        val user = auth.currentUser
        val uid = user?.uid ?: return null

        db.collection("users").document(uid).delete()
        return user.delete()
    }
}
