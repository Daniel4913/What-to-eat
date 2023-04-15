package com.example.whattoeat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whattoeat.R
import com.example.whattoeat.adapters.NutritionsAdapter
import com.example.whattoeat.databinding.FragmentNutritionBinding
import com.example.whattoeat.util.observeOnce
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
        // Inflate the layout for this fragment
        _binding = FragmentNutritionBinding.inflate(inflater, container, false)
        setupRecyclerView()

        lifecycleScope.launch {
            mainViewModel.readDetailedRecipe.observeOnce(viewLifecycleOwner) { database ->
                val nutrition = database.detailedRecipe.nutrition
                val nutrients = database.detailedRecipe.nutrition.nutrients
                val caloricBreakdown = database.detailedRecipe.nutrition.caloricBreakdown
                val property = database.detailedRecipe.nutrition.properties

                binding.recipe = database.detailedRecipe
                binding.nutrients = nutrients
                binding.caloricBreakdown = caloricBreakdown
                binding.property = property
                binding.nutrition = nutrition

                mAdapter.setData(nutrients)
            }
        }

        return binding.root
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