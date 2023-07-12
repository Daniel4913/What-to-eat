package com.example.whattoeat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whattoeat.adapters.IngredientsAdapter
import com.example.whattoeat.databinding.FragmentIngredientsBinding
import com.example.whattoeat.viewmodels.MainViewModel
import com.example.whattoeat.viewmodels.RecipesViewModel
import kotlinx.coroutines.launch

class IngredientsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        setupRecyclerView()

        val args = requireArguments()
        val recipeId = args.getBundle("recipeBundle")?.getInt("recipeId")!!
        val loadFavorite = args.getBundle("recipeBundle")?.getBoolean("loadFavorite")
        if (loadFavorite!!) {
            lifecycleScope.launch {
                mainViewModel.readFavorite(recipeId).observe(viewLifecycleOwner) { favoriteEntity ->
                    favoriteEntity.detailedRecipe.extendedIngredients.let {
                        ingredientsAdapter.setIngredients(it)
                    }
                }
            }
        } else {
            lifecycleScope.launch {
                mainViewModel.readCurrentRecipe(recipeId)
                    .observe(viewLifecycleOwner) { detailedEntity ->
                        ingredientsAdapter.setIngredients(detailedEntity.detailedRecipe.extendedIngredients)
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