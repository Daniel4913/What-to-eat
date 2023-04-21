package com.example.whattoeat.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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

    private var multiSelection = false

    private lateinit var mActionMode: ActionMode

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

        holder.itemView.setOnClickListener {
            if(multiSelection){
                applySelection(holder,stepsList[position].number)
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

    private fun applyActionModeTitle(){
        when(selectedSteps.size){
            0 ->{
                multiSelection = false
                mActionMode.finish()
            }
            1-> {
                mActionMode.title = "${selectedSteps.size} / ${stepsList.size}"
            }
            else ->{
                mActionMode.title = "${selectedSteps.size} / ${stepsList.size}"
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mActionMode = mode!!
//        mActionMode.customView.textAlignment = View.TEXT_ALIGNMENT_CENTER nullPointerException
//        mActionMode.customView.visibility = View.INVISIBLE nullPointer
//        mActionMode.customView = get/set customView required View! //
        mActionMode.subtitle = "Steps done"

//        mode?.title = "${selectedSteps.size.plus(1)} / ${stepsList.size}"

//        requireActivity.window.statusBarColor =
//            ContextCompat.getColor(requireActivity, R.color.md_theme_light_surfaceTint)

//        (requireActivity as? AppCompatActivity)?.supportActionBar?.title = "/ ${stepsList.size}"
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return true
    }

    fun finishActionMode(){
        if(this::mActionMode.isInitialized){
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