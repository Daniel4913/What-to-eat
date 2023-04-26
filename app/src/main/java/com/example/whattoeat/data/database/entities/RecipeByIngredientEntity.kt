package com.example.whattoeat.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whattoeat.model.RecipesByIngredients
import com.example.whattoeat.util.Constants.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipeByIngredientEntity(
    val recipesByIngredients: RecipesByIngredients)
{
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}