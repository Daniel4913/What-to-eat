package com.example.whattoeat.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.whattoeat.databinding.FragmentOverviewBinding
import com.example.whattoeat.util.NetworkListener
import com.example.whattoeat.viewmodels.DetailsViewModel
import com.example.whattoeat.viewmodels.RecipesListViewModel
import com.example.whattoeat.viewmodels.RecipesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class OverviewFragment : Fragment() {
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var recipeListViewModel: RecipesListViewModel
    //    private val recipeListViewModel: RecipesListViewModel by viewModels() //RE:  Cannot create an instance of class

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var networkListener: NetworkListener

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeListViewModel = ViewModelProvider(requireActivity())[RecipesListViewModel::class.java]
        detailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                recipesViewModel.networkStatus = status
                recipesViewModel.showNetworkStatus(
                    requireContext(), recipesViewModel.networkStatus, recipesViewModel.backOnline
                )
                val args = arguments
                val recipeId = args!!.getBundle("recipeBundle")?.getInt("recipeId")!!
                val loadFavorites = args.getBundle("recipeBundle")?.getBoolean("loadFavorite")
                if (loadFavorites!!) {
                    readFavorites(recipeId)
                } else {
                    readDatabase(recipeId)
                }
            }
        }
        return binding.root
    }

    private fun readFavorites(recipeId: Int) {
        lifecycleScope.launch {
            detailsViewModel.readFavorites.observe(viewLifecycleOwner) { favoriteRecipes ->
                favoriteRecipes?.forEach { favoriteEntity ->
                    if (favoriteEntity.id == recipeId) {
                        binding.recipeBinding = favoriteEntity.detailedRecipe
                        binding.dietsListTextView.isSelected = true
                        binding.cuisinesListTextView.isSelected = true
                        binding.webpageButton.text
                    }
                }
            }
        }
    }

    private fun readDatabase(recipeId: Int) {
        lifecycleScope.launch {
            recipeListViewModel.readCachedDetailedRecipes.observe(viewLifecycleOwner) { detailedEntities ->
                if (!detailedEntities.isNullOrEmpty()) {
                    detailedEntities.forEach { entity ->
                        if (entity.id == recipeId) {
                            binding.recipeBinding = entity.detailedRecipe
                            binding.fragmentActivity = requireActivity()
                            detailsViewModel.currentRecipe = entity.detailedRecipe
                        }
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        "An Error occurred. \nPlease provide back internet",
                        Snackbar.LENGTH_LONG
                    ).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setAction("Ok") {}
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}