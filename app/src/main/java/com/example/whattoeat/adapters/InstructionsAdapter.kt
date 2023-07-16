package com.example.whattoeat.adapters

import android.content.Context
import android.os.Build
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.whattoeat.R
import com.example.whattoeat.databinding.InstructionsItemBinding
import com.example.whattoeat.model.Step
import com.example.whattoeat.util.RecipesDiffUtil

class InstructionsAdapter(
    private val context: Context,
    private val requireActivity: FragmentActivity
) :
    RecyclerView.Adapter<InstructionsAdapter.InstructionsViewHolder>(), ActionMode.Callback {

    private lateinit var mActionMode: ActionMode

    private var multiSelection = false
    private var selectedSteps = mutableListOf<Int>()
    private var instructionsViewHolders = arrayListOf<InstructionsViewHolder>()
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
        instructionsViewHolders.add(holder)
        holder.binding.apply {
            stepCountTextView.text = stepsList[position].number.toString()

            val names = mutableListOf<String>()
            stepsList[position].ingredients.forEach {
                names.add(it.name)
            }

            stepIngredientsTextView.text = names.toString().removeSurrounding("[", "]")

            stepDetailsTextView.text = stepsList[position].step

            if (stepsList[position].length != null) {
                val length = stepsList[position].length.number
                val unit = stepsList[position].length.unit
                stepLengthTextView.text = context.getString(R.string.step_length, length, unit)
            } else {
                stepLengthTextView.text = ""
            }
        }

        holder.itemView.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, stepsList[position].number)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, stepsList[position].number)
                true
            } else {
                applySelection(holder, stepsList[position].number)
                true
            }
        }
    }

    private fun applySelection(holder: InstructionsViewHolder, currentStep: Int) {
        if (selectedSteps.contains(currentStep)) {
            selectedSteps.remove(currentStep)
            changeStepStyle(holder, 1f, 0, 0.05f)
            applyActionModeTitle()
        } else {
            selectedSteps.add(currentStep)
            changeStepStyle(holder, 10f, 3, 0.6f)
            applyActionModeTitle()
        }
    }

    fun setInstructions(newInstructions: List<Step>) {
        val instructionsDiffUtil = RecipesDiffUtil(stepsList, newInstructions)
        val diffUtilResult = DiffUtil.calculateDiff(instructionsDiffUtil)
        stepsList = newInstructions
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun changeStepStyle(
        holder: InstructionsViewHolder,
        cardElevation: Float,
        strokeWidth: Int,
        checkMarkAlpha: Float
    ) {
        holder.binding.instructionItemCardView.apply {
            setCardElevation(cardElevation)
            setStrokeWidth(strokeWidth)
        }
        holder.binding.checkImageView.alpha = checkMarkAlpha
    }

    private fun applyActionModeTitle() {
        when (selectedSteps.size) {
            0 -> {
                multiSelection = false
                mActionMode.finish()
            }

            1 -> {
                mActionMode.title = "${selectedSteps.size} / ${stepsList.size}"
            }

            else -> {
                mActionMode.title = "${selectedSteps.size} / ${stepsList.size}"
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mActionMode = mode!!
        mActionMode.subtitle = context.getString(R.string.steps_done)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return true
    }

    fun finishActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        instructionsViewHolders.forEach { holder ->
            changeStepStyle(holder, 1.0f, 0, 0.05f)
        }
        multiSelection = false
        selectedSteps.clear()
        mode?.finish()
    }
}