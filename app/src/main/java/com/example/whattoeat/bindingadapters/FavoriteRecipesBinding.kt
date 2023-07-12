package com.example.whattoeat.bindingadapters


import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.whattoeat.ui.fragments.FavoritesFragmentDirections

class FavoriteRecipesBinding {
    companion object {
        @BindingAdapter("onFavoriteRecipeClickListener")
        @JvmStatic
        fun onFavoriteRecipeClickListener(itemLayout: ConstraintLayout, recipeId: Int) {
            itemLayout.setOnClickListener {
                val action =
                    FavoritesFragmentDirections.actionFavoritesFragmentToDetailsActivity(recipeId)
                itemLayout.findNavController().navigate(action)
            }
        }
    }
}