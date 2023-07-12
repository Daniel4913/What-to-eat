package com.example.whattoeat.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whattoeat.adapters.IngredientsAdapter
import com.example.whattoeat.databinding.FragmentIngredientsBinding
import com.example.whattoeat.viewmodels.DetailsViewModel
import com.example.whattoeat.viewmodels.RecipesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class IngredientsFragment : Fragment() {

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        setupRecyclerView()

        val args = requireArguments()
        val recipeId = args.getBundle("recipeBundle")?.getInt("recipeId")!!
        val loadFavorite = args.getBundle("recipeBundle")?.getBoolean("loadFavorite")
        if (loadFavorite!!) {
            lifecycleScope.launch {
                detailsViewModel.readFavorite(recipeId)
                    .observe(viewLifecycleOwner) { favoriteEntity ->
                        favoriteEntity.detailedRecipe.extendedIngredients.let {
                            ingredientsAdapter.setIngredients(it)
                        }
                    }
            }
        } else {
            lifecycleScope.launch {
                detailsViewModel.readCurrentRecipe(recipeId)
                    .observe(viewLifecycleOwner) { detailedEntity ->
                        if (detailedEntity != null) {
                            ingredientsAdapter.setIngredients(detailedEntity.detailedRecipe.extendedIngredients)
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
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.ingredientsRecyclerview.adapter = ingredientsAdapter
        binding.ingredientsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}