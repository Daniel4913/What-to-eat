package com.example.whattoeat.usecase

import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.RecipeByIngredientEntity
import com.example.whattoeat.model.RecipesByIngredients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

class CacheRecipesByIngredientsUseCase(private val repoUse: Repository,) {

    suspend fun execute(recipes: RecipeByIngredientEntity){
        coroutineScope(
//            Dispatchers.IO
        ) {
        repoUse.local.insertRecipesByIngredients(recipes)
        }
    }
}