package com.example.whattoeat.usecase

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.whattoeat.data.DataSource
import com.example.whattoeat.data.Repository
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.util.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class GetDetailedRecipesUseCase @Inject constructor(
    private val repoUse: Repository,
    private val connectivityManager: ConnectivityManager,
) {

    suspend fun execute(queries: Map<String, String>):NetworkResult<List<DetailedRecipe>> {
//        detailedRecipesResponse.value = NetworkResult.Loading()
       return if (checkInternetConnection()) {
            try {
                val response = repoUse.remote.getDetailedRecipes(queries)
                handleDetailedResponse(response)
//                val recipes = detailedRecipesResponse.value!!.data
//                if (recipes != null) {
//                    offlineCacheDetailedRecipe(recipes)
//                }
            } catch (e: Exception) {
                    NetworkResult.Error("Error: $e")
            }
        } else {
            NetworkResult.Error("Probable no internet connection")
        }
    }

    private fun handleDetailedResponse(response: Response<List<DetailedRecipe>>): NetworkResult<List<DetailedRecipe>> {
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