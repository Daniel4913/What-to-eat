package com.example.whattoeat.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.entities.FavoriteRecipeEntity
import com.example.whattoeat.data.database.entities.RecipeByIngredientEntity
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.model.RecipesByIngredients
import com.example.whattoeat.usecase.CacheDetailedRecipesUseCase
import com.example.whattoeat.usecase.CacheRecipesByIngredientsUseCase
import com.example.whattoeat.usecase.GetDetailedRecipesUseCase
import com.example.whattoeat.usecase.GetRecipesByIngredientsUseCase
import com.example.whattoeat.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val getRecipesUseCase: GetRecipesByIngredientsUseCase,
    private val cacheRecipesUseCase: CacheRecipesByIngredientsUseCase,
    private val getDetailedRecipesUseCase: GetDetailedRecipesUseCase,
    private val cacheDetailedRecipesUseCase: CacheDetailedRecipesUseCase,
) : ViewModel() {

    lateinit var currentRecipe: DetailedRecipe

    /** ROOM DB */

    val readCachedRecipesByIngredients: LiveData<List<RecipeByIngredientEntity>> =
        repository.local.readRecipesByIngredients().asLiveData()

    val readDetailedRecipes: LiveData<List<DetailedRecipeEntity>> =
        repository.local.readDetailedRecipes().asLiveData()

    val readFavorites: LiveData<List<FavoriteRecipeEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()

    fun readCurrentRecipe(id: Int): LiveData<DetailedRecipeEntity> {
        return repository.local.readDetailedRecipe(id).asLiveData()
    }

    fun readFavorite(id: Int): LiveData<FavoriteRecipeEntity> {
        return repository.local.readFavoriteRecipe(id).asLiveData()
    }

    private fun insertRecipes(recipeByIngredientEntity: RecipeByIngredientEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipesByIngredients(recipeByIngredientEntity)
        }

    private fun insertDetailedRecipe(detailedRecipeEntity: DetailedRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertDetailedRecipe(detailedRecipeEntity)
        }

    fun insertFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipe(favoriteRecipeEntity)
        }

    fun deleteFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoriteRecipeEntity)
        }

    fun deleteAllFavorites() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }


    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<RecipesByIngredients>> = MutableLiveData()
    var detailedRecipesResponse: MutableLiveData<NetworkResult<List<DetailedRecipe>>> =
        MutableLiveData()


    fun getRecipesByIngredients(queries: Map<String, String>) = viewModelScope.launch {

        recipesResponse.value = getRecipesUseCase.execute(queries)
        recipesResponse.value!!.data?.let { offlineCacheRecipesByIngredients(it) }
//        getRecipesByIngredientsSafeCall(queries)
    }

    fun getDetailedRecipes(queries: Map<String, String>) =
        viewModelScope.launch {
            detailedRecipesResponse.value = getDetailedRecipesUseCase.execute(queries)
//            getDetailedRecipesSafeCall(queries)
            detailedRecipesResponse.value!!.data?.let { offlineCacheDetailedRecipe(it) }
        }

    private suspend fun offlineCacheRecipesByIngredients(recipes: RecipesByIngredients) {
        val recipeByIngredientEntity = RecipeByIngredientEntity(recipes)
        cacheRecipesUseCase.execute(recipeByIngredientEntity)
    }

    private suspend fun offlineCacheDetailedRecipe(recipes: List<DetailedRecipe>) {
        recipes.forEach { detailedRecipe ->
            val detailedRecipeEntity = DetailedRecipeEntity(detailedRecipe.id, detailedRecipe)
            cacheDetailedRecipesUseCase.execute(detailedRecipeEntity)
//            insertDetailedRecipe(detailedRecipeEntity)
        }
    }


}