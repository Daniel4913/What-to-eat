package com.example.whattoeat.ui.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whattoeat.R
import com.example.whattoeat.adapters.InstructionsAdapter
import com.example.whattoeat.databinding.FragmentInstructionsBinding
import com.example.whattoeat.viewmodels.MainViewModel
import kotlinx.coroutines.launch


class InstructionsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!
    private val mAdapter: InstructionsAdapter by lazy { InstructionsAdapter(requireContext()) }

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
        lifecycleScope.launch {

            mainViewModel.readDetailedRecipe.observe(viewLifecycleOwner) { database ->

                if (database.detailedRecipe.analyzedInstructions.isEmpty()) {
                    binding.noInstructionsImageView.visibility = View.VISIBLE
                    binding.noInstructionsTextView.visibility = View.VISIBLE
                } else {
                    binding.noInstructionsImageView.visibility = View.INVISIBLE
                    binding.noInstructionsTextView.visibility = View.INVISIBLE
                    database.detailedRecipe.analyzedInstructions.first().let {
                        mAdapter.setInstructions(it.steps)
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


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}