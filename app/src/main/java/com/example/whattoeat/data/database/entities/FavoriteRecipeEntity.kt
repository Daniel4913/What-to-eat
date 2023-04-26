package com.example.whattoeat.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.util.Constants.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteRecipeEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var detailedRecipe: DetailedRecipe
)