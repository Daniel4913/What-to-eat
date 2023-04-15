package com.example.whattoeat.model


import com.google.gson.annotations.SerializedName

data class UsedIngredient(
    @SerializedName("original")
    val original: String,
)