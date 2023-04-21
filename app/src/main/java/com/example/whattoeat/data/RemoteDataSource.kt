package com.example.whattoeat.data

import android.util.Log
import com.example.whattoeat.data.network.SpoonacularApi
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.model.RecipesByIngredients
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val spoonacularApi: SpoonacularApi
) {

    suspend fun getRecipesByIngredients(queries: Map<String, String>): Response<RecipesByIngredients> {
        return spoonacularApi.getRecipesByIngredients(queries)
    }

    suspend fun getDetailedRecipe(
        query: String,
        queries: Map<String, String>
    ): Response<DetailedRecipe> {
        return spoonacularApi.getRecipeInformation(query, queries)
    }

    suspend fun getDetailedRecipes(queries: Map<String, String>): Response<List<DetailedRecipe>> {
        return spoonacularApi.getDetailedRecipes(queries)
    }


}