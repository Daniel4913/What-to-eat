package com.example.whattoeat.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.whattoeat.databinding.RecipesBottomSheetBinding
import com.example.whattoeat.util.Constants.DEFAULT_INGREDIENTS
import com.example.whattoeat.util.Constants.DEFAULT_RANKING
import com.example.whattoeat.viewmodels.RecipesViewModel
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

        recipesViewModel.readIngredientsAndRanking.asLiveData()
            .observe(viewLifecycleOwner) { value ->
                ranking = value.selectedRanking
                ingredients = value.typedIngredients
                updateSwitch(value.selectedRanking, binding.rankingSwitch)
                updateEditText(value.typedIngredients, binding.myIngredientsEditText)
            }

        binding.rankingSwitch.isChecked

        binding.rankingSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = "Maximize used ingredients first"
                ranking = "1"
                updateSwitch(ranking, binding.rankingSwitch)
            } else {
                buttonView.text = "Minimize missing ingredients first"
                ranking = "2"
                updateSwitch(ranking, binding.rankingSwitch)
            }
        }


        binding.searchBtn.setOnClickListener {
            ingredients = binding.myIngredientsEditText.text.toString()
            recipesViewModel.saveIngredientsTemp(
                ranking,
                ingredients
            )
            val action =
                RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateEditText(typedIngredients: String, myIngredientsEditText: TextInputEditText) {
        myIngredientsEditText.setText(typedIngredients)
    }

    private fun updateSwitch(selectedRanking: String, rankingSwitch: SwitchMaterial) {
        if (selectedRanking == "1") {
            rankingSwitch.isChecked = true
            rankingSwitch.text = "Maximize used ingredients first"
        } else if (selectedRanking == "2") {
            rankingSwitch.isChecked = false
            rankingSwitch.text = "Minimize missing ingredients first"
        }
    }

    private fun setupEditText() {
        binding.myIngredientsEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.myIngredientsEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.myIngredientsEditText.imeOptions
    }


}