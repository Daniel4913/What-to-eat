package com.example.whattoeat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.whattoeat.databinding.NutritionItemBinding
import com.example.whattoeat.model.NutrientX
import com.example.whattoeat.util.RecipesDiffUtil

class NutritionsAdapter : RecyclerView.Adapter<NutritionsAdapter.NutritionsViewHolder>() {
    private var nutritionsList = emptyList<NutrientX>()

    class NutritionsViewHolder(val binding: NutritionItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionsViewHolder {
        return NutritionsViewHolder(
            NutritionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return nutritionsList.size
    }

    override fun onBindViewHolder(holder: NutritionsViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = nutritionsList[position].name
            tvAmount.text = String.format("%.2f", nutritionsList[position].amount)
            tvUnits.text = nutritionsList[position].unit
        }
    }

    fun setData(newNutritions: List<NutrientX>) {
        val diffUtil = RecipesDiffUtil(nutritionsList, newNutritions)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        nutritionsList = newNutritions
        diffUtilResult.dispatchUpdatesTo(this)
    }
}