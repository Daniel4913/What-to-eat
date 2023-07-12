package com.example.whattoeat.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.whattoeat.R
import com.example.whattoeat.util.Constants.BASE_IMAGE_URL

class IngredientsBinding {
    companion object {
        @BindingAdapter("setIngredientImage")
        @JvmStatic
        fun setIngredientImage(imageView: ImageView, ingredientUrl: String?) {
            if (ingredientUrl != null) {
                imageView.load(BASE_IMAGE_URL + ingredientUrl) {
                    crossfade(600)
                    transformations(RoundedCornersTransformation(0f, 0f, 0f, 15f))
                    error(R.drawable.baseline_wifi_tethering_error_24)
                    placeholder(R.drawable.ic_imagesearch_roller)
                }
            }
        }
    }
}