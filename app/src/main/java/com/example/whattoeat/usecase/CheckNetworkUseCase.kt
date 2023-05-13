package com.example.whattoeat.usecase

import android.content.Context
import android.widget.Toast
import com.example.whattoeat.util.NetworkListener
import javax.inject.Inject

class CheckNetworkUseCase @Inject constructor(
    private val networkListener: NetworkListener
){
//    fun showNetworkStatus(context:Context ) {
//        if (!networkStatus) {
//            Toast.makeText(
//                context,
//                "No internet connection",
//                Toast.LENGTH_SHORT
//            ).show()
//            saveBackOnline(true)
//        } else {
//            if (backOnline) {
//                Toast.makeText(
//                    context,
//                    "Back online in internet",
//                    Toast.LENGTH_SHORT
//                ).show()
//                saveBackOnline(false)
//            }
//        }
//    }
}
