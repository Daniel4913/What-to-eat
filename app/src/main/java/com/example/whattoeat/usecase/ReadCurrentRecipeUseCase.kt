package com.example.whattoeat.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.whattoeat.data.Repository
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import javax.inject.Inject

class ReadCurrentRecipeUseCase @Inject constructor(
    private val repoUse: Repository
) {
    fun execute(id: Int): LiveData<DetailedRecipeEntity>{
       return repoUse.local.readDetailedRecipe(id).asLiveData()
    }

}
