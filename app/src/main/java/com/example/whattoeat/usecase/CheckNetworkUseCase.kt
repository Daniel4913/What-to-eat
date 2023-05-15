package com.example.whattoeat.usecase

import android.content.Context
import android.widget.Toast
import com.example.whattoeat.data.DataStoreRepository
import javax.inject.Inject

class CheckNetworkUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun showNetworkStatus(context: Context, networkStatus: Boolean, backOnline: Boolean) {
        if (!networkStatus) {
            Toast.makeText(
                context,
                "No internet connection",
                Toast.LENGTH_SHORT
            ).show()
            dataStoreRepository.saveBackOnline(true)
        } else {
            if (backOnline) {
                Toast.makeText(
                    context,
                    "Back online in internet",
                    Toast.LENGTH_SHORT
                ).show()
                dataStoreRepository.saveBackOnline(false)
            }
        }
    }
}
