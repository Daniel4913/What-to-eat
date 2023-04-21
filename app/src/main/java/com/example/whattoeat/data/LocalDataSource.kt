package com.example.whattoeat.data

import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.DetailedRecipeDao
import com.example.whattoeat.data.database.RecipesDao
import com.example.whattoeat.data.database.entities.FavoriteEntity
import com.example.whattoeat.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao,
) {

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    fun readDetailedRecipe(id:Int): Flow<DetailedRecipeEntity> {
        return recipesDao.readDetailedRecipe(id)
    }

    fun readDetailedRecipes(): Flow<List<DetailedRecipeEntity>> {
        return recipesDao.readDetailedRecipes()
    }

    fun readFavorites(): Flow<List<FavoriteEntity>> {
        return recipesDao.readFavorites()
    }

    fun readFavorite(id:Int): Flow<FavoriteEntity>{
        return recipesDao.readFavorite(id)
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipe(recipesEntity)
    }

    suspend fun insertDetailedRecipe(detailedRecipeEntity: DetailedRecipeEntity) {
        recipesDao.insertDetailedRecipe(detailedRecipeEntity)
    }

    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        recipesDao.insertFavorite(favoriteEntity)
    }

    suspend fun deleteAllFavorites() {
        recipesDao.deleteAllFavorites()
    }

    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity) {
        recipesDao.deleteFavorite(favoriteEntity)
    }




}