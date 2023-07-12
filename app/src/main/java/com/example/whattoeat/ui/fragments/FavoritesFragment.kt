package com.example.whattoeat.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.whattoeat.R
import com.example.whattoeat.adapters.FavoriteRecipesAdapter
import com.example.whattoeat.databinding.FragmentFavoritesBinding
import com.example.whattoeat.viewmodels.DetailsViewModel
import com.example.whattoeat.viewmodels.RecipesListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: FavoriteRecipesAdapter
    private lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        mAdapter = FavoriteRecipesAdapter { recipeId ->
            val action =
                FavoritesFragmentDirections.actionFavoritesFragmentToDetailsActivity(recipeId, true)
            findNavController().navigate(action)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.favorites_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.deleteAll_favorites_menu) {
                    showSnackBar()
                    viewModel.deleteAllFavorites()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupRecyclerView()
        lifecycleScope.launch {
            viewModel.readFavorites.observe(viewLifecycleOwner) { favoriteEntities ->
                if (favoriteEntities.isEmpty()) {
                    binding.noDataImageView.visibility = View.VISIBLE
                    binding.noDataTextView.visibility = View.VISIBLE
                    mAdapter.setData(favoriteEntities)
                } else {
                    binding.noDataImageView.visibility = View.INVISIBLE
                    binding.noDataTextView.visibility = View.INVISIBLE
                    mAdapter.setData(favoriteEntities)
                }
            }
        }
        return binding.root
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            "Favorites deleted",
            Snackbar.LENGTH_LONG
        ).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setAction("Ok") {}
            .show()
    }

    private fun setupRecyclerView() {
        binding.favoritesRecyclerView.adapter = mAdapter
        binding.favoritesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}