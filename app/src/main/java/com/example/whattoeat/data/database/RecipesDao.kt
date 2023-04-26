package com.example.whattoeat.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.entities.FavoriteRecipeEntity
import com.example.whattoeat.data.database.entities.RecipeByIngredientEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeByIngredients(recipeByIngredientEntity: RecipeByIngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailedRecipe(detailedRecipeEntity: DetailedRecipeEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipesByIngredients(): Flow<List<RecipeByIngredientEntity>>

    @Query("SELECT * FROM detailed_recipes_table WHERE id= :id")
    fun readDetailedRecipe(id: Int): Flow<DetailedRecipeEntity>

    @Query("SELECT * FROM detailed_recipes_table ORDER BY id ASC")
    fun readDetailedRecipes(): Flow<List<DetailedRecipeEntity>>

    @Query("SELECT * from favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT * from favorite_recipes_table WHERE id= :id")
    fun readFavoriteRecipe(id: Int): Flow<FavoriteRecipeEntity>

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity)

    @Query("DELETE from favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()


}