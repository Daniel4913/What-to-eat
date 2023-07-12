package com.example.whattoeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.entities.FavoriteRecipeEntity
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.usecase.FavoritesUseCase
import com.example.whattoeat.usecase.ReadCurrentRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor (
    private val readCurrentRecipeUseCase: ReadCurrentRecipeUseCase,
    private val favoritesUseCase: FavoritesUseCase,

    ): ViewModel() {

    lateinit var currentRecipe: DetailedRecipe

    val readFavorites: LiveData<List<FavoriteRecipeEntity>> =
        favoritesUseCase.readFavorites

    fun readFavorite(id: Int): LiveData<FavoriteRecipeEntity> {
        return readFavorite(id)
    }

    fun readCurrentRecipe(id: Int): LiveData<DetailedRecipeEntity> {
        return readCurrentRecipeUseCase.execute(id)
    }

    fun deleteFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            favoritesUseCase.deleteFavorite(favoriteRecipeEntity)
        }

    fun deleteAllFavorites() =
        viewModelScope.launch(Dispatchers.IO) {
            favoritesUseCase.deleteAllFavorites()
        }

    fun insertFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            favoritesUseCase.insertFavorite(favoriteRecipeEntity)
        }

}
