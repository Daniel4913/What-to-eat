package com.example.whattoeat.model


import com.google.gson.annotations.SerializedName

data class Tips(
    @SerializedName("cooking")
    val cooking: List<String>,
    @SerializedName("green")
    val green: List<String>,
    @SerializedName("health")
    val health: List<String>,
    @SerializedName("price")
    val price: List<String>
)