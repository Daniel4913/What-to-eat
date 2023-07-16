package com.example.whattoeat.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.whattoeat.R
import com.example.whattoeat.model.NutrientX
import com.example.whattoeat.model.Property
import com.example.whattoeat.util.Constants.CALORIES
import com.example.whattoeat.util.Constants.CARBOHYDRATES
import com.example.whattoeat.util.Constants.FAT
import com.example.whattoeat.util.Constants.GLYCEMIC_INDEX
import com.example.whattoeat.util.Constants.GLYCEMIC_LOAD
import com.example.whattoeat.util.Constants.NUTRITION_SCORE
import com.example.whattoeat.util.Constants.PROTEIN
import com.example.whattoeat.util.Constants.SATURATED_FAT
import com.example.whattoeat.util.Constants.SUGAR

class NutritionFragmentBinding {
    companion object {
        @BindingAdapter("getNutrient")
        @JvmStatic
        fun getNutrient(textView: TextView, nutrientX: List<NutrientX>?) {
            nutrientX?.forEach { nutrientItem ->
                when (nutrientItem.name) {
                    CALORIES -> {
                        if (textView.id == R.id.calories_textView) {
                            textView.text = textView.resources.getString(
                                R.string.calories,
                                nutrientItem.amount
                            )
                        }
                    }

                    FAT -> {
                        if (textView.id == R.id.gramFat_textView) {
                            textView.text = textView.resources.getString(
                                R.string.gram_nutrient,
                                nutrientItem.amount
                            )
                        }
                    }

                    CARBOHYDRATES -> {
                        if (textView.id == R.id.gramCarb_textView) {
                            textView.text = textView.resources.getString(
                                R.string.gram_nutrient,
                                nutrientItem.amount
                            )
                        }
                    }

                    PROTEIN -> {
                        if (textView.id == R.id.gramProtein_textView) {
                            textView.text = textView.resources.getString(
                                R.string.gram_nutrient,
                                nutrientItem.amount
                            )
                        }
                    }

                    SATURATED_FAT -> {
                        if (textView.id == R.id.gramSaturatedFat_textView) {
                            textView.text = textView.resources.getString(
                                R.string.saturated_fat,
                                nutrientItem.amount
                            )
                        }
                    }

                    SUGAR -> {
                        if (textView.id == R.id.gramSugar_textView) {
                            textView.text =
                                textView.resources.getString(R.string.sugar, nutrientItem.amount)
                        }
                    }

                }
            }

        }

        @BindingAdapter("getProperty")
        @JvmStatic
        fun getProperty(textView: TextView, properties: List<Property>?) {
            properties?.forEach { property ->
                when (property.name) {
                    GLYCEMIC_INDEX -> {
                        if (textView.id == R.id.glycemicIndex_textView) {
                            textView.text = textView.resources.getString(
                                R.string.glycemic_index,
                                property.amount
                            )
                        }
                    }

                    GLYCEMIC_LOAD -> {
                        if (textView.id == R.id.glycemicLoad_textView) {
                            textView.text = textView.resources.getString(
                                R.string.glycemic_load,
                                property.amount
                            )
                        }
                    }

                    NUTRITION_SCORE -> {
                        if (textView.id == R.id.nutritionScore_textView) {
                            textView.text = textView.resources.getString(
                                R.string.nutrition_score,
                                property.amount
                            )

                        }
                    }
                }
            }
        }

        @BindingAdapter("setNutriScore")
        @JvmStatic
        fun setNutriScore(imageView: ImageView, properties: List<Property>?) {
            properties?.forEach {
                if (it.name == NUTRITION_SCORE) {
                    when (it.amount) {
                        in -15.0..-1.0 -> {
                            imageView.setImageResource(R.drawable.nutri_a)
                        }

                        in -0.0..2.0 -> {
                            imageView.setImageResource(R.drawable.nutri_b)
                        }

                        in -3.0..10.0 -> {
                            imageView.setImageResource(R.drawable.nutri_c)
                        }

                        in 11.0..18.0 -> {
                            imageView.setImageResource(R.drawable.nutri_d)
                        }

                        in 19.0..40.0 -> {
                            imageView.setImageResource(R.drawable.nutri_e)
                        }
                    }
                }
            }
        }

        @BindingAdapter("getServings")
        @JvmStatic
        fun getServings(textView: TextView, servings: Int?) {
            textView.text = textView.resources.getString(
                R.string.servings,
                servings
            )
        }
    }
}