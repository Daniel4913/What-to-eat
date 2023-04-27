package com.example.whattoeat.bindingadapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.whattoeat.R
import com.example.whattoeat.model.NutrientX
import com.example.whattoeat.model.Property
import com.google.android.material.button.MaterialButton

class NutritionFragmentBinding {

    companion object {

        @BindingAdapter("setRecipePageString")
        @JvmStatic
        fun setRecipePageString(materialButton: MaterialButton, sourceUrl: String?) {
            if (sourceUrl != null) {
                materialButton.text =
                    materialButton.resources.getString(R.string.visit_recipe_page, sourceUrl)
            }
        }

        @BindingAdapter("getNutrient")
        @JvmStatic
        fun getNutrient(textView: TextView, nutrientX: List<NutrientX>?) {
            nutrientX?.forEach { nutrientItem ->
                when (nutrientItem.name) {
                    "Calories" -> {
                        if (textView.id == R.id.calories_textView) {
                            textView.text = textView.resources.getString(
                                R.string.calories,
                                nutrientItem.amount
                            )
                        }
                    }
                    "Fat" -> {
                        if (textView.id == R.id.gramFat_textView) {
                            textView.text = nutrientItem.amount.toString()
                        }
                    }
                    "Carbohydrates" -> {
                        if (textView.id == R.id.gramCarb_textView) {
                            textView.text = nutrientItem.amount.toString()
                        }
                    }
                    "Protein" -> {
                        if (textView.id == R.id.gramProtein_textView) {
                            textView.text = nutrientItem.amount.toString()
                        }
                    }
                    "Saturated Fat" -> {
                        if (textView.id == R.id.gramSaturatedFat_textView) {
                            textView.text = textView.resources.getString(
                                R.string.saturated_fat,
                                nutrientItem.amount
                            )
                        }
                    }
                    "Sugar" -> {
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
                    "Glycemic Index" -> {
                        if (textView.id == R.id.glycemicIndex_textView) {
//                            textView.text = property.amount.toString()
                            textView.text = textView.resources.getString(
                                R.string.glycemic_index,
                                property.amount
                            )
                        }
                    }
                    "Glycemic Load" -> {
                        if (textView.id == R.id.glycemicLoad_textView) {
                            textView.text = textView.resources.getString(
                                R.string.glycemic_load,
                                property.amount
                            )
                        }
                    }
                    "Nutrition Score" -> {
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
                Log.d("changeNutriScore", "${it.name} ${it.amount}")
                if (it.name == "Nutrition Score") {
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