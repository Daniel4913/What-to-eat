package com.example.whattoeat.usecase

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.whattoeat.data.Repository
import com.example.whattoeat.model.RecipesByIngredients
import com.example.whattoeat.util.NetworkResult
import retrofit2.Response
import javax.inject.Inject


class GetRecipesByIngredientsUseCase @Inject constructor (
    private val repository: Repository,
    private val connectivityManager: ConnectivityManager,
) {

    suspend fun execute(myIngredients: Map<String, String>):
            NetworkResult<RecipesByIngredients> // class RecipesByIngredients : ArrayList<RecipesByIngredientsItem>()
//          ArrayList<RecipesByIngredientsItem>
    {
        return if (checkInternetConnection()) {
            try {
                val response = repository.remote.getRecipesByIngredients(myIngredients)
                handleRecipesByIngredientsResponse(response)
            } catch (e: Exception) {
                NetworkResult.Error("Recipes not found. Some kind of exception occurred")
            }
        } else {
            NetworkResult.Error(message = "No internet connection")
        }
    }

    private fun handleRecipesByIngredientsResponse(response: Response<RecipesByIngredients>): NetworkResult<RecipesByIngredients> {
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

    private fun checkInternetConnection(): Boolean {
//        val connectivityManager = getApplication(application).getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
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

//repository
class RecipesJsonModel
class RecipesDto //room

// domain
class RecipesEntity

interface Mapper<In, Out> {
    fun map(input: In): Out
}

class RecipesEntityFromJsonMapper : Mapper<RecipesJsonModel, RecipesEntity> {
    override fun map(input: RecipesJsonModel): RecipesEntity {
        TODO("Not yet implemented")
    }
}

class RecipesEntityFromDto : Mapper<RecipesDto, RecipesEntity> {
    override fun map(input: RecipesDto): RecipesEntity {
        TODO("Not yet implemented")
    }
}