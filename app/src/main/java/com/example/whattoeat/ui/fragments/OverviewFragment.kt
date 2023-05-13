package com.example.whattoeat.ui.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.whattoeat.databinding.FragmentOverviewBinding
import com.example.whattoeat.util.NetworkListener
import com.example.whattoeat.viewmodels.MainViewModel
import com.example.whattoeat.viewmodels.RecipesViewModel
import kotlinx.coroutines.launch


class OverviewFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var networkListener: NetworkListener

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
                    showNetworkStatus()
                    val args = arguments
                    val recipeId = args!!.getBundle("recipeBundle")?.getInt("recipeId")!!
                    val loadFavorites = args!!.getBundle("recipeBundle")?.getBoolean("loadFavorite")
                    if (loadFavorites!!) {
                        readFavorites(recipeId)
                    } else {
                        readDatabase(recipeId)
                    }
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

    private fun readFavorites(recipeId: Int) {
        lifecycleScope.launch {
            mainViewModel.readFavorites.observe(viewLifecycleOwner) { favoriteRecipes ->
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
            mainViewModel.readDetailedRecipes.observe(viewLifecycleOwner) { detailedEntities ->
                if (!detailedEntities.isNullOrEmpty()) {
                    detailedEntities.forEach { entity ->
                        if (entity.id == recipeId) {
                            binding.recipeBinding = entity.detailedRecipe
                            binding.fragmentActivity = requireActivity()
                            mainViewModel.currentRecipe = entity.detailedRecipe
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}