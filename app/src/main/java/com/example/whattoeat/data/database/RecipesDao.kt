package com.example.whattoeat.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.entities.FavoriteEntity
import com.example.whattoeat.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailedRecipe(detailedRecipeEntity: DetailedRecipeEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>


    @Query("SELECT * FROM detailed_recipes_table ORDER BY id ASC")
    fun readDetailedRecipe(): Flow<DetailedRecipeEntity>

    @Query("SELECT * from favorite_recipes_table ORDER BY id ASC")
    fun readFavorites(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE from favorite_recipes_table")
    suspend fun deleteAllFavorites()
}