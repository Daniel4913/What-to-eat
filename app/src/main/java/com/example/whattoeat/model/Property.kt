package com.example.whattoeat.model


import com.google.gson.annotations.SerializedName

data class Property(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("unit")
    val unit: String
)