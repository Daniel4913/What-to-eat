package com.example.whattoeat.model


import com.google.gson.annotations.SerializedName

data class RecipesByIngredientsItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("missedIngredientCount")
    val missedIngredientCount: Int,
    @SerializedName("missedIngredients")
    val missedIngredients: List<MissedIngredient>,
    @SerializedName("title")
    val title: String,
    @SerializedName("usedIngredientCount")
    val usedIngredientCount: Int,
    @SerializedName("usedIngredients")
    val usedIngredients: List<UsedIngredient>,
    @SerializedName("likes")
    val likes: Int
)