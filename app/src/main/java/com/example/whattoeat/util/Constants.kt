package com.example.whattoeat.util


object Constants {

    // Spoonacular
    const val BASE_URL = "https://api.spoonacular.com"
    const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
    const val API_KEY = "1b7968aa5d314a21b6b3c24d1f563dde"

    // Nutrient and property api names
    const val CALORIES = "Calories"
    const val FAT = "Fat"
    const val CARBOHYDRATES = "Carbohydrates"
    const val PROTEIN = "Protein"
    const val SATURATED_FAT = "Saturated Fat"
    const val SUGAR = "Sugar"
    const val GLYCEMIC_INDEX = "Glycemic Index"
    const val GLYCEMIC_LOAD = "Glycemic Load"
    const val NUTRITION_SCORE = "Nutrition Score"

    //Network results
    const val TIMEOUT = "Timeout"
    const val API_KEY_LIMITED = "API KEY LIMITED"
    const val RECIPES_NOT_FOUND = "Recipes not found"
    const val RECIPES_NOT_FOUND_EXT = "Recipes not found. Some kind of exception occurred"
    const val NO_INTERNET = "No internet connection"
    const val BACK_INTERNET = "Back with internet connection"

    // ROOM DB
    const val DATABASE_NAME = "recipes_database"
    const val RECIPES_TABLE = "recipes_table"
    const val DETAILED_RECIPES_TABLE = "detailed_recipes_table"
    const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"

    //API QUERY KEYS
    const val QUERY_NUMBER = "number"
    const val QUERY_INGREDIENTS = "ingredients"
    const val QUERY_RANKING = "ranking"
    const val QUERY_API_KEY = "apiKey"
    const val QUERY_IDS = "ids"
    const val RANKING_1 = "1"
    const val RANKING_2 = "2"

    // Bottom sheet and preferences values
    const val DEFAULT_RANKING = "1"
    const val DEFAULT_INGREDIENTS = "onion,bread"
    const val DEFAULT_RECIPES_NUMBER = "30"

    const val PREFERENCES_NAME = "ingredients_preferences"
    const val PREFERENCES_RANKING_KEY = "ranking"
    const val PREFERENCES_INGREDIENTS_KEY = "ingredients"
    const val PREFERENCES_BACK_ONLINE = "backOnline"

}