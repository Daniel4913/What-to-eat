package com.example.whattoeat.viewmodels

import androidx.lifecycle.*
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.entities.FavoriteRecipeEntity
import com.example.whattoeat.data.database.entities.RecipeByIngredientEntity
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.model.RecipesByIngredients
import com.example.whattoeat.usecase.*
import com.example.whattoeat.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesByIngredientsUseCase,
    private val cacheRecipesUseCase: CacheRecipesUseCase,
    private val getDetailedRecipesUseCase: GetDetailedRecipesUseCase,
    private val cacheDetailedRecipesUseCase: CacheDetailedRecipesUseCase,
     readRecipesUseCase: ReadRecipesUseCase,
     readDetailedRecipesUseCase: ReadDetailedRecipesUseCase,
    private val readCurrentRecipeUseCase: ReadCurrentRecipeUseCase,
    private val favoritesUseCase: FavoritesUseCase,
) : ViewModel() {

    lateinit var currentRecipe: DetailedRecipe

    /** ROOM DB */

    val readCachedRecipesByIngredients: LiveData<List<RecipeByIngredientEntity>> =
        readRecipesUseCase.execute()

    val readDetailedRecipes: LiveData<List<DetailedRecipeEntity>> =
        readDetailedRecipesUseCase.execute()

    val readFavorites: LiveData<List<FavoriteRecipeEntity>> =
        favoritesUseCase.readFavorites

    fun readCurrentRecipe(id: Int): LiveData<DetailedRecipeEntity> {
        return readCurrentRecipeUseCase.execute(id)
    }

    fun readFavorite(id: Int): LiveData<FavoriteRecipeEntity> {
        return readFavorite(id)
    }

     fun insertRecipes(recipeByIngredientEntity: RecipeByIngredientEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            cacheRecipesUseCase.execute(recipeByIngredientEntity)
        }

     fun insertDetailedRecipe(detailedRecipeEntity: DetailedRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            cacheDetailedRecipesUseCase.execute(detailedRecipeEntity)
        }

    fun insertFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            favoritesUseCase.insertFavorite(favoriteRecipeEntity)
        }

    fun deleteFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            favoritesUseCase.deleteFavorite(favoriteRecipeEntity)
        }

    fun deleteAllFavorites() =
        viewModelScope.launch(Dispatchers.IO) {
            favoritesUseCase.deleteAllFavorites()
        }


    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<RecipesByIngredients>> = MutableLiveData()
    var detailedRecipesResponse: MutableLiveData<NetworkResult<List<DetailedRecipe>>> =
        MutableLiveData()


    fun getRecipesByIngredients(queries: Map<String, String>) = viewModelScope.launch {
        recipesResponse.value = getRecipesUseCase.execute(queries)
        recipesResponse.value!!.data?.let { offlineCacheRecipesByIngredients(it) }
    }

    fun getDetailedRecipes(queries: Map<String, String>) =
        viewModelScope.launch {
            detailedRecipesResponse.value = getDetailedRecipesUseCase.execute(queries)
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
        }
    }


}