package com.example.whattoeat.usecase

import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import kotlinx.coroutines.coroutineScope

class CacheDetailedRecipesUseCase(private val repoUse:Repository) {
    suspend fun execute(recipes: DetailedRecipeEntity){
        coroutineScope {
            repoUse.local.insertDetailedRecipe(recipes)
        }
    }
}