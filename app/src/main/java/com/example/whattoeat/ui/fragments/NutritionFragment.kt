package com.example.whattoeat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whattoeat.adapters.NutritionsAdapter
import com.example.whattoeat.databinding.FragmentNutritionBinding
import com.example.whattoeat.model.DetailedRecipe
import com.example.whattoeat.viewmodels.MainViewModel
import kotlinx.coroutines.launch


class NutritionFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentNutritionBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { NutritionsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionBinding.inflate(inflater, container, false)
        setupRecyclerView()
        val args = requireArguments()
        val recipeId = args.getBundle("recipeBundle")?.getInt("recipeId")!!
        val loadFavorite = args.getBundle("recipeBundle")?.getBoolean("loadFavorite")
        if (loadFavorite!!) {
            lifecycleScope.launch {
                mainViewModel.readFavorite(recipeId).observe(viewLifecycleOwner) { favoriteEntity ->
                    setNutrientsData(favoriteEntity.detailedRecipe)
                }
            }
        } else {
            lifecycleScope.launch {
                mainViewModel.readCurrentRecipe(recipeId)
                    .observe(viewLifecycleOwner) { detailedEntity ->
                        setNutrientsData(detailedEntity.detailedRecipe)
                    }
            }
        }

        return binding.root
    }

    private fun setNutrientsData(detailedRecipe: DetailedRecipe) {
        val nutrition = detailedRecipe.nutrition
        val nutrients = detailedRecipe.nutrition.nutrients
        val caloricBreakdown = detailedRecipe.nutrition.caloricBreakdown
        val property = detailedRecipe.nutrition.properties
        binding.recipe = detailedRecipe
        binding.nutrients = nutrients
        binding.caloricBreakdown = caloricBreakdown
        binding.property = property
        binding.nutrition = nutrition
        mAdapter.setData(nutrients)
    }

    private fun setupRecyclerView() {
        binding.nutritionsRecyclerView.adapter = mAdapter
        binding.nutritionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}