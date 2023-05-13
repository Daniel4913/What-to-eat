package com.example.whattoeat.usecase

import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import javax.inject.Inject

class CacheDetailedRecipesUseCase @Inject constructor(private val repository:Repository) {
    suspend fun execute(recipes: DetailedRecipeEntity){
            repository.local.insertDetailedRecipe(recipes)
    }
}