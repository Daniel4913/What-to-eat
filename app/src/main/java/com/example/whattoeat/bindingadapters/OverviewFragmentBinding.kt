package com.example.whattoeat.bindingadapters

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.whattoeat.R
import com.google.android.material.button.MaterialButton


class OverviewFragmentBinding {
    companion object {
        @BindingAdapter("setImage")
        @JvmStatic
        fun setImage(imageView: ImageView, imageUrl: String?) {
            if (imageUrl != null) {
                imageView.load(imageUrl) {
                    crossfade(600)
                    transformations(
                        RoundedCornersTransformation(0f, 0f, 15f, 15f),
                    )
                    error(R.drawable.baseline_wifi_tethering_error_24)
                    placeholder(R.drawable.ic_imagesearch_roller).scale(Scale.FIT)
                }
            }
        }

        @BindingAdapter("passActivity", "openWebsite")
        @JvmStatic
        fun openWebsite(button: MaterialButton, activity: FragmentActivity?, sourceUrl: String?) {
            if (sourceUrl != null) {
                button.text =
                    button.resources.getString(R.string.visit_recipe_page, sourceUrl)
                button.setOnClickListener {
                    if (activity != null) {
                        val webpage: Uri = Uri.parse(sourceUrl)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        activity.startActivity(intent)
                    }
                }
            }
        }
    }
}