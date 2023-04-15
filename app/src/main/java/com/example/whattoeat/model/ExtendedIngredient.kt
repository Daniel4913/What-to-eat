package com.example.whattoeat.model


import com.google.gson.annotations.SerializedName

data class ExtendedIngredient(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consitency")
    val consitency: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("originalName")
    val originalName: String,
    @SerializedName("unit")
    val unit: String
)