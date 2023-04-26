package com.example.whattoeat.data.network

import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.model.RecipesByIngredients
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface SpoonacularApi {

    @GET("/recipes/findByIngredients")
    suspend fun getRecipesByIngredients(
        @QueryMap queries: Map<String, String>
    ): Response<RecipesByIngredients>

    @GET("/recipes/informationBulk?includeNutrition=true")
    suspend fun getDetailedRecipes(
        @QueryMap queries: Map<String,String>
    ): Response<List<DetailedRecipe>>
}