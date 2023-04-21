package com.example.whattoeat.util


object Constants {

    const val BASE_URL = "https://api.spoonacular.com"
    const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
//    const val API_KEY = "b9df31a176c74fa9a0324df9bfd3dc72" danielo49
    const val API_KEY = "1b7968aa5d314a21b6b3c24d1f563dde" // sosnowskidan13

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

    // Bottom sheet and preferences values
    const val DEFAULT_RANKING = "1"
    const val DEFAULT_INGREDIENTS = "onion,bread"
    const val DEFAULT_RECIPES_NUMBER = "10"


    const val PREFERENCES_NAME = "ingredients_preferences"
    const val PREFERENCES_RANKING_KEY = "ranking"
    const val PREFERENCES_INGREDIENTS_KEY = "ingredients"
    const val PREFERENCES_BACK_ONLINE = "backOnline"

}