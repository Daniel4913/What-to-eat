package com.example.whattoeat.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whattoeat.adapters.InstructionsAdapter
import com.example.whattoeat.databinding.FragmentInstructionsBinding
import com.example.whattoeat.viewmodels.MainViewModel
import kotlinx.coroutines.launch


class InstructionsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!
    private val mAdapter: InstructionsAdapter by lazy {
        InstructionsAdapter(
            requireContext(),
            requireActivity()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        val args = requireArguments()
        val recipeId = args.getBundle("recipeBundle")?.getInt("recipeId")!!
        val loadFavorite = args.getBundle("recipeBundle")?.getBoolean("loadFavorite")

        if (loadFavorite!!) {
            lifecycleScope.launch {
                mainViewModel.readFavorite(recipeId).observe(viewLifecycleOwner) { favoriteEntity ->
                    if (favoriteEntity.detailedRecipe.analyzedInstructions.isEmpty()) {
                        binding.noInstructionsImageView.visibility = View.VISIBLE
                        binding.noInstructionsTextView.visibility = View.VISIBLE
                    } else {
                        binding.noInstructionsImageView.visibility = View.INVISIBLE
                        binding.noInstructionsTextView.visibility = View.INVISIBLE
                        favoriteEntity.detailedRecipe.analyzedInstructions.first().let {
                            mAdapter.setInstructions(it.steps)
                        }
                    }
                }
            }
        } else {
            lifecycleScope.launch {
                mainViewModel.readCurrentRecipe(recipeId)
                    .observe(viewLifecycleOwner) { detailedEntity ->
                        if (detailedEntity.detailedRecipe.analyzedInstructions.isEmpty()) {
                            binding.noInstructionsImageView.visibility = View.VISIBLE
                            binding.noInstructionsTextView.visibility = View.VISIBLE
                        } else {
                            binding.noInstructionsImageView.visibility = View.INVISIBLE
                            binding.noInstructionsTextView.visibility = View.INVISIBLE
                            detailedEntity.detailedRecipe.analyzedInstructions.first()
                                .let {
                                    mAdapter.setInstructions(it.steps)
                                }
                        }
                    }
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.instructionsRecyclerView.adapter = mAdapter
        binding.instructionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onPause() {
        super.onPause()
        mAdapter.finishActionMode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}