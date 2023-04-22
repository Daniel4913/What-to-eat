package com.example.whattoeat.bindingadapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.whattoeat.R
import com.example.whattoeat.model.MissedIngredient
import com.example.whattoeat.model.UsedIngredient
import com.example.whattoeat.ui.fragments.RecipesFragmentDirections
import org.jsoup.Jsoup

class RecipesItemBinding() {

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
                error(R.drawable.baseline_wifi_tethering_error_24)
            }
        }

        @BindingAdapter("setImage")
        @JvmStatic
        fun setImage(imageView: ImageView, imageUrl: String?) {
            //TODO dlaczego w OverviewFragment zwraca mi odrazu null a w RecipesFragment stevdza nie dostaje nulla?
            if (imageUrl != null) {
                imageView.load(imageUrl) {
                    crossfade(600)
                    error(R.drawable.baseline_wifi_tethering_error_24)
                }
            }
        }

        @BindingAdapter("setMyIngredients")
        @JvmStatic
        fun setMyIngredients(textView: TextView, ingredients: List<UsedIngredient>) {
            var listIngredients = mutableListOf<String>()
            ingredients.forEach {
                listIngredients.add("✓ ${it.original}\n")
            }

            textView.text = listIngredients.toString().replace(",", "").removeSurrounding("[", "]")
        }

        @BindingAdapter("setMissingIngredients")
        @JvmStatic
        fun setMissingIngredients(textView: TextView, ingredients: List<MissedIngredient>) {
            val listIngredients = mutableListOf<String>()
            ingredients.forEach {
                listIngredients.add("+ ${it.original}\n")
            }
            textView.text = listIngredients.toString().replace(",", "").removeSurrounding("[", "]")

        }
        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?){
            if(description != null) {
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }


    }
}