package com.example.whattoeat.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.FavoriteRecipeEntity
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(
    val repository: Repository
) {
    val readFavorites = repository.local.readFavoriteRecipes().asLiveData()

    suspend fun insertFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) {
        repository.local.insertFavoriteRecipe(favoriteRecipeEntity)
    }

    suspend fun deleteFavorite(favoriteRecipeEntity: FavoriteRecipeEntity){
        repository.local.deleteFavoriteRecipe(favoriteRecipeEntity)
    }
    suspend fun deleteAllFavorites() {
        repository.local.deleteAllFavoriteRecipes()
    }
}
