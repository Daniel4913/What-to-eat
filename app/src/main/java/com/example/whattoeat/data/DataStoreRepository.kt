package com.example.whattoeat.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.whattoeat.util.Constants.DEFAULT_INGREDIENTS
import com.example.whattoeat.util.Constants.DEFAULT_RANKING
import com.example.whattoeat.util.Constants.PREFERENCES_BACK_ONLINE
import com.example.whattoeat.util.Constants.PREFERENCES_INGREDIENTS_KEY
import com.example.whattoeat.util.Constants.PREFERENCES_NAME
import com.example.whattoeat.util.Constants.PREFERENCES_RANKING_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val selectedRanking = stringPreferencesKey(PREFERENCES_RANKING_KEY)
        val typedIngredients = stringPreferencesKey(PREFERENCES_INGREDIENTS_KEY)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveIngredients(
        ranking: String,
        myIngredients: String
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedRanking] = ranking
            preferences[PreferencesKeys.typedIngredients] = myIngredients
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.backOnline] = backOnline
        }
    }

    val readIngredients: Flow<IngredientsAndRanking> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedRanking =
                preferences[PreferencesKeys.selectedRanking] ?: DEFAULT_RANKING
            val typedIngredients =
                preferences[PreferencesKeys.typedIngredients] ?: DEFAULT_INGREDIENTS
            IngredientsAndRanking(
                selectedRanking,
                typedIngredients
            )
        }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferencesKeys.backOnline] ?: false
            backOnline
        }
}

data class IngredientsAndRanking(
    val selectedRanking: String,
    val typedIngredients: String,
)