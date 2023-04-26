package com.example.whattoeat.data

import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.RecipesDao
import com.example.whattoeat.data.database.entities.FavoriteRecipeEntity
import com.example.whattoeat.data.database.entities.RecipeByIngredientEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao,
) {

    fun readRecipesByIngredients(): Flow<List<RecipeByIngredientEntity>> {
        return recipesDao.readRecipesByIngredients()
    }

    fun readDetailedRecipe(id:Int): Flow<DetailedRecipeEntity> {
        return recipesDao.readDetailedRecipe(id)
    }

    fun readDetailedRecipes(): Flow<List<DetailedRecipeEntity>> {
        return recipesDao.readDetailedRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoriteRecipeEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    fun readFavoriteRecipe(id:Int): Flow<FavoriteRecipeEntity>{
        return recipesDao.readFavoriteRecipe(id)
    }

    suspend fun insertRecipesByIngredients(recipeByIngredientEntity: RecipeByIngredientEntity) {
        recipesDao.insertRecipeByIngredients(recipeByIngredientEntity)
    }

    suspend fun insertDetailedRecipe(detailedRecipeEntity: DetailedRecipeEntity) {
        recipesDao.insertDetailedRecipe(detailedRecipeEntity)
    }

    suspend fun insertFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity) {
        recipesDao.insertFavoriteRecipe(favoriteRecipeEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }

    suspend fun deleteFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity) {
        recipesDao.deleteFavoriteRecipe(favoriteRecipeEntity)
    }




}