package com.example.whattoeat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whattoeat.R
import com.example.whattoeat.viewmodels.MainViewModel
import com.example.whattoeat.adapters.RecipesAdapter
import com.example.whattoeat.data.database.entities.DetailedRecipeEntity
import com.example.whattoeat.data.database.entities.RecipeByIngredientEntity
import com.example.whattoeat.databinding.FragmentRecipesBinding
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.model.RecipesByIngredients
import com.example.whattoeat.util.NetworkListener
import com.example.whattoeat.util.NetworkResult
import com.example.whattoeat.util.observeOnce
import com.example.whattoeat.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var networkListener: NetworkListener

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel


//        setupMenu()
        setupRecyclerView()


        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    recipesViewModel.networkStatus = status
                    showNetworkStatus()
                    readDatabase()
                }
        }

        return binding.root
    }

    private fun showNetworkStatus() {
        if (!recipesViewModel.networkStatus) {
            Toast.makeText(
                requireContext(),
                "No internet connection",
                Toast.LENGTH_SHORT
            ).show()
            recipesViewModel.saveBackOnline(true)
        } else {
            if (recipesViewModel.backOnline) {
                Toast.makeText(
                    requireContext(),
                    "Back online in internet",
                    Toast.LENGTH_SHORT
                ).show()
                recipesViewModel.saveBackOnline(false)
            }
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.bottom_nav_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                if (menuItem.itemId == android.R.id.home) {
//
                } else if (menuItem.itemId == R.id.recipesFragment) {
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun setupRecyclerView() {
        binding.recipesRecyclerView.adapter = mAdapter
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readCachedRecipesByIngredients.observeOnce(viewLifecycleOwner) { database ->
                Log.d("RecipesFragment", "readDatabase called")
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setData(database[0].recipesByIngredients)
                    Log.d("RecipesFragment", "readDatabase called")
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        mainViewModel.getRecipesByIngredients(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let { mAdapter.setData(it) }
                    recipesViewModel.saveIngredients()
                    val recipes: RecipesByIngredients? = response.data
                    requestDetailedRecipesApiData(recipesViewModel.applyDetailedQueries(recipes))
                    cacheRecipes(recipes)
                }
                is NetworkResult.Error -> {
                    readDataFromCache()
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

    private fun cacheRecipes(recipes: RecipesByIngredients?) {
        val recipeEntity = recipes?.let { RecipeByIngredientEntity(it) }
        if (recipeEntity != null) {
            mainViewModel.insertRecipes(recipeEntity)
        }
    }

    private fun requestDetailedRecipesApiData(queries: Map<String, String>) {
        mainViewModel.getDetailedRecipes(queries)
        cacheDetailedRecipes()
    }

    private fun cacheDetailedRecipes() {
        mainViewModel.detailedRecipesResponse.value?.data?.forEach {
            val detailedRecipe = DetailedRecipeEntity(it.id, it)
            mainViewModel.insertDetailedRecipe(detailedRecipe)
        }
    }

    private fun readDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readCachedRecipesByIngredients.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].recipesByIngredients)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}