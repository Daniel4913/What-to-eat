package com.example.whattoeat.ui

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.whattoeat.R
import com.example.whattoeat.adapters.PagerAdapter
import com.example.whattoeat.data.database.entities.FavoriteRecipeEntity
import com.example.whattoeat.databinding.ActivityDetailsBinding
import com.example.whattoeat.ui.fragments.IngredientsFragment
import com.example.whattoeat.ui.fragments.InstructionsFragment
import com.example.whattoeat.ui.fragments.NutritionFragment
import com.example.whattoeat.ui.fragments.OverviewFragment
import com.example.whattoeat.util.observeOnce
import com.example.whattoeat.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityDetailsBinding

    private lateinit var menuItem: MenuItem
    private lateinit var currentConfiguration: Configuration

    private var recipeSaved = false
    private var savedRecipeId = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.detailsToolbar)
        currentConfiguration = resources?.configuration!!

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())
        fragments.add(NutritionFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")
        titles.add("Nutrition")
        val icons = ArrayList<Drawable>()
        getDrawable(R.drawable.ic_checkmark)?.let { icons.add(it) }
        getDrawable(R.drawable.ic_molecule)?.let { icons.add(it) }
        getDrawable(R.drawable.ic_molecule)?.let { icons.add(it) }
        getDrawable(R.drawable.ic_molecule)?.let { icons.add(it) }

        val recipeBundle = Bundle()
        recipeBundle.putParcelable("recipeBundle", args.toBundle())

        val pagerAdapter = PagerAdapter(
            recipeBundle,
            fragments,
            this
        )
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.apply { adapter = pagerAdapter }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
            tab.icon = icons[position]
        }.attach()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorites_menu_item)
        checkSavedRecipes(menuItem)
        setNightModeBookmarkIconColor()
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu_item && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu_item && recipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavorites.observe(this) { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.detailedRecipe.id == args.recipeId) {
                        changeMenuItem(
                            menuItem,
                            R.color.md_theme_dark_primary,
                            R.drawable.bookmark_added
                        )
                        savedRecipeId = savedRecipe.detailedRecipe.id
                        recipeSaved = true
                        setNightModeBookmarkIconColor()
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.d("DetailsActivity checkSavedRecipes", e.message.toString())
            }
        }
    }

    private fun removeFromFavorites(item: MenuItem) {
        mainViewModel.readCurrentRecipe(args.recipeId).observeOnce(this) {
            val favoriteRecipeEntity =
                FavoriteRecipeEntity(savedRecipeId, it.detailedRecipe)
            mainViewModel.deleteFavorite(favoriteRecipeEntity)
            changeMenuItem(item, R.color.md_theme_dark_onSurfaceVariant, R.drawable.bookmark_add)
            showSnackBar("Recipe removed from favorites")
            recipeSaved = false
            setNightModeBookmarkIconColor()
        }

    }

    private fun saveToFavorites(item: MenuItem) {
        mainViewModel.readCurrentRecipe(args.recipeId).observeOnce(this) {
            val favoriteRecipeEntity =
                FavoriteRecipeEntity(it.detailedRecipe.id, it.detailedRecipe)
            mainViewModel.insertFavorite(favoriteRecipeEntity)
        }
        changeMenuItem(item, R.color.md_theme_dark_onPrimaryContainer, R.drawable.bookmark_added)
        recipeSaved = true
        showSnackBar("Recipe saved!")
        setNightModeBookmarkIconColor()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT,
        ).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setAction("Ok") {}.show()
    }

    private fun changeMenuItem(
        item: MenuItem,
        color: Int,
        icon: Int
    ) {
        item.setIcon(icon)
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }


    private fun setNightModeBookmarkIconColor() {
        if (this::currentConfiguration.isInitialized) {
            val isNightModeActive =
                currentConfiguration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            if (recipeSaved) {
                if (isNightModeActive) {
                    changeMenuItem(
                        menuItem,
                        R.color.md_theme_dark_primary,
                        R.drawable.bookmark_added
                    )
                } else {
                    changeMenuItem(
                        menuItem,
                        R.color.md_theme_light_primary,
                        R.drawable.bookmark_added
                    )
                }
            } else {
                if (isNightModeActive) {
                    changeMenuItem(
                        menuItem,
                        R.color.md_theme_dark_onSurfaceVariant,
                        R.drawable.bookmark_add
                    )
                } else {
                    changeMenuItem(
                        menuItem,
                        R.color.md_theme_light_onSurfaceVariant,
                        R.drawable.bookmark_add
                    )
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        setNightModeBookmarkIconColor()
        changeMenuItem(menuItem, R.color.white, R.drawable.bookmark_add)

    }
}