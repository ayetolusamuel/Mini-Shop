package com.codingwithset.minie_commerce.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Message
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.codingwithset.minie_commerce.R


/*
this function [callSeller] call the merchant
 */
fun View.callSeller() {
    val phone: String = context.getString(R.string.seller_number_developer)
    val intent =
        Intent(Intent.ACTION_DIAL, Uri.fromParts(context.getString(R.string.tel), phone, null))
    context.startActivity(intent)
}

/*
set the view visibility to be visible
 */
fun View.visible() {
    this.visibility = View.VISIBLE

}

/*
set the view visibility to be gone
 */
fun View.gone() {
    this.visibility = View.GONE

}

///**
// * Check whether network is available by ping ip address/
// * @return Whether device is connected to Network.
// */
//fun checkInternetAccess():Boolean{
//    try {
//        val process: Process = Runtime.getRuntime().exec("ping -c 1 8.8.8.8")
//        val returnVal = process.waitFor()
//        return (returnVal == 0)
//    }catch (exception: Exception){
//        exception.printStackTrace()
//    }
//   return false
//
//}


/**
 * Check whether network is available
 *
 * @param context
 * @return Whether device is connected to Network.
 */
fun Context.checkInternetAccess(): Boolean {
    try {

        with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Device is running on Marshmallow or later Android OS.
                with(getNetworkCapabilities(activeNetwork)) {
                    return hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR
                    )
                }
            } else {
                @Suppress("DEPRECATION")
                activeNetworkInfo?.let {
                    // connected to the internet
                    @Suppress("DEPRECATION")
                    return listOf(
                        ConnectivityManager.TYPE_WIFI,
                        ConnectivityManager.TYPE_MOBILE
                    ).contains(it.type)
                }
            }
        }
        return false
    } catch (exc: NullPointerException) {
        return false
    }


}

fun Context.message(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.chatSeller() {
    try {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(this.getString(R.string.seller_whatsap_link))
        )
        this.startActivity(browserIntent)
    } catch (exception: PackageManager.NameNotFoundException) {
        Log.e("Utils", exception.toString())
        message("Whastsapp is not install on your phone")
    }


}

/*
hide the keyboard
 */
fun Activity.hideKeyboard() {
    val inputMethodManager: InputMethodManager? =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
}

