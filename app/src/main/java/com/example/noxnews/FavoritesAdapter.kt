package com.example.noxnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noxnews.databinding.ItemFavoriteNewsBinding

class FavoritesAdapter(
    private var favorites: List<FavoriteNews>,
    private val onRemoveClick: (FavoriteNews) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: ItemFavoriteNewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteNewsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favorites[position]
        with(holder.binding) {
            titleText.text = item.title
            Glide.with(newsImage.context).load(item.imageUrl).into(newsImage)

            removeButton.setOnClickListener { onRemoveClick(item) }
        }
    }

    override fun getItemCount() = favorites.size

    fun updateList(newList: List<FavoriteNews>) {
        favorites = newList
        notifyDataSetChanged()
    }
}
