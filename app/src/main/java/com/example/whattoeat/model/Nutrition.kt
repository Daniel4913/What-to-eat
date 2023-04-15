package com.example.whattoeat.model

import com.google.gson.annotations.SerializedName

data class Nutrition(
    @SerializedName("caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown,
    @SerializedName("ingredients")
    val ingredients: List<IngredientX>,
    @SerializedName("nutrients")
    val nutrients: List<NutrientX>,
    @SerializedName("properties")
    val properties: List<Property>,
    @SerializedName("weightPerServing")
    val weightPerServing: WeightPerServing
)