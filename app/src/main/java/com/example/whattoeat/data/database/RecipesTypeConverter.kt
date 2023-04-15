package com.example.whattoeat.data.database

import androidx.room.TypeConverter
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.model.RecipesByIngredients
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun recipesToString(recipesByIngredients: RecipesByIngredients): String {
        return gson.toJson(recipesByIngredients)
    }

    @TypeConverter
    fun stringToRecipes(data: String): RecipesByIngredients {
        val listType = object : TypeToken<RecipesByIngredients>() {}.type
        return gson.fromJson(data, listType )
    }

    @TypeConverter
    fun detailedRecipeToString(detailedRecipe: DetailedRecipe): String {
        return gson.toJson(detailedRecipe)
    }

    @TypeConverter
    fun stringToDetailedRecipe(data: String): DetailedRecipe {
        val listType = object : TypeToken<DetailedRecipe>() {}.type
        return gson.fromJson(data, listType )
    }


}