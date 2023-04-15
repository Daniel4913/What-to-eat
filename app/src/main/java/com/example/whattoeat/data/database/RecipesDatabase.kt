package com.example.whattoeat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.entities.FavoriteEntity
import com.example.whattoeat.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, DetailedRecipeEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {
    abstract fun recipesDao(): RecipesDao

}