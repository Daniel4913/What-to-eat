package com.example.whattoeat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.whattoeat.adapters.FavoritesAdapter
import com.example.whattoeat.databinding.FragmentFavoritesBinding
import com.example.whattoeat.viewmodels.MainViewModel
import kotlinx.coroutines.launch


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: FavoritesAdapter

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        mAdapter = FavoritesAdapter {
            findNavController().navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToDetailsActivity(
                    it.id
                )
            )
        }

        setupRecyclerView()

        lifecycleScope.launch {
            mainViewModel.readFavorites.observe(viewLifecycleOwner) { favoriteEntities ->
                if (favoriteEntities.isEmpty()) {
                    binding.noDataImageView.visibility = View.VISIBLE
                    binding.noDataTextView.visibility = View.VISIBLE
                } else {
                    binding.noDataImageView.visibility = View.INVISIBLE
                    binding.noDataTextView.visibility = View.INVISIBLE
                    mAdapter.setData(favoriteEntities)
                }
            }
        }



        return binding.root
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