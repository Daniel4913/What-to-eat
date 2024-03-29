package com.example.whattoeat.bindingadapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.whattoeat.R
import com.example.whattoeat.model.MissedIngredient
import com.example.whattoeat.model.UsedIngredient
import com.example.whattoeat.ui.fragments.RecipesFragmentDirections
import org.jsoup.Jsoup
import java.util.*

class RecipesBinding {
    companion object {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(itemLayout: ConstraintLayout, recipeId: Int) {
            itemLayout.setOnClickListener {
                try {
                    val action =
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(recipeId)
                    itemLayout.findNavController().navigate(action)
                } catch (e: java.lang.Exception) {
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }


        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl) {
                crossfade(600)
                transformations(RoundedCornersTransformation(0f, 0f, 0f, 15f))
                error(R.drawable.baseline_wifi_tethering_error_24)
                placeholder(R.drawable.ic_imagesearch_roller)
            }
        }

        @BindingAdapter("setList")
        @JvmStatic
        fun setList(textView: TextView, list: List<Any>?) {
            if (list != null) {
                val capitalizedItems = mutableListOf<String>()
                list.forEach { item ->
                    val itemList = item.toString()
                        .replaceFirstChar { firstChar ->
                            firstChar.uppercaseChar()
                        }
                    capitalizedItems.add(itemList)
                }
                textView.text = capitalizedItems.toString()
                    .removeSurrounding("[", "]")
                    .replace(",", " -")

            } else {
                textView.text = textView.resources.getString(R.string.no_data)
            }
        }

        @BindingAdapter("setMyIngredients")
        @JvmStatic
        fun setMyIngredients(textView: TextView, ingredients: List<UsedIngredient>) {
            val listIngredients = mutableListOf<String>()
            ingredients.forEach {
                listIngredients.add("✓ ${it.original}\n")
            }

            textView.text = listIngredients.toString().replace(",", "").removeSurrounding("[", "]")
        }

        @BindingAdapter("setMissingIngredients")
        @JvmStatic
        fun setMissingIngredients(textView: TextView, ingredients: List<MissedIngredient>?) {
            if (!ingredients.isNullOrEmpty()) {
                val listIngredients = mutableListOf<String>()
                ingredients.forEach {
                    listIngredients.add("+ ${it.original}\n")
                }
                textView.text =
                    listIngredients.toString().replace(",", "").removeSurrounding("[", "]")

            } else {
                textView.text = textView.resources.getString(R.string.no_missed_ingredients)
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?) {
            if (description != null) {
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }


    }
}