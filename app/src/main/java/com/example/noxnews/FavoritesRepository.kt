package com.example.noxnews

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FavoritesRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Add a news item to user's favorites
    fun addToFavorites(news: FavoriteNews, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onComplete(false)

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(news.articleUrl.hashCode().toString()) // unique per article
            .set(news)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Get all favorites (one-time fetch)
    fun getFavorites(onComplete: (List<FavoriteNews>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onComplete(emptyList())

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .addOnSuccessListener { snapshot ->
                val favorites = snapshot.toObjects(FavoriteNews::class.java)
                onComplete(favorites)
            }
            .addOnFailureListener { onComplete(emptyList()) }
    }

    // Remove favorite
    fun removeFavorite(news: FavoriteNews, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onComplete(false)

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(news.articleUrl.hashCode().toString())
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
