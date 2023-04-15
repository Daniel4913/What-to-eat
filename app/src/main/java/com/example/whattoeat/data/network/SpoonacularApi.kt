package com.example.whattoeat.data.network

import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.model.RecipesByIngredients
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SpoonacularApi {

    ///recipes/findByIngredients?ingredients="
    @GET("/recipes/findByIngredients")
    suspend fun getRecipesByIngredients(
        @QueryMap queries: Map<String, String>
    ): Response<RecipesByIngredients>

//    @GET("/recipes/{recipeId}/information/includeNutrition=true") NIE DZIAUA
//    @GET("/recipes/{recipeId}/information&includeNutrition=true") NIE DZIAUA
//    @GET("/recipes/{recipeId}/includeNutrition=true/information") NIE DZIAUA
    @GET("/recipes/{recipeId}/information?includeNutrition=true")
    suspend fun getRecipeInformation(
       @Path("recipeId") query: String,
       @QueryMap queries: Map<String,String>
    ): Response<DetailedRecipe>
}