package com.example.whattoeat.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.whattoeat.data.DataStoreRepository
import com.example.whattoeat.data.IngredientsAndRanking
import com.example.whattoeat.model.RecipesByIngredients
import com.example.whattoeat.util.Constants.API_KEY
import com.example.whattoeat.util.Constants.DEFAULT_INGREDIENTS
import com.example.whattoeat.util.Constants.DEFAULT_RANKING
import com.example.whattoeat.util.Constants.DEFAULT_RECIPES_NUMBER
import com.example.whattoeat.util.Constants.QUERY_API_KEY
import com.example.whattoeat.util.Constants.QUERY_IDS
import com.example.whattoeat.util.Constants.QUERY_INGREDIENTS
import com.example.whattoeat.util.Constants.QUERY_NUMBER
import com.example.whattoeat.util.Constants.QUERY_RANKING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) :
    AndroidViewModel(application) {

    private lateinit var ingredientsAndRanking: IngredientsAndRanking

    var networkStatus = false
    var backOnline = false

    val readIngredientsAndRanking = dataStoreRepository.readIngredients
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()
    var recipesIds = mutableListOf<String>()

    fun saveIngredients() =
        viewModelScope.launch(Dispatchers.IO) {
            if (this@RecipesViewModel::ingredientsAndRanking.isInitialized) {
                dataStoreRepository.saveIngredients(
                    ingredientsAndRanking.selectedRanking,
                    ingredientsAndRanking.typedIngredients
                )
            }
        }

    fun saveIngredientsTemp(
        selectedRanking: String,
        typedIngredients: String
    ) {
        ingredientsAndRanking = IngredientsAndRanking(selectedRanking, typedIngredients)
    }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }


    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY

        if (this@RecipesViewModel::ingredientsAndRanking.isInitialized) {
            queries[QUERY_RANKING] = ingredientsAndRanking.selectedRanking
            queries[QUERY_INGREDIENTS] = ingredientsAndRanking.typedIngredients
        } else {
            queries[QUERY_RANKING] = DEFAULT_RANKING
            queries[QUERY_INGREDIENTS] = DEFAULT_INGREDIENTS
        }

        return queries
    }

    fun applyDetailedQueries(recipes: RecipesByIngredients?): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        getRecipesIds(recipes)

        queries[QUERY_IDS] = recipesIds.toString().removeSurrounding("[", "]")
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        Log.d("applyDetailedQueries recipesVM", "$queries")
        return queries
    }

    private fun getRecipesIds(recipes: RecipesByIngredients?) {
        recipes?.forEach {
            recipesIds.add(it.id.toString())
        }
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(
                getApplication(),
                "No internet connection",
                Toast.LENGTH_SHORT
            ).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(
                    getApplication(),
                    "Back online in internet",
                    Toast.LENGTH_SHORT
                ).show()
                saveBackOnline(false)
            }
        }
    }

}