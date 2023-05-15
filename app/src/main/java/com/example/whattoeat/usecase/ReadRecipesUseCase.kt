package com.example.whattoeat.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.RecipeByIngredientEntity
import javax.inject.Inject

class ReadRecipesUseCase @Inject constructor(
    private val repository: Repository
) {
    fun execute(): LiveData<List<RecipeByIngredientEntity>> {
        return repository.local.readRecipesByIngredients().asLiveData()
    }
}
