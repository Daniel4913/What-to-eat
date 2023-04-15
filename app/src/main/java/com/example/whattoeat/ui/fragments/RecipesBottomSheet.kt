package com.example.whattoeat.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.whattoeat.R
import com.example.whattoeat.databinding.RecipesBottomSheetBinding
import com.example.whattoeat.util.Constants.DEFAULT_INGREDIENTS
import com.example.whattoeat.util.Constants.DEFAULT_RANKING
import com.example.whattoeat.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText


class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var ranking = DEFAULT_RANKING
    private var ingredients = DEFAULT_INGREDIENTS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

        setupEditText()

        recipesViewModel.readIngredients.asLiveData().observe(viewLifecycleOwner) { value ->
            ranking = value.selectedRanking
            ingredients = value.typedIngredients
            updateSwitch(value.selectedRanking, binding.rankingSwitch)
            updateEditText(value.typedIngredients, binding.myIngredientsEditText)
        }

        binding.rankingSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = "Maximize used ingredients first"
                ranking = "1"
            } else {
                buttonView.text = "Minimize missing ingredients first"
                ranking = "2"
            }
        }

        binding.searchBtn.setOnClickListener {
            ingredients = binding.myIngredientsEditText.text.toString()
            recipesViewModel.saveIngredients(
                ranking,
                ingredients
            )
            val action =
                RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        binding.favoritesBtn.setOnClickListener {
            findNavController().navigate(RecipesBottomSheetDirections.actionRecipesBottomSheetToFavoritesFragment())
        }

        return binding.root
    }

    private fun updateEditText(typedIngredients: String, myIngredientsEditText: TextInputEditText) {
        myIngredientsEditText.setText(typedIngredients)
    }

    private fun updateSwitch(selectedRanking: String, rankingSwitch: SwitchMaterial) {
        rankingSwitch.isChecked = selectedRanking == "1"
//        if (selectedRanking == "1") {
//            rankingSwitch.isChecked = true
//        } else {
//            rankingSwitch.isChecked = false
//        }
    }

    private fun setupEditText() {
        binding.myIngredientsEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.myIngredientsEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.myIngredientsEditText.imeOptions
    }


}