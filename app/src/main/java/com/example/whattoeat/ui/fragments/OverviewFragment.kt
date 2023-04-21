package com.example.whattoeat.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.whattoeat.databinding.FragmentOverviewBinding
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.util.Constants
import com.example.whattoeat.util.NetworkListener
import com.example.whattoeat.util.NetworkResult
import com.example.whattoeat.util.observeOnce
import com.example.whattoeat.viewmodels.MainViewModel
import com.example.whattoeat.viewmodels.RecipesViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class OverviewFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var networkListener: NetworkListener
    private var resipiAjdi by Delegates.notNull<Int>()

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    val args = arguments
                    resipiAjdi = args!!.getBundle("recipeBundle")?.getInt("recipeId")!!
                    val loadFavorites = args!!.getBundle("recipeBundle")?.getBoolean("loadFavorite")

                    if (loadFavorites!!) {
                        Log.d("OverviewFrag", "loadFavorites: $loadFavorites")
                        readFavorites()
                    } else {
                        readDatabase()
                    }
                }
        }


        return binding.root
    }

    private fun readFavorites() {
        lifecycleScope.launch {
            mainViewModel.readFavorites.observe(viewLifecycleOwner) { favoriteRecipes ->
                // zastapic to ta hashtable? hashmap
                favoriteRecipes?.forEach {
                    if (it.id == resipiAjdi) {
                        binding.recipeBinding = it.detailedRecipe
                    }
                }
            }
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readDetailedRecipes.observeOnce(viewLifecycleOwner) { detailedRecipesEntity ->
                // TODO teraz tutaj zrobic hashmape id z tabeli w db porownac do argumentu z nawigacji i zbindowac recipeBinding? bez
                //  sensu no bo i tak najpierw robie forEach aby potem zrobic z tego mape xDD
                if (!detailedRecipesEntity.isNullOrEmpty()) {
                    detailedRecipesEntity.forEach {
                        if (it.id == resipiAjdi) {
                            binding.recipeBinding = it.detailedRecipe
                            mainViewModel.currentRecipe =  it.detailedRecipe
                        }
                    }
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        val args = arguments
        val recipeId = args!!.getBundle("recipeBundle")?.getInt("recipeId")
        resipiAjdi
        fun applyQuery(): String {
            return recipeId.toString()
        }

        fun applyApiKey(): HashMap<String, String> {
            val queries: HashMap<String, String> = HashMap()
            queries[Constants.QUERY_API_KEY] = Constants.API_KEY

            return queries
        }

//        mainViewModel.getDetailedRecipe(applyQuery(), applyApiKey())

        mainViewModel.detailedRecipesResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
//                    response.data?.let { binding.recipeBinding } //TODO dlaczego z tym let nie dziala??
//                    binding.recipeBinding = response.data
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}