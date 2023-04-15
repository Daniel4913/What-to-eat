package com.example.whattoeat.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.whattoeat.R
import com.example.whattoeat.adapters.PagerAdapter
import com.example.whattoeat.data.database.entities.FavoriteEntity
import com.example.whattoeat.databinding.ActivityDetailsBinding
import com.example.whattoeat.ui.fragments.IngredientsFragment
import com.example.whattoeat.ui.fragments.InstructionsFragment
import com.example.whattoeat.ui.fragments.NutritionFragment
import com.example.whattoeat.ui.fragments.OverviewFragment
import com.example.whattoeat.util.NetworkListener
import com.example.whattoeat.util.observeOnce
import com.example.whattoeat.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args: DetailsActivityArgs by navArgs()

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var networkListener: NetworkListener

    private lateinit var binding: ActivityDetailsBinding

    private lateinit var menuItem: MenuItem

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false) kiedy to jest to toolbar jest przesuniety pod belke (czyli caly widok. z 'false' ignorowane sa navigation i appbar)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.detailsToolbar)
//        binding.detailsToolbar.setTitleTextColor(
//            ContextCompat.getColor(
//                this,
//                R.color.md_theme_dark_error
//            )
//        )

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
            recipeSaved
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.detailedRecipe.id == args.recipeId) {
                        changeMenuItem(
                            menuItem,
                            R.color.md_theme_dark_onPrimaryContainer,
                            R.drawable.bookmark_added
                        )
                        savedRecipeId = savedRecipe.detailedRecipe.id
                        recipeSaved = true

                    }
                }
            } catch (e: java.lang.Exception) {
                Log.d("DetailsActivity checkSavedRecipes", e.message.toString())
            }
        }
    }

    private fun removeFromFavorites(item: MenuItem) {
        mainViewModel.readDetailedRecipe.observeOnce(this) {
            val favoriteEntity =
                FavoriteEntity(savedRecipeId, it.detailedRecipe)
            mainViewModel.deleteFavorite(favoriteEntity)
            Log.d("removeFromFav", "readDetailedRecipe.observeOnce called")
            changeMenuItem(item, R.color.white, R.drawable.bookmark_add)
            showSnackBar("Recipe removed from favorites")
            recipeSaved = false
        }

    }

    private fun saveToFavorites(item: MenuItem) {
        mainViewModel.readDetailedRecipe.observeOnce(this) {
            val favoriteEntity =
                FavoriteEntity(it.detailedRecipe.id, it.detailedRecipe)
            mainViewModel.insertFavorite(favoriteEntity)
        }
        changeMenuItem(item, R.color.md_theme_dark_onPrimaryContainer, R.drawable.bookmark_added)
        recipeSaved = true
        showSnackBar("Recipe saved!")
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

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItem(menuItem, R.color.white, R.drawable.bookmark_add)
    }
}