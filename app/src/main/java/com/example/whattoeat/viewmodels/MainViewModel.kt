package com.example.whattoeat.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.entities.FavoriteEntity
import com.example.whattoeat.data.database.entities.RecipesEntity
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.model.RecipesByIngredients
import com.example.whattoeat.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    lateinit var currentRecipe: DetailedRecipe

    /** ROOM DB */

    val readCachedRecipesByIngredients: LiveData<List<RecipesEntity>> =
        repository.local.readRecipes().asLiveData()

    val readDetailedRecipes: LiveData<List<DetailedRecipeEntity>> =
        repository.local.readDetailedRecipes().asLiveData()

    val readFavorites: LiveData<List<FavoriteEntity>> =
        repository.local.readFavorites().asLiveData()

    fun readDetailedRecipe(id: Int): LiveData<DetailedRecipeEntity>{
        return repository.local.readDetailedRecipe(id).asLiveData()
    }

    fun readFavorite(id: Int): LiveData<FavoriteEntity> {
        return repository.local.readFavorite(id).asLiveData()

    }

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    private fun insertDetailedRecipe(detailedRecipeEntity: DetailedRecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertDetailedRecipe(detailedRecipeEntity)
        }

    fun insertFavorite(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipe(favoriteEntity)
        }

    fun deleteFavorite(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavorite(favoriteEntity)
        }

    fun deleteAllFavorites() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavorites()
        }


    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<RecipesByIngredients>> = MutableLiveData()
    var detailedRecipesResponse: MutableLiveData<NetworkResult<List<DetailedRecipe>>> =
        MutableLiveData()


    fun getRecipesByIngredients(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesByIngredientsSafeCall(queries)
    }

    fun getDetailedRecipes(queries: Map<String, String>) =
        viewModelScope.launch {
            getDetailedRecipesSafeCall(queries)
        }

    private suspend fun getDetailedRecipesSafeCall(
        queries: Map<String, String>
    ) {
        detailedRecipesResponse.value = NetworkResult.Loading()
        if (checkInternetConnection()) {
            try {
                val response = repository.remote.getDetailedRecipes(queries)
                detailedRecipesResponse.value = handleDetailedResponse(response)
                val recipes = detailedRecipesResponse.value!!.data
                Log.d("getDetailedRecipesSafeCall", "$recipes")
                if (recipes != null) {
                    offlineCacheDetailedRecipe(recipes)
                }
            } catch (e: Exception) {
                detailedRecipesResponse.value =
                    NetworkResult.Error("Error: $e")
            }
        } else {
            detailedRecipesResponse.value = NetworkResult.Error("Probable no internet connection")
        }
    }

    private suspend fun getRecipesByIngredientsSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (checkInternetConnection()) {
            try {
                val response = repository.remote.getRecipesByIngredients(queries)
                recipesResponse.value = handleByIngredientsResponse(response)

                val recipes = recipesResponse.value!!.data
                if (recipes != null) {
                    offlineCacheRecipes(recipes)

                }
            } catch (e: Exception) {
                recipesResponse.value =
                    NetworkResult.Error("Recipes not found. Some kind of exception occurred")
                Log.d("get SafeCall", "${e}")
            }
        } else {
            recipesResponse.value = NetworkResult.Error(message = "No internet connection")
        }
    }

    private fun offlineCacheRecipes(recipes: RecipesByIngredients) {
        val recipesEntity = RecipesEntity(recipes)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheDetailedRecipe(recipes: List<DetailedRecipe>) {
        recipes.forEach { detailedRecipe ->
            val detailedRecipeEntity = DetailedRecipeEntity(detailedRecipe.id, detailedRecipe)
            insertDetailedRecipe(detailedRecipeEntity)
        }

    }

    private fun handleByIngredientsResponse(response: Response<RecipesByIngredients>): NetworkResult<RecipesByIngredients>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API KEY LIMITED")
            }
            response.body().isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleDetailedResponse(response: Response<List<DetailedRecipe>>): NetworkResult<List<DetailedRecipe>>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API KEY LIMITED")
            }
            response.body() == null -> {
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}