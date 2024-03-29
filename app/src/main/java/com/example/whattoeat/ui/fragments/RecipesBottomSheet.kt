package com.example.whattoeat.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.whattoeat.R
import com.example.whattoeat.databinding.RecipesBottomSheetBinding
import com.example.whattoeat.util.Constants.DEFAULT_INGREDIENTS
import com.example.whattoeat.util.Constants.DEFAULT_RANKING
import com.example.whattoeat.util.Constants.RANKING_1
import com.example.whattoeat.util.Constants.RANKING_2
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
        // style for not cover search button by keyboard
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.rankingSwitch.toggle()

        binding.rankingSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.maximize_used_ingredients)
                ranking = RANKING_1
                updateSwitch(ranking, binding.rankingSwitch)
            } else {
                buttonView.text = getString(R.string.minimize_missing_ingredients)
                ranking = RANKING_2
                updateSwitch(ranking, binding.rankingSwitch)
            }
        }

        binding.searchButton.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                ingredients = binding.myIngredientsEditText.text.toString()
                recipesViewModel.saveIngredientsTemp(
                    ranking,
                    ingredients
                )
                val action =
                    RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
                findNavController().navigate(action)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
        return binding.root
    }

    private fun updateEditText(typedIngredients: String, myIngredientsEditText: TextInputEditText) {
        myIngredientsEditText.setText(typedIngredients)
    }

    private fun updateSwitch(selectedRanking: String, rankingSwitch: SwitchMaterial) {
        if (selectedRanking == RANKING_1) {
            rankingSwitch.isChecked = true
            rankingSwitch.text = getString(R.string.maximize_used_ingredients)
        } else if (selectedRanking == RANKING_2) {
            rankingSwitch.isChecked = false
            rankingSwitch.text = getString(R.string.minimize_missing_ingredients)
        }
    }

    private fun setupEditText() {
        binding.myIngredientsEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.myIngredientsEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.myIngredientsEditText.imeOptions
    }
}