package com.example.whattoeat.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.util.Constants.DETAILED_RECIPES_TABLE

@Entity(tableName = DETAILED_RECIPES_TABLE)
class DetailedRecipeEntity(
    val detailedRecipe: DetailedRecipe)
{
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}