package com.example.whattoeat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.whattoeat.data.database.entities.FavoriteRecipeEntity
import com.example.whattoeat.databinding.FavoritesItemBinding
import com.example.whattoeat.util.RecipesDiffUtil

class FavoriteRecipesAdapter(private val onItemClicked: (Int) -> Unit) :
    RecyclerView.Adapter<FavoriteRecipesAdapter.FavoritesViewHolder>() {

    private var favoriteRecipesList = emptyList<FavoriteRecipeEntity>()

    class FavoritesViewHolder(val binding: FavoritesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(favoriteRecipeEntity: FavoriteRecipeEntity){
                binding.recipeItem = favoriteRecipeEntity.detailedRecipe
                binding.executePendingBindings()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(
            FavoritesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return favoriteRecipesList.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val detailedRecipe = favoriteRecipesList[position]
        holder.bind(detailedRecipe)
    }

    fun setData(newFavorites: List<FavoriteRecipeEntity>) {
        val favoriteDiffUtil = RecipesDiffUtil(favoriteRecipesList, newFavorites)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteDiffUtil)
        favoriteRecipesList = newFavorites
        diffUtilResult.dispatchUpdatesTo(this)
    }
}