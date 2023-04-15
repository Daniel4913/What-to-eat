package com.example.whattoeat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.whattoeat.data.database.entities.FavoriteEntity
import com.example.whattoeat.databinding.FavoritesItemBinding
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.util.RecipesDiffUtil

class FavoritesAdapter(private val onItemClicked: (DetailedRecipe) -> Unit) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private var favoritesList = emptyList<FavoriteEntity>()

    class FavoritesViewHolder(val binding: FavoritesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(
            FavoritesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.binding.apply {

            favoriteItemCardView.setOnClickListener {
                onItemClicked(favoritesList[position].detailedRecipe)
            }
            favoriteRecipeImageView.load(favoritesList[position].detailedRecipe.image)
            favoriteTitleTextView.text = favoritesList[position].detailedRecipe.title
            favoriteCookingTimeChip.text =
                favoritesList[position].detailedRecipe.readyInMinutes.toString()
        }
    }

    fun setData(newFavorites: List<FavoriteEntity>) {
        val favoriteDiffUtil = RecipesDiffUtil(favoritesList, newFavorites)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteDiffUtil)
        favoritesList = newFavorites
        diffUtilResult.dispatchUpdatesTo(this)
    }
}