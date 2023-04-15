package com.example.whattoeat.adapters

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.whattoeat.R
import com.example.whattoeat.databinding.InstructionsItemBinding
import com.example.whattoeat.model.Step
import com.example.whattoeat.util.RecipesDiffUtil

class InstructionsAdapter(private val context: Context) :
    RecyclerView.Adapter<InstructionsAdapter.InstructionsViewHolder>() {

    private var stepsList = emptyList<Step>()

    class InstructionsViewHolder(val binding: InstructionsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionsViewHolder {

        return InstructionsViewHolder(
            InstructionsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return stepsList.size
    }

    override fun onBindViewHolder(holder: InstructionsViewHolder, position: Int) {

        holder.binding.apply {
            stepCount.text = stepsList[position].number.toString()

            val names = mutableListOf<String>()
            stepsList[position].ingredients.forEach {
                names.add(it.name)
            }

            stepIngredients.text = names.toString().removeSurrounding("[", "]")

            stepDetails.text = stepsList[position].step

            if (stepsList[position].length != null) {
                val length = stepsList[position].length.number
                val unit = stepsList[position].length.unit
                stepLength.text = context.getString(R.string.step_length, length, unit)
            } else {
                stepLength.text = ""
            }

        }
    }

    fun setInstructions(newInstructions: List<Step>) {
        val instructionsDiffUtil = RecipesDiffUtil(stepsList, newInstructions)
        val diffUtilResult = DiffUtil.calculateDiff(instructionsDiffUtil)
        stepsList = newInstructions
        diffUtilResult.dispatchUpdatesTo(this)
    }
}