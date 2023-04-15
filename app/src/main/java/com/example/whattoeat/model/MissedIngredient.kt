package com.example.whattoeat.model


import com.google.gson.annotations.SerializedName

data class MissedIngredient(
    @SerializedName("original")
    val original: String,
)