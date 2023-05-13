package com.example.whattoeat.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import javax.inject.Inject

class ReadDetailedRecipesUseCase @Inject constructor(
    private val repoUse: Repository
) {
    fun execute(): LiveData<List<DetailedRecipeEntity>> {
        return repoUse.local.readDetailedRecipes().asLiveData()
    }

}
