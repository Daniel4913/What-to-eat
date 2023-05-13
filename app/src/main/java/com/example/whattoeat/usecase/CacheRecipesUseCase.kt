package com.example.whattoeat.usecase

import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.RecipeByIngredientEntity
import javax.inject.Inject

class CacheRecipesUseCase @Inject constructor (private val repoUse: Repository,) {

    suspend fun execute(recipes: RecipeByIngredientEntity){
        repoUse.local.insertRecipesByIngredients(recipes)
    }
}